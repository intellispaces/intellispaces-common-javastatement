package intellispaces.javastatements.reference;

import intellispaces.actions.Actions;
import intellispaces.actions.getter.Getter;
import intellispaces.commons.type.TypeFunctions;
import intellispaces.javastatements.StatementType;
import intellispaces.javastatements.StatementTypes;
import intellispaces.javastatements.common.JavaModelFunctions;
import intellispaces.javastatements.context.TypeContext;
import intellispaces.javastatements.customtype.CustomType;
import intellispaces.javastatements.session.Session;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Adapter od {@link DeclaredType} to {@link CustomTypeReference}.
 */
class CustomTypeReferenceBasedOnDeclaredType extends AbstractCustomTypeReference {
  private final Getter<CustomType> targetTypeGetter;
  private final Getter<List<NotPrimitiveReference>> typeArgumentsGetter;
  private final Getter<Map<String, NotPrimitiveReference>> typeArgumentMappingsGetter;
  private final Getter<String> typeArgumentsDeclarationGetter;

  CustomTypeReferenceBasedOnDeclaredType(DeclaredType declaredType, TypeContext typeContext, Session session) {
    super();
    TypeElement typeElement = (TypeElement) declaredType.asElement();
    this.targetTypeGetter = Actions.cachedLazyGetter(JavaModelFunctions::asCustomStatement, typeElement, session);
    this.typeArgumentsGetter = Actions.cachedLazyGetter(JavaModelFunctions::getTypeArguments, declaredType, typeContext, session);
    this.typeArgumentMappingsGetter = Actions.cachedLazyGetter(TypeReferenceFunctions::getTypeArgumentMapping, this);
    this.typeArgumentsDeclarationGetter = Actions.cachedLazyGetter(TypeReferenceFunctions::getTypeArgumentsDeclaration, this);
  }

  @Override
  public StatementType statementType() {
    return StatementTypes.CustomReference;
  }

  @Override
  public CustomType targetType() {
    return targetTypeGetter.get();
  }

  @Override
  public Class<?> targetClass() {
    return TypeFunctions.getClass(targetType().canonicalName()).orElseThrow();
  }

  @Override
  public List<NotPrimitiveReference> typeArguments() {
    return typeArgumentsGetter.get();
  }

  @Override
  public Map<String, NotPrimitiveReference> typeArgumentMapping() {
    return typeArgumentMappingsGetter.get();
  }

  @Override
  public String typeArgumentsDeclaration() {
    return typeArgumentsDeclarationGetter.get();
  }

  @Override
  public String typeArgumentsDeclaration(Function<String, String> simpleNameMapper) {
    return TypeReferenceFunctions.getTypeArgumentsDeclaration(this, simpleNameMapper);
  }
}