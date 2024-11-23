package tech.intellispaces.java.reflection.samples;

import tech.intellispaces.java.reflection.support.TesteeType;

@TesteeType
public record RecordWithCharGetter() {

  public char charGetter() {
    return 'a';
  }
}