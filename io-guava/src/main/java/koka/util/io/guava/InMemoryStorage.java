package koka.util.io.guava;

import java.io.InputStream;
import java.io.OutputStream;

import koka.util.io.OutToInStream;

/**
 * Stores data in memory.
 * <p>
 * Note: multiple writes will append. Create a new instance if intending to
 * clear the buffer.
 */
public final class InMemoryStorage implements Storage {
  private final OutToInStream outToIn = new OutToInStream();

  @Override
  public InputStream getInput() {
    return outToIn.toInputStream();
  }

  @Override
  public OutputStream getOutput() {
    return outToIn;
  }
}
