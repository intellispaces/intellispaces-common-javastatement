package tech.intellispaces.java.reflection.samples;

import tech.intellispaces.java.reflection.support.TesteeType;

@TesteeType
public record RecordWithSimpleMethod() {

  public void simpleMethod() {
    return;
  }
}