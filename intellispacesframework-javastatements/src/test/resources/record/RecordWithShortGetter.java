package tech.intellispacesframework.javastatements.samples;

import tech.intellispacesframework.javastatements.support.TesteeType;

@TesteeType
public record RecordWithShortGetter() {

  public short shortGetter() {
    return 0;
  }
}