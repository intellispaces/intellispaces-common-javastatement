package intellispaces.javastatements.instance;

import intellispaces.actions.Actions;
import intellispaces.actions.getter.Getter;
import intellispaces.javastatements.StatementType;
import intellispaces.javastatements.StatementTypes;
import intellispaces.javastatements.customtype.AnnotationFunctions;
import intellispaces.javastatements.customtype.AnnotationType;
import intellispaces.javastatements.customtype.Annotations;
import intellispaces.javastatements.session.Session;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Adapter of {@link AnnotationMirror} to {@link AnnotationInstance}.
 */
class AnnotationInstanceBasedOnAnnotationMirror implements AnnotationInstance {
  private final Getter<AnnotationType> annotationStatementGetter;
  private final Getter<Map<String, AnnotationElement>> elementsGetter;

  AnnotationInstanceBasedOnAnnotationMirror(AnnotationMirror annotationMirror, Session session) {
    this.annotationStatementGetter = Actions.cachedLazyGetter(annMirror ->
        Annotations.of((TypeElement) annMirror.getAnnotationType().asElement(), session), annotationMirror);
    this.elementsGetter = Actions.cachedLazyGetter(annMirror ->
        AnnotationFunctions.getAnnotationElements(annMirror, session).stream()
            .collect(Collectors.toMap(AnnotationElement::name, Function.identity())), annotationMirror
    );
  }

  public StatementType statementType() {
    return StatementTypes.AnnotationInstance;
  }

  @Override
  public AnnotationType annotationStatement() {
    return annotationStatementGetter.get();
  }

  @Override
  public Collection<AnnotationElement> elements() {
    return elementsGetter.get().values();
  }

  @Override
  public Optional<Instance> elementValue(String elementName) {
    return Optional.ofNullable(elementsGetter.get().get(elementName)).map(AnnotationElement::value);
  }

  @Override
  public <A extends Annotation> A asAnnotationOf(Class<A> annotationClass) {
    return AnnotationFunctions.asAnnotation(this, annotationClass);
  }
}