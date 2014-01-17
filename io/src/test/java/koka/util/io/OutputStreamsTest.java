package koka.util.io;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class OutputStreamsTest {
  @Parameters
  public static Collection<Object[]> testCases() {
    OutputStream d1 = mock(OutputStream.class);
    OutputStream d2 = mock(OutputStream.class);

    Object[][] result = {
      { OutputStreams.closeIgnoring(d1), d1 },
      { OutputStreams.nonCloseable(d2), d2 },
    };

    return Arrays.asList(result);
  }

  private final OutputStream toTest;
  private final OutputStream delegate;

  public OutputStreamsTest(OutputStream toTest, OutputStream delegate) {
    this.toTest = toTest;
    this.delegate = delegate;
  }

  @Test
  public void usesByteArrayOverloading() throws Exception {
    byte[] in = new byte[0];
    toTest.write(in);
    verify(delegate).write(in);
  }

  @Test
  public void usesByteArrayOffsetOverloading() throws Exception {
    byte[] in = new byte[10];
    int offset = 0;
    int len = 10;
    toTest.write(in, offset, len);
    verify(delegate).write(in, offset, len);
  }

  @Test
  public void usesByteOverloading() throws Exception {
    byte in = 1;
    toTest.write(in);
    verify(delegate).write(in);
  }
}
