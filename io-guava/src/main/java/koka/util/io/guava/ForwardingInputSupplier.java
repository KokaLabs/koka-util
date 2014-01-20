package koka.util.io.guava;

import java.io.IOException;

import com.google.common.io.InputSupplier;

/**
 * @deprecated To be removed once Guava removes {@link InputSupplier} in version
 *             18.0
 * @since 0.1
 */
@Deprecated
public abstract class ForwardingInputSupplier<T> implements InputSupplier<T> {
  protected abstract InputSupplier<T> delegate();

  @Override
  public T getInput() throws IOException {
    return delegate().getInput();
  }
}
