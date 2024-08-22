package intellispaces.javastatements.instance;

import intellispaces.javastatements.customtype.EnumType;

import java.util.Optional;

/**
 * Enum instance.
 */
public interface EnumInstance extends Instance {

  @Override
  default Optional<EnumInstance> asEnum() {
    return Optional.of(this);
  }

  /**
   * Enum type statement.
   */
  EnumType type();

  /**
   * Enum name.
   */
  String name();
}