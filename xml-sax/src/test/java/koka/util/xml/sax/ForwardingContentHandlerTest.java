package koka.util.xml.sax;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

@RunWith(MockitoJUnitRunner.class)
public final class ForwardingContentHandlerTest {
  @Mock
  public ContentHandler delegate;

  public ForwardingContentHandler toTest;

  @Before
  public void setup() {
    toTest = new Impl(delegate);
  }

  @Test
  public void setDocumentLocator() {
    toTest.setDocumentLocator(null);
    verify(delegate).setDocumentLocator(null);
  }

  @Test
  public void startDocument() throws SAXException {
    toTest.startDocument();
    verify(delegate).startDocument();
  }

  @Test
  public void endDocument() throws SAXException {
    toTest.endDocument();
    verify(delegate).endDocument();
  }

  @Test
  public void startPrefixMapping() throws SAXException {
    toTest.startPrefixMapping(null, null);
    verify(delegate).startPrefixMapping(null, null);
  }

  @Test
  public void endPrefixMapping() throws SAXException {
    toTest.endPrefixMapping(null);
    verify(delegate).endPrefixMapping(null);
  }

  @Test
  public void startElement() throws SAXException {
    toTest.startElement(null, null, null, null);
    verify(delegate).startElement(null, null, null, null);
  }

  @Test
  public void endElement() throws SAXException {
    toTest.endElement(null, null, null);
    verify(delegate).endElement(null, null, null);
  }

  @Test
  public void characters() throws SAXException {
    toTest.characters(null, 0, 0);
    verify(delegate).characters(null, 0, 0);
  }

  @Test
  public void ignorableWhitespace() throws SAXException {
    toTest.ignorableWhitespace(null, 0, 0);
    verify(delegate).ignorableWhitespace(null, 0, 0);
  }

  @Test
  public void processingInstruction() throws SAXException {
    toTest.processingInstruction(null, null);
    verify(delegate).processingInstruction(null, null);
  }

  @Test
  public void skippedEntity() throws SAXException {
    toTest.skippedEntity(null);
    verify(delegate).skippedEntity(null);
  }

  private static class Impl extends ForwardingContentHandler {
    private final ContentHandler delegate;

    Impl(ContentHandler delegate) {
      this.delegate = delegate;
    }

    @Override
    protected ContentHandler delegate() {
      return delegate;
    }
  }
}
