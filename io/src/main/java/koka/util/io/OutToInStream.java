package koka.util.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Data written to this {@link java.io.OutputStream} is stored in memory. It can
 * then be read as an {@link java.io.InputStream} without a byte array copy.
 */
public final class OutToInStream extends ByteArrayOutputStream {
  /**
   * A view of the bytes that have been written thus far.
   */
  public InputStream toInputStream() {
    return new ByteArrayInputStream(buf, 0, count);
  }
}
