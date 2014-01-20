package koka.util.io.guava;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import koka.util.io.OutToInStream;

import com.google.common.io.ByteSink;

public class InMemoryBytes extends ByteSink {
  private final OutToInStream outToIn = new OutToInStream();

  @Override
  public OutputStream openStream() throws IOException {
    return outToIn;
  }

  public InputStream asByteSource() {
    return outToIn.toInputStream();
  }
}
