package tech.intellispaces.java.reflection.samples;

import tech.intellispaces.java.reflection.support.TesteeType;

public interface ClassImplementedInterfaceWithGenericDefaultMethod3 {

  @TesteeType
  class TesteeClass implements Interface2 {
  }

  interface Interface2 extends Interface1 {
    @Override
    default <T, R> R method(T param1, Class<? extends T> param2) {
      return null;
    }
  }

  interface Interface1 {
    <T, R> R method(T param1, Class<? extends T> param2);
  }
}