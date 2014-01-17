package koka.util.xml.transform;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import javax.xml.transform.Result;
import javax.xml.transform.sax.SAXResult;

import org.junit.Test;

public class XmlTransformCloseables_SAXResult_Test {
  private final Result sax = mock(SAXResult.class);

  @Test
  public void swallow_ClosesBoth() throws Exception {
    XmlTransformCloseables.close(sax, true);
    verifyZeroInteractions(sax);
  }

  @Test
  public void noSwallow_ClosesBoth() throws Exception {
    XmlTransformCloseables.close(sax, false);
    verifyZeroInteractions(sax);
  }
}
