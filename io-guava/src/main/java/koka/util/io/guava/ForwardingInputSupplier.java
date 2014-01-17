package koka.util.io.guava;

import java.io.IOException;

import com.google.common.io.InputSupplier;

/**
 * @since 0.7
 */
public abstract class ForwardingInputSupplier<T> implements InputSupplier<T> {
  protected abstract InputSupplier<T> delegate();

  @Override
  public T getInput() throws IOException {
    return delegate().getInput();
  }
}
