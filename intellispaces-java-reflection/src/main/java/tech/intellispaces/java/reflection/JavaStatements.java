package tech.intellispaces.java.reflection;

import tech.intellispaces.java.reflection.common.JavaModelFunctions;
import tech.intellispaces.java.reflection.customtype.AnnotationType;
import tech.intellispaces.java.reflection.customtype.Annotations;
import tech.intellispaces.java.reflection.customtype.ClassType;
import tech.intellispaces.java.reflection.customtype.Classes;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.customtype.CustomTypes;
import tech.intellispaces.java.reflection.customtype.EnumType;
import tech.intellispaces.java.reflection.customtype.Enums;
import tech.intellispaces.java.reflection.customtype.InterfaceType;
import tech.intellispaces.java.reflection.customtype.Interfaces;
import tech.intellispaces.java.reflection.customtype.RecordType;
import tech.intellispaces.java.reflection.customtype.Records;
import tech.intellispaces.java.reflection.reference.CustomTypeReference;
import tech.intellispaces.java.reflection.reference.CustomTypeReferences;
import tech.intellispaces.java.reflection.session.Session;
import tech.intellispaces.java.reflection.session.Sessions;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

/**
 * Java statements facade.
 */
public interface JavaStatements {

  static Statement statement(Element element) {
    return JavaModelFunctions.of(element);
  }

  static CustomType customTypeStatement(Class<?> aClass) {
    return CustomTypes.of(aClass);
  }

  static CustomType customTypeStatement(TypeElement typeElement) {
    return CustomTypes.of(typeElement, Sessions.get());
  }

  static CustomType customTypeStatement(TypeElement typeElement, Session session) {
    return CustomTypes.of(typeElement, session);
  }

  static ClassType classStatement(TypeElement typeElement) {
    return Classes.of(typeElement, Sessions.get());
  }

  static ClassType classStatement(TypeElement typeElement, Session session) {
    return Classes.of(typeElement, session);
  }

  static InterfaceType interfaceStatement(TypeElement typeElement) {
    return Interfaces.of(typeElement, Sessions.get());
  }

  static InterfaceType interfaceStatement(TypeElement typeElement, Session session) {
    return Interfaces.of(typeElement, session);
  }

  static RecordType recordStatement(TypeElement typeElement) {
    return Records.of(typeElement, Sessions.get());
  }

  static RecordType recordStatement(TypeElement typeElement, Session session) {
    return Records.of(typeElement, session);
  }

  static EnumType enumStatement(TypeElement typeElement) {
    return Enums.of(typeElement, Sessions.get());
  }

  static EnumType enumStatement(TypeElement typeElement, Session session) {
    return Enums.of(typeElement, session);
  }

  static AnnotationType annotationStatement(TypeElement typeElement) {
    return Annotations.of(typeElement, Sessions.get());
  }

  static AnnotationType annotationStatement(TypeElement typeElement, Session session) {
    return Annotations.of(typeElement, session);
  }

  static CustomTypeReference customTypeReference(Class<?> aClass) {
    return CustomTypeReferences.get(aClass);
  }

  static CustomTypeReference customTypeReference(TypeElement typeElement) {
    return CustomTypeReferences.get(typeElement, Sessions.get());
  }

  static CustomTypeReference customTypeReference(TypeElement typeElement, Session session) {
    return CustomTypeReferences.get(typeElement, session);
  }

  static CustomTypeReference customTypeReference(DeclaredType declaredType) {
    return CustomTypeReferences.get(declaredType, Sessions.get());
  }

  static CustomTypeReference customTypeReference(DeclaredType declaredType, Session session) {
    return CustomTypeReferences.get(declaredType, session);
  }
}