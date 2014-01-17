package koka.util.xml.transform;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.InputStream;
import java.io.Reader;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class XmlTransformCloseables_StreamSource_Test {
  @Rule
  public final ExpectedException shouldFail = ExpectedException.none();

  private final InputStream in = mock(InputStream.class);
  private final Reader reader = mock(Reader.class);

  private Source stream;

  @Before
  public void setup() {
    StreamSource temp = new StreamSource();
    temp.setInputStream(in);
    temp.setReader(reader);

    stream = temp;
  }

  @Test
  public void swallow_ClosesBoth() throws Exception {
    XmlTransformCloseables.close(stream, true);

    verify(in).close();
    verify(reader).close();
  }

  @Test
  public void noSwallow_ClosesBoth() throws Exception {
    XmlTransformCloseables.close(stream, false);

    verify(in).close();
    verify(reader).close();
  }
}
