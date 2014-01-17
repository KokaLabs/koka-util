package koka.util.xml.transform;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.stax.StAXResult;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class XmlTransformCloseables_StAXResult_Test {
  @Rule
  public final ExpectedException shouldFail = ExpectedException.none();

  private final XMLStreamWriter stream = mock(XMLStreamWriter.class);
  private final XMLEventWriter event = mock(XMLEventWriter.class);

  private Result stax;

  @Test
  public void swallow_Stream() throws Exception {
    stax = new StAXResult(stream);
    XmlTransformCloseables.close(stax, true);
    verify(stream).close();
  }

  @Test
  public void noSwallow_Stream() throws Exception {
    stax = new StAXResult(stream);
    XmlTransformCloseables.close(stax, false);
    verify(stream).close();
  }

  @Test
  public void swallow_Event() throws Exception {
    stax = new StAXResult(event);
    XmlTransformCloseables.close(stax, true);
    verify(event).close();
  }

  @Test
  public void noSwallow_Event() throws Exception {
    stax = new StAXResult(event);
    XmlTransformCloseables.close(stax, false);
    verify(event).close();
  }
}
