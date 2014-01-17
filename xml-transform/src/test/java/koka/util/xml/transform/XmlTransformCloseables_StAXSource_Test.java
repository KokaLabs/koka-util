package koka.util.xml.transform;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.stax.StAXSource;

import org.junit.Test;

public class XmlTransformCloseables_StAXSource_Test {
  private final XMLStreamReader stream = mock(XMLStreamReader.class);
  private final XMLEventReader event = mock(XMLEventReader.class);

  @Test
  public void swallow_Stream() throws Exception {
    XmlTransformCloseables.close(streamBased(), true);
    verify(stream).close();
  }

  @Test
  public void noSwallow_Stream() throws Exception {
    XmlTransformCloseables.close(streamBased(), false);
    verify(stream).close();
  }

  @Test
  public void swallow_Event() throws Exception {
    XmlTransformCloseables.close(eventBased(), true);
    verify(event).close();
  }

  @Test
  public void noSwallow_Event() throws Exception {
    XmlTransformCloseables.close(eventBased(), false);
    verify(event).close();
  }

  private Source eventBased() {
    StAXSource stax = mock(StAXSource.class);
    given(stax.getXMLEventReader()).willReturn(event);
    return stax;
  }

  private Source streamBased() {
    StAXSource stax = mock(StAXSource.class);
    given(stax.getXMLStreamReader()).willReturn(stream);
    return stax;
  }
}
