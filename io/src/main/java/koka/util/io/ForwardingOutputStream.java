package koka.util.io;

import java.io.IOException;
import java.io.OutputStream;

abstract class ForwardingOutputStream extends OutputStream {
  abstract OutputStream delegate();

  @Override
  public void write(int b) throws IOException {
    delegate().write(b);
  }

  @Override
  public void write(byte[] b) throws IOException {
    delegate().write(b);
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    delegate().write(b, off, len);
  }

  @Override
  public void flush() throws IOException {
    delegate().flush();
  }

  @Override
  public void close() throws IOException {
    delegate().close();
  }
}
