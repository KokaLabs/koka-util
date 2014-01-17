package koka.util.xml.transform;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.sax.SAXTransformerFactory;

import org.junit.Test;

public class ForwardingSAXTransformerFactoryTest {
  private final SAXTransformerFactory delegate =
      mock(SAXTransformerFactory.class);

  private final Impl toTest = new Impl();

  @Test
  public void newTransformerHandler_Source() throws Exception {
    toTest.newTransformerHandler((Source) null);
    verify(delegate).newTransformerHandler((Source) null);
  }

  @Test
  public void newTransformerHandler_Templates() throws Exception {
    toTest.newTransformerHandler((Templates) null);
    verify(delegate).newTransformerHandler((Templates) null);
  }

  @Test
  public void newTransformerHandler() throws Exception {
    toTest.newTransformerHandler();
    verify(delegate).newTransformerHandler();
  }

  @Test
  public void newTemplatesHandler() throws Exception {
    toTest.newTemplatesHandler();
    verify(delegate).newTemplatesHandler();
  }

  @Test
  public void newXMLFilter_Source() throws Exception {
    toTest.newXMLFilter((Source) null);
    verify(delegate).newXMLFilter((Source) null);
  }

  @Test
  public void newXMLFilter_Templates() throws Exception {
    toTest.newXMLFilter((Templates) null);
    verify(delegate).newXMLFilter((Templates) null);
  }

  @Test
  public void newTransformer_Source() throws Exception {
    toTest.newTransformer(null);
    verify(delegate).newTransformer(null);
  }

  @Test
  public void newTransformer() throws Exception {
    toTest.newTransformer();
    verify(delegate).newTransformer();
  }

  @Test
  public void newTemplates_Source() throws Exception {
    toTest.newTemplates(null);
    verify(delegate).newTemplates(null);
  }

  @Test
  public void AssociatedStylesheet() throws Exception {
    toTest.getAssociatedStylesheet(null, null, null, null);
    verify(delegate).getAssociatedStylesheet(null, null, null, null);
  }

  @Test
  public void setURIResolver() {
    toTest.setURIResolver(null);
    verify(delegate).setURIResolver(null);
  }

  @Test
  public void getURIResolver() {
    toTest.getURIResolver();
    verify(delegate).getURIResolver();
  }

  @Test
  public void setFeature() throws Exception {
    toTest.setFeature(null, false);
    verify(delegate).setFeature(null, false);
  }

  @Test
  public void getFeature() {
    toTest.getFeature(null);
    verify(delegate).getFeature(null);
  }

  @Test
  public void setAttribute() {
    toTest.setAttribute(null, null);
    verify(delegate).setAttribute(null, null);
  }

  @Test
  public void getAttribute() {
    toTest.getAttribute(null);
    verify(delegate).getAttribute(null);
  }

  @Test
  public void setErrorListener() {
    toTest.setErrorListener(null);
    verify(delegate).setErrorListener(null);
  }

  @Test
  public void getErrorListener() {
    toTest.getErrorListener();
    verify(delegate).getErrorListener();
  }

  class Impl extends ForwardingSAXTransformerFactory {
    @Override
    protected SAXTransformerFactory delegate() {
      return delegate;
    }
  }
}
