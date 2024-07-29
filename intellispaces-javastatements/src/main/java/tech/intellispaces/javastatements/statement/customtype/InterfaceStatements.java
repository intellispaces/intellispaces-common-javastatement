package tech.intellispaces.javastatements.statement.customtype;

import tech.intellispaces.javastatements.context.TypeContext;
import tech.intellispaces.javastatements.context.TypeContexts;
import tech.intellispaces.javastatements.session.Session;
import tech.intellispaces.javastatements.statement.common.TypeElementFunctions;
import tech.intellispaces.javastatements.statement.reference.NotPrimitiveTypeReference;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.util.Map;

public interface InterfaceStatements {

  static InterfaceType of(Class<?> aClass) {
    return new InterfaceTypeBasedOnLangClass(aClass);
  }

  static InterfaceType of(TypeElement typeElement, Session session) {
    return of(typeElement, TypeContexts.empty(), session);
  }

  static InterfaceType of(TypeElement typeElement, TypeContext typeContext, Session session) {
    return TypeElementFunctions.asCustomStatement(
        typeElement,
        ElementKind.INTERFACE,
        InterfaceStatements::create,
        typeContext,
        session
    );
  }

  static InterfaceType effectiveOf(
      InterfaceType actualType, Map<String, NotPrimitiveTypeReference> typeMapping
  ) {
    return new EffectiveInterfaceType(actualType, typeMapping);
  }

  private static InterfaceType create(
      TypeElement typeElement, TypeContext typeContext, Session session
  ) {
    return new InterfaceStatementBasedOnTypeElement(typeElement, typeContext, session);
  }
}
