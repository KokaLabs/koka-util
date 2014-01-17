package koka.util.collect;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

public final class Collections3 {
  private Collections3() {}

  public static <E> List<E> nullToEmpty(@Nullable List<E> maybe) {
    return (maybe != null) ? maybe : Collections.<E> emptyList();
  }
}
