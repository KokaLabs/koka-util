package koka.util.io.guava;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Notify {@link PipedInputStream} as soon as possible to avoid 1 second wait. <br>
 * See: http://stackoverflow.com/questions/2843555/
 */
class EagerAlertingPipedOutputStream extends PipedOutputStream {
  EagerAlertingPipedOutputStream(PipedInputStream in) throws IOException {
    super(in);
  }

  @Override
  public void write(int b) throws IOException {
    super.write(b);
    flush();
  }

  @Override
  public void write(byte[] b) throws IOException {
    super.write(b);
    flush();
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    super.write(b, off, len);
    flush();
  }
}
