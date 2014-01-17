package koka.util.io;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.io.OutputStream;

import org.junit.Test;

public class OutputStreamsCloseIgnoringTest {
  @Test
  public void closeDoesNotDoAnything() throws Exception {
    OutputStream delegate = mock(OutputStream.class);
    OutputStream toTest = OutputStreams.closeIgnoring(delegate);
    toTest.close();
    verifyZeroInteractions(delegate);
  }
}
