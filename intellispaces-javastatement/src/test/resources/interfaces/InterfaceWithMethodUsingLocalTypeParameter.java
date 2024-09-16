package intellispaces.common.javastatement.samples;

import intellispaces.common.javastatement.support.TesteeType;

import java.util.List;

@TesteeType
public interface InterfaceWithMethodUsingLocalTypeParameter {

  <T> List<T> methodUsingLocalTypeParameter(T arg);
}