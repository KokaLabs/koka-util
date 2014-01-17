package koka.util.xml.transform;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.OutputStream;
import java.io.Writer;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class XmlTransformCloseables_StreamResult_Test {
  @Rule
  public final ExpectedException shouldFail = ExpectedException.none();

  private final OutputStream out = mock(OutputStream.class);
  private final Writer writer = mock(Writer.class);

  private Result stream;

  @Before
  public void setup() {
    StreamResult temp = new StreamResult();
    temp.setOutputStream(out);
    temp.setWriter(writer);

    stream = temp;
  }

  @Test
  public void swallow_ClosesBoth() throws Exception {
    XmlTransformCloseables.close(stream, true);

    verify(out).close();
    verify(writer).close();
  }

  @Test
  public void noSwallow_ClosesBoth() throws Exception {
    XmlTransformCloseables.close(stream, false);

    verify(out).close();
    verify(writer).close();
  }
}
