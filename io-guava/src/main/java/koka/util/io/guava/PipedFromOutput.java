package koka.util.io.guava;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.io.InputSupplier;

/**
 * A mechanism for converting an OutputStream to an InputStream.
 * <p>
 * Bytes written in {@link #write(OutputStream)} are accessible via
 * {@link #getInput()}. <br>
 * Note that the writing process occurs on a separate thread.
 * 
 * @deprecated see {@link PipedByteSource}
 * @since 0.1
 */
@Deprecated
public abstract class PipedFromOutput implements InputSupplier<InputStream> {
  @VisibleForTesting
  static final int MAX_BUFFER_SIZE = Piped.MAX_BUFFER_SIZE;

  private final Piped actual;

  public PipedFromOutput() {
    actual = new Piped(asProducedData(), useSubmitOf(this));
  }

  private static Threading useSubmitOf(final PipedFromOutput within) {
    return new Threading() {
      @Override
      <V> Future<V> submit(Callable<V> task) {
        return within.submit(task);
      }
    };
  }

  private PipedFromOutput(ExecutorService using) {
    actual = new Piped(asProducedData(), using);
  }

  private ProducedData asProducedData() {
    return new ProducedData() {
      @Override
      public final void sendTo(OutputStream sink) throws IOException {
        write(sink);
      }
    };
  }

  /**
   * Anything written here can be read back via {@link #getInput()}.
   */
  protected abstract void write(OutputStream to) throws IOException;

  /**
   * Executes writing process on separate thread.
   */
  protected <V> Future<V> submit(Callable<V> onSeparateThread) {
    return Threading.freshExecutor().submit(onSeparateThread);
  }

  /**
   * Returned instance will schedule any writing process with this instead.
   */
  @Beta
  public final PipedFromOutput using(final ExecutorService forWritingProcess) {
    final PipedFromOutput alreadyImpl = this;
    return new PipedFromOutput(forWritingProcess) {
      @Override
      protected void write(OutputStream to) throws IOException {
        alreadyImpl.write(to);
      }
    };
  }

  @Override
  public final InputStream getInput() throws IOException {
    return actual.getInput();
  }
}
