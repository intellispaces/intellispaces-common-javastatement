package tech.intellispaces.framework.javastatements.statement.reference;

import tech.intellispaces.framework.commons.action.ActionBuilders;
import tech.intellispaces.framework.commons.action.Getter;
import tech.intellispaces.framework.javastatements.statement.StatementType;
import tech.intellispaces.framework.javastatements.statement.StatementTypes;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;

import java.util.List;
import java.util.function.Function;

class CustomTypeReferenceImpl extends AbstractTypeReference implements CustomTypeReference {
  private final CustomType targetType;
  private final List<NonPrimitiveTypeReference> typeArguments;
  private final Getter<String> typeArgumentsDeclarationGetter;

  CustomTypeReferenceImpl(CustomType targetType, List<NonPrimitiveTypeReference> typeArguments) {
    super();
    this.targetType = targetType;
    this.typeArguments = typeArguments;
    this.typeArgumentsDeclarationGetter = ActionBuilders.cachedLazyGetter(TypeReferenceFunctions::getTypeArgumentsDeclaration, this);
  }

  @Override
  public StatementType statementType() {
    return StatementTypes.CustomTypeReference;
  }

  @Override
  public CustomType targetType() {
    return targetType;
  }

  @Override
  public List<NonPrimitiveTypeReference> typeArguments() {
    return typeArguments;
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
