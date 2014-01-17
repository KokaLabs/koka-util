package koka.util.xml.transform;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.InputStream;
import java.io.Reader;

import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xml.sax.InputSource;

public class XmlTransformCloseables_SAXSource_Test {
  @Rule
  public final ExpectedException shouldFail = ExpectedException.none();

  private final InputStream in = mock(InputStream.class);
  private final Reader reader = mock(Reader.class);

  private Source sax;

  @Before
  public void setup() {
    InputSource source = new InputSource();
    source.setByteStream(in);
    source.setCharacterStream(reader);

    SAXSource temp = new SAXSource();
    temp.setInputSource(source);

    sax = temp;
  }

  @Test
  public void swallow_ClosesBoth() throws Exception {
    XmlTransformCloseables.close(sax, true);

    verify(in).close();
    verify(reader).close();
  }

  @Test
  public void noSwallow_ClosesBoth() throws Exception {
    XmlTransformCloseables.close(sax, false);

    verify(in).close();
    verify(reader).close();
  }

  @Test
  public void swallow_NoInputSource_Ignores() throws Exception {
    sax = new SAXSource((InputSource) null);
    XmlTransformCloseables.close(sax, true);
  }
}
