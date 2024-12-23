package tech.intellispaces.java.reflection.reference;

import tech.intellispaces.java.reflection.JavaStatements;
import tech.intellispaces.java.reflection.Statement;
import tech.intellispaces.java.reflection.StatementType;
import tech.intellispaces.java.reflection.StatementTypes;
import tech.intellispaces.java.reflection.common.JavaModelFunctions;
import tech.intellispaces.java.reflection.context.TypeContext;
import tech.intellispaces.java.reflection.session.Session;
import tech.intellispaces.action.cache.CachedSupplierActions;
import tech.intellispaces.action.supplier.SupplierAction;

import javax.lang.model.element.TypeParameterElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Adapter of {@link TypeParameterElement} to {@link NamedReference}.
 */
class NamedReferenceBasedOnTypeParameterElement extends AbstractTypeReference implements NamedReference {
  private final String name;
  private final SupplierAction<Statement> ownerGetter;
  private final SupplierAction<List<ReferenceBound>> extendedBoundsGetter;

  NamedReferenceBasedOnTypeParameterElement(
      TypeParameterElement typeParameter, TypeContext typeContext, Session session
  ) {
    super();
    ownerGetter = CachedSupplierActions.get(JavaStatements::statement, typeParameter.getGenericElement());
    this.name = typeParameter.getSimpleName().toString();
    this.extendedBoundsGetter = CachedSupplierActions.get(
        JavaModelFunctions::getExtendedBounds, typeParameter, typeContext, session);
  }

  @Override
  public StatementType statementType() {
    return StatementTypes.NamedReference;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public Statement owner() {
    return ownerGetter.get();
  }

  @Override
  public List<ReferenceBound> extendedBounds() {
    return extendedBoundsGetter.get();
  }

  @Override
  public TypeReference effective(Map<String, NotPrimitiveReference> typeMapping) {
    TypeReference specifiedReference = typeMapping.get(name);
    if (specifiedReference != null) {
      return specifiedReference;
    }
    List<ReferenceBound> curExtendedBounds = extendedBounds();
    List<ReferenceBound> newExtendedBounds = new ArrayList<>();
    for (ReferenceBound curExtendedBound : curExtendedBounds) {
      newExtendedBounds.add((ReferenceBound) curExtendedBound.effective(typeMapping));
    }
    return new NamedReferenceImpl(name, owner(), newExtendedBounds);
  }

  @Override
  public boolean isVoidType() {
    return false;
  }
}