package intellispaces.common.javastatement.reference;

import intellispaces.common.base.type.Primitive;
import intellispaces.common.javastatement.customtype.CustomType;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Primitive type reference.
 */
public interface PrimitiveReference extends TypeReference {

  String typename();

  Class<?> wrapperClass();

  Primitive asPrimitive();

  @Override
  default boolean isPrimitiveReference() {
    return true;
  }

  @Override
  default boolean isArrayReference() {
    return false;
  }

  @Override
  default boolean isCustomTypeReference() {
    return false;
  }

  @Override
  default boolean isNamedReference() {
    return false;
  }

  @Override
  default boolean isWildcard() {
    return false;
  }

  @Override
  default Optional<PrimitiveReference> asPrimitiveReference() {
    return Optional.of(this);
  }

  @Override
  default TypeReference effective(Map<String, NotPrimitiveReference> typeMapping) {
    return this;
  }

  @Override
  default Collection<CustomType> dependencies() {
    return List.of();
  }

  @Override
  default Collection<String> dependencyTypenames() {
    return List.of();
  }
}
