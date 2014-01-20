package koka.util.io.guava;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.io.ByteSource;

/**
 * A mechanism for converting an OutputStream to an InputStream.
 * <p>
 * Bytes written in {@link #passThrough(OutputStream)} are accessible via
 * {@link #openStream()}. <br>
 * Note that the writing process occurs on a separate thread.
 * 
 * @since 0.4
 */
public abstract class PipedByteSource extends ByteSource {
  @VisibleForTesting
  static final int MAX_BUFFER_SIZE = Piped.MAX_BUFFER_SIZE;

  private final Piped actual;

  public PipedByteSource() {
    actual = new Piped(asProducedData(), useSubmitOf(this));
  }

  private static Threading useSubmitOf(final PipedByteSource within) {
    return new Threading() {
      @Override
      <V> Future<V> submit(Callable<V> task) {
        return within.submit(task);
      }
    };
  }

  private PipedByteSource(ExecutorService using) {
    actual = new Piped(asProducedData(), using);
  }

  private ProducedData asProducedData() {
    return new ProducedData() {
      @Override
      public final void sendTo(OutputStream sink) throws IOException {
        passThrough(sink);
      }
    };
  }

  /**
   * Anything written here can be read back via {@link #getInput()}.
   */
  protected abstract void passThrough(OutputStream to) throws IOException;

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
  public final PipedByteSource using(final ExecutorService forWritingProcess) {
    final PipedByteSource alreadyImpl = this;
    return new PipedByteSource(forWritingProcess) {
      @Override
      protected void passThrough(OutputStream to) throws IOException {
        alreadyImpl.passThrough(to);
      }
    };
  }

  @Override
  public final InputStream openStream() throws IOException {
    return actual.getInput();
  }
}
