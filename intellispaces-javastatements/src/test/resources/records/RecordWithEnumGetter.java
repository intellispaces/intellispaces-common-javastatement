package tech.intellispaces.javastatements.samples;

import tech.intellispaces.javastatements.samples.TestEnum;
import tech.intellispaces.javastatements.support.TesteeType;

@TesteeType
public record RecordWithEnumGetter() {

  public TestEnum enumGetter() {
    return null;
  }
}