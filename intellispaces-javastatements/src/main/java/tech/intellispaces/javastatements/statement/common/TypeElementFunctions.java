package tech.intellispaces.javastatements.statement.common;

import tech.intellispaces.commons.function.TriFunction;
import tech.intellispaces.javastatements.context.ContextTypeParameter;
import tech.intellispaces.javastatements.context.TypeContext;
import tech.intellispaces.javastatements.context.TypeContextBlank;
import tech.intellispaces.javastatements.context.TypeContexts;
import tech.intellispaces.javastatements.exception.JavaStatementException;
import tech.intellispaces.javastatements.session.Session;
import tech.intellispaces.javastatements.statement.StatementTypes;
import tech.intellispaces.javastatements.statement.instance.AnnotationInstance;
import tech.intellispaces.javastatements.statement.method.MethodStatement;
import tech.intellispaces.javastatements.statement.method.MethodStatements;
import tech.intellispaces.javastatements.statement.reference.ArrayTypeReferences;
import tech.intellispaces.javastatements.statement.reference.CustomTypeReference;
import tech.intellispaces.javastatements.statement.reference.CustomTypeReferences;
import tech.intellispaces.javastatements.statement.reference.NamedReference;
import tech.intellispaces.javastatements.statement.reference.NamedTypes;
import tech.intellispaces.javastatements.statement.reference.NotPrimitiveTypeReference;
import tech.intellispaces.javastatements.statement.reference.PrimitiveReferences;
import tech.intellispaces.javastatements.statement.reference.TypeReference;
import tech.intellispaces.javastatements.statement.reference.TypeReferenceBound;
import tech.intellispaces.javastatements.statement.reference.WildcardTypes;
import tech.intellispaces.javastatements.statement.customtype.AnnotationFunctions;
import tech.intellispaces.javastatements.statement.customtype.AnnotationStatements;
import tech.intellispaces.javastatements.statement.customtype.ClassStatements;
import tech.intellispaces.javastatements.statement.customtype.CustomType;
import tech.intellispaces.javastatements.statement.customtype.EnumStatements;
import tech.intellispaces.javastatements.statement.customtype.InterfaceStatements;
import tech.intellispaces.javastatements.statement.customtype.RecordStatements;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Parameterizable;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Functions related to javax.lang.model elements and types.
 */
public interface TypeElementFunctions {

  static String getCanonicalName(TypeElement typeElement) {
    return typeElement.getQualifiedName().toString();
  }

  static String getClassName(TypeElement typeElement) {
    if (typeElement.getEnclosingElement().getKind().equals(ElementKind.PACKAGE)) {
      return typeElement.getQualifiedName().toString();
    }

    List<String> typenames = new ArrayList<>();
    Element curElement = typeElement;
    while (true) {
      Element enclosingElement = curElement.getEnclosingElement();
      if (!enclosingElement.getKind().isClass() && !enclosingElement.getKind().isInterface()) {
        break;
      }
      typenames.add(curElement.getSimpleName().toString());
      curElement = curElement.getEnclosingElement();
    }

    var sb = new StringBuilder();
    sb.append(((TypeElement) curElement).getQualifiedName().toString());
    for (int i = typenames.size() - 1; i >= 0; i--) {
      sb.append("$");
      sb.append(typenames.get(i));
    }
    return sb.toString();
  }

  static String getSimpleName(TypeElement typeElement) {
    return typeElement.getSimpleName().toString();
  }

  static String getPackageName(TypeElement typeElement) {
    TypeElement element = typeElement;
    while (element.getEnclosingElement().getKind() != ElementKind.PACKAGE) {
      element = (TypeElement) element.getEnclosingElement();
    }
    String canonicalName = getCanonicalName(element);
    int dot = canonicalName.lastIndexOf('.');
    return (dot != -1) ? canonicalName.substring(0, dot).intern() : "";
  }

  static List<CustomTypeReference> getParentTypes(TypeElement typeElement, Session session) {
    return getParentTypes(typeElement, TypeContexts.empty(), session);
  }

  static List<CustomTypeReference> getParentTypes(
      TypeElement typeElement, TypeContext typeContext, Session session
  ) {
    List<TypeMirror> parentTypeMirrors = new ArrayList<>(1 + typeElement.getInterfaces().size());
    TypeMirror superClass = typeElement.getSuperclass();
    if (superClass != null && TypeKind.NONE != superClass.getKind()) {
      parentTypeMirrors.add(superClass);
    }
    parentTypeMirrors.addAll(typeElement.getInterfaces());

    List<NamedReference> thisTypeParams = getTypeParameters(typeElement, typeContext, session);
    TypeContext newTypeContext = TypeContexts.build()
        .parentContext(typeContext)
        .addTypeParams(thisTypeParams)
        .get();
    return parentTypeMirrors.stream()
        .map(parent -> CustomTypeReferences.of((DeclaredType) parent, newTypeContext, session))
        .filter(ref -> !Object.class.getName().equals(ref.customType().canonicalName()))
        .toList();
  }

  static List<NamedReference> getTypeParameters(TypeElement typeElement, Session session) {
    return getTypeParameters(typeElement, TypeContexts.empty(), session);
  }

  static List<NamedReference> getTypeParameters(
      Parameterizable parameterizable, TypeContext typeContext, Session session
  ) {
    TypeContextBlank nameContextBlank = TypeContexts.blank();
    nameContextBlank.setParentContext(typeContext);
    List<NamedReference> typeParams = parameterizable.getTypeParameters().stream()
        .map(param -> getTypeParameter(param, nameContextBlank, session))
        .toList();
    typeParams.forEach(typeParam -> nameContextBlank.addTypeParam(typeParam.name(), typeParam));
    return typeParams;
  }

  private static NamedReference getTypeParameter(
      TypeParameterElement typeParameter, TypeContext typeContext, Session session
  ) {
    return NamedTypes.of(typeParameter, typeContext, session);
  }

  static List<NotPrimitiveTypeReference> getTypeArguments(
      DeclaredType declaredType, TypeContext typeContext, Session session
  ) {
    List<NotPrimitiveTypeReference> typeArguments = new ArrayList<>();
    for (TypeMirror typeArgumentsMirror : declaredType.getTypeArguments()) {
      TypeReference typeReferenceArgumentsReference = getTypeReference(typeArgumentsMirror, typeContext, session);
      if (typeReferenceArgumentsReference instanceof NotPrimitiveTypeReference) {
        typeArguments.add((NotPrimitiveTypeReference) typeReferenceArgumentsReference);
      } else {
        throw JavaStatementException.withMessage("Invalid type kind");
      }
    }
    return typeArguments;
  }

  static List<AnnotationInstance> getAnnotations(Element typeElement, Session session) {
    return AnnotationFunctions.getAnnotations(typeElement.getAnnotationMirrors(), session);
  }

  static List<MethodStatement> getConstructors(
      TypeElement typeElement, CustomType methodOwner, TypeContext typeContext, Session session
  ) {
    return typeElement.getEnclosedElements().stream()
        .filter(element -> element.getKind() == ElementKind.CONSTRUCTOR)
        .map(element -> (ExecutableElement) element)
        .map(element -> MethodStatements.of(element, methodOwner, typeContext, session))
        .toList();
  }

  static List<MethodStatement> getDeclaredMethods(
      TypeElement typeElement, CustomType methodOwner, TypeContext typeContext, Session session
  ) {
    return typeElement.getEnclosedElements().stream()
        .filter(element -> element.getKind() == ElementKind.METHOD)
        .map(element -> (ExecutableElement) element)
        .map(element -> MethodStatements.of(element, methodOwner, typeContext, session))
        .toList();
  }

  static CustomTypeReference getTypeReference(DeclaredType declaredType, Session session) {
    return getTypeReference(declaredType, TypeContexts.empty(), session);
  }

  static CustomTypeReference getTypeReference(DeclaredType declaredType, TypeContext typeContext, Session session) {
    return CustomTypeReferences.of(declaredType, typeContext, session);
  }

  static TypeReference getTypeReference(TypeVariable typeVariable, TypeContext typeContext) {
    String typeParamName = typeVariable.asElement().getSimpleName().toString();
    Optional<ContextTypeParameter> namedTypeReference = typeContext.get(typeParamName);
    if (namedTypeReference.isPresent()) {
      return namedTypeReference.get().namedType();
    }
    throw JavaStatementException.withMessage("Unknown type: {}", typeParamName);
  }

  static TypeReference getTypeReference(TypeMirror typeMirror, Session session) {
    return getTypeReference(typeMirror, TypeContexts.empty(), session);
  }

  static TypeReference getTypeReference(TypeMirror typeMirror, TypeContext typeContext, Session session) {
    return switch (typeMirror.getKind()) {
      case BOOLEAN -> PrimitiveReferences.Boolean;
      case BYTE -> PrimitiveReferences.Byte;
      case SHORT -> PrimitiveReferences.Short;
      case INT -> PrimitiveReferences.Integer;
      case LONG -> PrimitiveReferences.Long;
      case CHAR -> PrimitiveReferences.Char;
      case FLOAT -> PrimitiveReferences.Float;
      case DOUBLE -> PrimitiveReferences.Double;
      case DECLARED -> getTypeReference((DeclaredType) typeMirror, typeContext, session);
      case TYPEVAR -> getTypeReference((TypeVariable) typeMirror, typeContext);
      case WILDCARD -> WildcardTypes.of((WildcardType) typeMirror, typeContext, session);
      case ARRAY -> ArrayTypeReferences.of((ArrayType) typeMirror, typeContext, session);
      default -> throw JavaStatementException.withMessage("Unsupported type mirror kind {}", typeMirror.getKind());
    };
  }

  static CustomType getCustomTypeStatement(DeclaredType declaredType, Session session) {
    return getTypeReference(declaredType, session).customType();
  }

  static boolean isCustomType(TypeElement typeElement) {
    return typeElement.getKind() == ElementKind.CLASS ||
        typeElement.getKind() == ElementKind.INTERFACE ||
        typeElement.getKind() == ElementKind.RECORD ||
        typeElement.getKind() == ElementKind.ENUM ||
        typeElement.getKind() == ElementKind.ANNOTATION_TYPE;
  }

  static Class<?> getClass(TypeReference typeReference) {
    if (StatementTypes.PrimitiveReference.equals(typeReference.statementType())) {
      return typeReference.asPrimitive().orElseThrow().wrapperClass();
    } else if (StatementTypes.CustomReference.equals(typeReference.statementType())) {
      return getClass(typeReference.asCustomType().orElseThrow().customType().canonicalName());
    } else {
      throw JavaStatementException.withMessage("Unsupported type {}", typeReference.statementType().typename());
    }
  }

  static java.lang.Class<?> getClass(String className) {
    final Class<?> aClass;
    try {
      aClass = java.lang.Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException("Class " + className + " is not found", e);
    }
    return aClass;
  }

  static List<TypeReferenceBound> getExtendedBounds(
      TypeParameterElement typeParameter, TypeContext typeContext, Session session
  ) {
    List<TypeReferenceBound> bounds = new ArrayList<>(typeParameter.getBounds().size());
    for (TypeMirror bound : typeParameter.getBounds()) {
      getBound(bound, typeContext, session).ifPresent(bounds::add);
    }
    return bounds;
  }

  static Optional<TypeReferenceBound> getExtendedBound(
      WildcardType wildcardType, TypeContext typeContext, Session session
  ) {
    if (wildcardType.getExtendsBound() == null) {
      return Optional.empty();
    }
    return getBound(wildcardType.getExtendsBound(), typeContext, session);
  }

  static Optional<TypeReferenceBound> getSuperBound(
      WildcardType wildcardType, TypeContext typeContext, Session session
  ) {
    if (wildcardType.getSuperBound() == null) {
      return Optional.empty();
    }
    return getBound(wildcardType.getSuperBound(), typeContext, session);
  }

  private static Optional<TypeReferenceBound> getBound(
      TypeMirror bound, TypeContext typeContext, Session session
  ) {
    TypeReferenceBound boundTypeReference = null;
    if (bound.getKind() == TypeKind.TYPEVAR) {
      TypeVariable typeVariable = (TypeVariable) bound;
      String typeParamName = typeVariable.asElement().getSimpleName().toString();
      NamedReference namedReference = typeContext.get(typeParamName)
          .map(ContextTypeParameter::namedType)
          .orElse(null);
      if (namedReference == null) {
        throw JavaStatementException.withMessage("Type variable named {} is not found", typeParamName);
      }
      boundTypeReference = namedReference;
    } else if (bound.getKind() == TypeKind.DECLARED) {
      DeclaredType declaredType = (DeclaredType) bound;
      CustomTypeReference customTypeReference = CustomTypeReferences.of(declaredType, typeContext, session);
      if (!Object.class.getName().equals(customTypeReference.customType().canonicalName())) {
        boundTypeReference = customTypeReference;
      }
    } else if (TypeKind.ARRAY == bound.getKind()) {
      boundTypeReference = ArrayTypeReferences.of((ArrayType) bound, typeContext, session);
    } else {
      throw JavaStatementException.withMessage("Unsupported type parameter kind {}", bound.getKind());
    }
    return Optional.ofNullable(boundTypeReference);
  }

  static CustomType asCustomStatement(TypeElement typeElement, Session session) {
    return asCustomStatement(typeElement, TypeContexts.empty(), session);
  }

  static CustomType asCustomStatement(TypeElement typeElement, TypeContext typeContext, Session session) {
    if (typeElement.getKind() == ElementKind.CLASS) {
      return ClassStatements.of(typeElement, typeContext, session);
    } else if (typeElement.getKind() == ElementKind.INTERFACE) {
      return InterfaceStatements.of(typeElement, typeContext, session);
    } else if (typeElement.getKind() == ElementKind.RECORD) {
      return RecordStatements.of(typeElement, typeContext, session);
    } else if (typeElement.getKind() == ElementKind.ENUM) {
      return EnumStatements.of(typeElement, typeContext, session);
    } else if (typeElement.getKind() == ElementKind.ANNOTATION_TYPE) {
      return AnnotationStatements.of(typeElement, typeContext, session);
    } else {
      throw JavaStatementException.withMessage("Type element of kind can't be represented as custom type statement {}",
          typeElement.getKind());
    }
  }

  @SuppressWarnings("unchecked")
  static <T extends CustomType> T asCustomStatement(
      TypeElement typeElement,
      ElementKind expectedElementKind,
      TriFunction<TypeElement, TypeContext, Session, T> factory,
      TypeContext typeContext,
      Session session
  ) {
    if (typeElement.getKind() != expectedElementKind) {
      throw JavaStatementException.withMessage("Expected type element of the kind {} but actual is {}",
          expectedElementKind, typeElement.getKind());
    }

    String typeName = getCanonicalName(typeElement);
    T statement = (T) session.getType(typeName);
    if (statement == null) {
      statement = factory.apply(typeElement, typeContext, session);
      session.putType(typeName, statement);
    }
    return statement;
  }
}