package koka.util.io;

import java.io.IOException;
import java.io.OutputStream;

public final class OutputStreams {
  private OutputStreams() {}

  /**
   * @since 0.7
   */
  public static OutputStream nonCloseable(final OutputStream delegate) {
    return new ForwardingOutputStream() {
      @Override
      public void close() throws IOException {
        throw new IOException("Attempted to close this stream");
      }

      @Override
      OutputStream delegate() {
        return delegate;
      }
    };
  }

  /**
   * @since 0.7
   */
  public static OutputStream closeIgnoring(final OutputStream delegate) {
    return new ForwardingOutputStream() {
      @Override
      public void close() {}

      @Override
      OutputStream delegate() {
        return delegate;
      }
    };
  }
}
