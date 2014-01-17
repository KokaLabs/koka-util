package koka.util.io.guava;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Timeout;

import com.google.common.base.Throwables;
import com.google.common.io.ByteStreams;

public class PipedFromOutputTest {
  @Rule
  public final Timeout sinceThreadsBeingUsed = new Timeout(10 * 1000);

  @Rule
  public final ExpectedException shouldFail = ExpectedException.none();

  private static PipedFromOutput justThrows(final Throwable fail) {
    return new PipedFromOutput() {
      @Override
      protected void write(OutputStream to) throws IOException {
        Throwables.propagateIfPossible(fail, IOException.class);
      }
    };
  }

  @Test
  public void writeContents_ReadBackSame() throws Exception {
    final byte[] written = { 1, 2, 3, 4, 5, 6, 7, 8 };
    PipedFromOutput toTest = new PipedFromOutput() {
      @Override
      protected void write(OutputStream to) throws IOException {
        ByteStreams.copy(new ByteArrayInputStream(written), to);
      }
    };

    byte[] read = ByteStreams.toByteArray(toTest);

    assertThat(read).isEqualTo(written);
  }

  @Test
  public void writeFailsWithIOException_ReadingShouldThrowIt()
      throws Exception {
    @SuppressWarnings("serial")
    class CustomIO extends IOException {}

    PipedFromOutput toTest = justThrows(new CustomIO());

    shouldFail.expect(CustomIO.class);
    toTest.getInput().read();
  }

  @Test
  public void writeFailsWithError_ReadingShouldThrowIt()
      throws Exception {
    @SuppressWarnings("serial")
    class CustomError extends Error {}

    PipedFromOutput toTest = justThrows(new CustomError());

    shouldFail.expect(CustomError.class);
    toTest.getInput().read();
  }

  @Test
  public void writeFailsWithRuntime_ReadingShouldThrowIt()
      throws Exception {
    @SuppressWarnings("serial")
    class CustomRuntime extends RuntimeException {}

    PipedFromOutput toTest = justThrows(new CustomRuntime());

    shouldFail.expect(CustomRuntime.class);
    toTest.getInput().read();
  }

  @Test
  public void writeGetsInterrupted_ShouldWrapAndRethrowIt()
      throws Exception {
    final PipedFromOutput toTest = new PipedFromOutput() {
      @Override
      protected void write(OutputStream to) {}

      @Override
      protected <V> Future<V> submit(Callable<V> onSeparateThread) {
        try {
          onSeparateThread.call();

          @SuppressWarnings("unchecked")
          Future<V> f = mock(Future.class);
          given(f.get()).willThrow(new InterruptedException());
          return f;
        } catch (Exception e) {
          throw new AssertionError(e);
        }
      }
    };

    InputStream in = toTest.getInput();

    shouldFail.expect(IOException.class);
    shouldFail.expectCause(CoreMatchers.isA(InterruptedException.class));
    in.read();
  }

  @Test
  public void shouldBeAbleToProceedWithFullBuffer() throws Exception {
    PipedFromOutput toTest = new PipedFromOutput() {
      @Override
      protected void write(OutputStream to) throws IOException {
        for (int i = 0; i < (PipedFromOutput.MAX_BUFFER_SIZE + 2); ++i) {
          to.write(0);
        }
      }
    };

    ByteStreams.toByteArray(toTest);
  }
}
