package tech.intellispacesframework.javastatements.samples;

import tech.intellispacesframework.javastatements.support.TesteeType;

import java.util.Collection;

@TesteeType
public record RecordWithMethodUsingWildcardThatSuperOtherClass() {

  public void methodUsingWildcardThatSuperOtherClass(Collection<? super Number[]> arg) {
  }
}