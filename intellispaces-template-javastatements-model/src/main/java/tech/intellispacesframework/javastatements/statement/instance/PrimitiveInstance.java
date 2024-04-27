package tech.intellispacesframework.javastatements.statement.instance;

import tech.intellispacesframework.javastatements.statement.reference.PrimitiveTypeReference;

import java.util.Optional;

/**
 * Primitive instance.
 */
public interface PrimitiveInstance extends Instance {

  @Override
  default Optional<PrimitiveInstance> asPrimitive() {
    return Optional.of(this);
  }

  /**
   * Primitive type reference.
   */
  PrimitiveTypeReference type();

  /**
   * Primitive value.
   */
  Object value();
}