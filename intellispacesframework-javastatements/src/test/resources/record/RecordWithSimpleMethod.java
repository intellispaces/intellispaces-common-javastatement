package tech.intellispacesframework.javastatements.samples;

import tech.intellispacesframework.javastatements.support.TesteeType;

@TesteeType
public record RecordWithSimpleMethod() {

  public void simpleMethod() {
    return;
  }
}