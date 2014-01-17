package koka.util.io.guava;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Timeout;

import com.google.common.base.Throwables;
import com.google.common.io.ByteStreams;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class PipedTest {
  @Rule
  public final Timeout sinceThreadsBeingUsed = new Timeout(10 * 1000);

  @Rule
  public final ExpectedException shouldFail = ExpectedException.none();

  private static Piped justThrows(final Throwable fail) {
    return new Piped(
        new ProducedData() {
          @Override
          public void sendTo(OutputStream sink) throws IOException {
            Throwables.propagateIfPossible(fail, IOException.class);
          }
        },
        anyExecutorService());
  }

  private static ExecutorService anyExecutorService() {
    ThreadFactory dontHang = new ThreadFactoryBuilder().setDaemon(true).build();
    return Executors.newCachedThreadPool(dontHang);
  }

  @Test
  public void writeContents_ReadBackSame() throws Exception {
    final byte[] written = { 1, 2, 3, 4, 5, 6, 7, 8 };
    ProducedData generated = new ProducedData() {
      @Override
      public void sendTo(OutputStream sink) throws IOException {
        ByteStreams.copy(new ByteArrayInputStream(written), sink);
      }
    };

    Piped toTest = new Piped(generated, anyExecutorService());

    byte[] read = ByteStreams.toByteArray(toTest);

    assertThat(read).isEqualTo(written);
  }

  @Test
  public void writeFailsWithIOException_ReadingShouldThrowIt()
      throws Exception {
    @SuppressWarnings("serial")
    class CustomIO extends IOException {}

    Piped toTest = justThrows(new CustomIO());

    shouldFail.expect(CustomIO.class);
    toTest.getInput().read();
  }

  @Test
  public void writeFailsWithError_ReadingShouldThrowIt()
      throws Exception {
    @SuppressWarnings("serial")
    class CustomError extends Error {}

    Piped toTest = justThrows(new CustomError());

    shouldFail.expect(CustomError.class);
    toTest.getInput().read();
  }

  @Test
  public void writeFailsWithRuntime_ReadingShouldThrowIt()
      throws Exception {
    @SuppressWarnings("serial")
    class CustomRuntime extends RuntimeException {}

    Piped toTest = justThrows(new CustomRuntime());

    shouldFail.expect(CustomRuntime.class);
    toTest.getInput().read();
  }

  @Test
  public void writeGetsInterrupted_ShouldWrapAndRethrowIt()
      throws Exception {
    ProducedData doNothing = new ProducedData() {
      @Override
      public void sendTo(OutputStream sink) {}
    };

    final Piped toTest = new Piped(doNothing, new Threading() {
      @Override
      <V> Future<V> submit(Callable<V> task) {
        try {
          task.call();

          @SuppressWarnings("unchecked")
          Future<V> f = mock(Future.class);
          given(f.get()).willThrow(new InterruptedException());
          return f;
        } catch (Exception e) {
          throw new AssertionError(e);
        }
      }
    });

    InputStream in = toTest.getInput();

    shouldFail.expect(IOException.class);
    shouldFail.expectCause(CoreMatchers.isA(InterruptedException.class));
    in.read();
  }

  @Test
  public void shouldBeAbleToProceedWithFullBuffer() throws Exception {
    ProducedData generated = new ProducedData() {
      @Override
      public void sendTo(OutputStream sink) throws IOException {
        for (int i = 0; i < (Piped.MAX_BUFFER_SIZE + 2); ++i) {
          sink.write(0);
        }
      }
    };

    Piped toTest = new Piped(generated, anyExecutorService());

    ByteStreams.toByteArray(toTest);
  }

  @Test
  public void attemptingToCloseStreamFails() throws Exception {
    ProducedData closesNonOwnedStream = new ProducedData() {
      @Override
      public void sendTo(OutputStream sink) throws IOException {
        sink.close();
      }
    };

    Piped toTest = new Piped(closesNonOwnedStream, anyExecutorService());
    InputStream in = toTest.getInput();

    shouldFail.expect(IOException.class);
    shouldFail.expectMessage("close");
    in.read();
  }
}
