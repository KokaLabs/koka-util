package koka.util.collect;

import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class Collections3Test {
  @Test
  public void nullToEmpty_Null_Empty() {
    List<Object> result = Collections3.nullToEmpty(null);
    assertThat(result.isEmpty()).isTrue();
  }

  @Test
  public void nullToEmpty_NotNull_SameInstance() {
    List<Object> notNullInput = new ArrayList<Object>();
    List<Object> result = Collections3.nullToEmpty(notNullInput);
    assertThat(result).isSameAs(notNullInput);
  }
}
