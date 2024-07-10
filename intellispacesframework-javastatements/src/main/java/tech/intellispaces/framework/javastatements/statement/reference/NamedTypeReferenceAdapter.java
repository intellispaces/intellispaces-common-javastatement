package tech.intellispaces.framework.javastatements.statement.reference;

import tech.intellispaces.framework.commons.action.ActionBuilders;
import tech.intellispaces.framework.commons.action.Getter;
import tech.intellispaces.framework.javastatements.context.TypeContext;
import tech.intellispaces.framework.javastatements.session.Session;
import tech.intellispaces.framework.javastatements.statement.StatementType;
import tech.intellispaces.framework.javastatements.statement.StatementTypes;
import tech.intellispaces.framework.javastatements.statement.TypeElementFunctions;

import javax.lang.model.element.TypeParameterElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class NamedTypeReferenceAdapter extends AbstractTypeReference implements NamedTypeReference {
  private final String name;
  private final Getter<List<TypeBoundReference>> extendedBoundsGetter;

  NamedTypeReferenceAdapter(TypeParameterElement typeParameter, TypeContext typeContext, Session session) {
    super();
    this.name = typeParameter.getSimpleName().toString();
    this.extendedBoundsGetter = ActionBuilders.cachedLazyGetter(TypeElementFunctions::getExtendedBounds, typeParameter, typeContext, session);
  }

  @Override
  public StatementType statementType() {
    return StatementTypes.NamedTypeReference;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public List<TypeBoundReference> extendedBounds() {
    return extendedBoundsGetter.get();
  }

  @Override
  public TypeReference specify(Map<String, NonPrimitiveTypeReference> typeMapping) {
    TypeReference specifiedReference = typeMapping.get(name);
    if (specifiedReference != null) {
      return specifiedReference;
    }
    List<TypeBoundReference> curExtendedBounds = extendedBounds();
    List<TypeBoundReference> newExtendedBounds = new ArrayList<>();
    for (TypeBoundReference curExtendedBound : curExtendedBounds) {
      newExtendedBounds.add((TypeBoundReference) curExtendedBound.specify(typeMapping));
    }
    return new NamedTypeReferenceImpl(name, newExtendedBounds);
  }
}
