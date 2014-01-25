package koka.util.io.guava;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import koka.util.io.OutputStreams;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Throwables;
import com.google.common.io.ByteSource;
import com.google.common.io.Closer;

/**
 * A mechanism for converting an OutputStream to an InputStream.
 * <p>
 * Allows reading {@link ProducedData} via {@link #openStream()}. <br>
 * Note that the produced data is performed on a separate thread.
 * 
 * @since 0.1
 */
public class Piped extends ByteSource {
  @VisibleForTesting
  static final int MAX_BUFFER_SIZE = 0x1000;

  private static final int EOF = -1;

  private final ProducedData generated;
  private final Threading forWriting;

  public Piped(ProducedData generated, ExecutorService forWriting) {
    this(generated, Threading.wrap(forWriting));
  }

  Piped(ProducedData generated, Threading forWriting) {
    this.generated = generated;
    this.forWriting = forWriting;
  }

  @Override
  public final InputStream openStream() throws IOException {
    PipedInputStream in = new PipedInputStream(MAX_BUFFER_SIZE);
    OutputStream out = new EagerAlertingPipedOutputStream(in);

    Callable<?> onSeparateThread = writingProcess(out);
    Future<?> writeIsDone = forWriting.submit(onSeparateThread);

    return waitBeforeFinalReturn(in, writeIsDone);
  }

  private static InputStream waitBeforeFinalReturn(
      final InputStream delegate,
      final Future<?> needsToFinishFirst) {
    return new InputStream() {
      @Override
      public int read(byte[] b, int off, int len) throws IOException {
        return handleEOF(delegate.read(b, off, len));
      }

      @Override
      public int read(byte[] b) throws IOException {
        return handleEOF(delegate.read(b));
      }

      @Override
      public int read() throws IOException {
        return handleEOF(delegate.read());
      }

      private int handleEOF(int maybe) throws IOException {
        if (maybe == EOF) {
          try {
            needsToFinishFirst.get();
          } catch (InterruptedException e) {
            throw new IOException(e);
          } catch (ExecutionException e) {
            Throwables.propagateIfPossible(e.getCause(), IOException.class);
          }
        }
        return maybe;
      }
    };
  }

  private Callable<Void> writingProcess(final OutputStream to) {
    return new Callable<Void>() {
      @Override
      public Void call() throws IOException {
        try {
          return performWrite();
        } catch (NoClassDefFoundError oldVersionOfGuava) {
          to.close();
          throw oldVersionOfGuava;
        }
      }

      private Void performWrite() throws IOException {
        Closer c = Closer.create();
        try {
          c.register(to);
          generated.sendTo(OutputStreams.nonCloseable(to));
          return null;
        } catch (Throwable e) {
          throw c.rethrow(e, IOException.class);
        } finally {
          c.close();
        }
      }
    };
  }
}
