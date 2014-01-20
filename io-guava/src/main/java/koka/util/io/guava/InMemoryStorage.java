package koka.util.io.guava;

import java.io.InputStream;
import java.io.OutputStream;

import koka.util.io.OutToInStream;

import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;
import com.google.common.io.InputSupplier;

/**
 * Stores data in memory.
 * <p>
 * Note: multiple writes will append. Create a new instance if intending to
 * clear the buffer.
 * 
 * @deprecated Replaced by {@link InMemoryBytes} using {@link ByteSource} and
 *             {@link ByteSink}. To be removed once Guava removes
 *             {@link InputSupplier} in version 18.0
 */
@Deprecated
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
