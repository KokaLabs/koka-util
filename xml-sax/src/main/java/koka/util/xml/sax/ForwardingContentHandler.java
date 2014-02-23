package koka.util.xml.sax;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Forwards all calls to a delegate {@link ContentHandler}.
 */
public abstract class ForwardingContentHandler implements ContentHandler {
  protected abstract ContentHandler delegate();

  @Override
  public void setDocumentLocator(Locator locator) {
    delegate().setDocumentLocator(locator);
  }

  @Override
  public void startDocument() throws SAXException {
    delegate().startDocument();
  }

  @Override
  public void endDocument() throws SAXException {
    delegate().endDocument();
  }

  @Override
  public void startPrefixMapping(String prefix, String uri) throws SAXException {
    delegate().startPrefixMapping(prefix, uri);
  }

  @Override
  public void endPrefixMapping(String prefix) throws SAXException {
    delegate().endPrefixMapping(prefix);
  }

  @Override
  public void startElement(String uri, String ln, String qName, Attributes atts)
      throws SAXException {
    delegate().startElement(uri, ln, qName, atts);
  }

  @Override
  public void endElement(String uri, String localName, String qName)
      throws SAXException {
    delegate().endElement(uri, localName, qName);
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    delegate().characters(ch, start, length);
  }

  @Override
  public void ignorableWhitespace(char[] ch, int start, int length)
      throws SAXException {
    delegate().ignorableWhitespace(ch, start, length);
  }

  @Override
  public void processingInstruction(String target, String data)
      throws SAXException {
    delegate().processingInstruction(target, data);
  }

  @Override
  public void skippedEntity(String name) throws SAXException {
    delegate().skippedEntity(name);
  }
}
