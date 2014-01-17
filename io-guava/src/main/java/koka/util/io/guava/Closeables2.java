package koka.util.io.guava;

import java.io.Closeable;
import java.io.IOException;

import javax.annotation.Nullable;

import com.google.common.io.Closeables;

public final class Closeables2 {
  private Closeables2() {}

  public static void close2(
      @Nullable Closeable first, @Nullable Closeable second, boolean swallow)
      throws IOException {
    if (swallow) {
      Closeables.close(first, true);
      Closeables.close(second, true);
    } else {
      boolean firstCloseThrew = true;
      try {
        Closeables.close(first, false);
        firstCloseThrew = false;
      } finally {
        Closeables.close(second, firstCloseThrew);
      }
    }
  }
}
