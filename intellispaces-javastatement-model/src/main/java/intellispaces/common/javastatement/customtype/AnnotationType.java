package intellispaces.common.javastatement.customtype;

import intellispaces.common.javastatement.reference.NamedReference;

import java.util.List;
import java.util.Optional;

/**
 * The annotation type.
 */
public interface AnnotationType extends CustomType {

  @Override
  default List<NamedReference> typeParameters() {
    return List.of();
  }

  @Override
  default Optional<AnnotationType> asAnnotation() {
    return Optional.of(this);
  }

  default CustomType asCustomType() {
    return this;
  }
}