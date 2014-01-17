package koka.util.xml.trax;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public final class SAXAsStAX implements XMLStreamWriter {
  private final CurrentScope env = CurrentScope.ofDocumentRoot();
  private final ContentHandler out;

  // root document doesn't need flush
  private boolean startEleFlushed = true;

  public SAXAsStAX(final ContentHandler out) {
    this.out = out;
  }

  @Override
  public void writeStartDocument() throws XMLStreamException {
    startDoc(null, null);
  }

  @Override
  public void writeStartDocument(String version) throws XMLStreamException {
    startDoc(null, version);
  }

  @Override
  public void writeStartDocument(String encoding, String version)
      throws XMLStreamException {
    startDoc(encoding, version);
  }

  @Override
  public void writeEndDocument() throws XMLStreamException {
    endDoc();
  }

  @Override
  public void writeStartElement(String localName) throws XMLStreamException {
    startEle(new QName(env.defaultUri(), localName, env.defaultUriPrefix()));
  }

  @Override
  public void writeStartElement(String uri, String localName)
      throws XMLStreamException {
    startEle(new QName(uri, localName, env.prefixFor(uri)));
  }

  @Override
  public void writeStartElement(String prefix, String localName, String uri)
      throws XMLStreamException {
    startEle(new QName(uri, localName, prefix));
  }

  @Override
  public void writeEmptyElement(String localName) throws XMLStreamException {
    startEle(new QName(env.defaultUri(), localName, env.defaultUriPrefix()));
    endEle();
  }

  @Override
  public void writeEmptyElement(String uri, String localName)
      throws XMLStreamException {
    startEle(new QName(uri, localName, env.prefixFor(uri)));
    endEle();
  }

  @Override
  public void writeEmptyElement(String prefix, String localName, String uri)
      throws XMLStreamException {
    startEle(new QName(uri, localName, prefix));
    endEle();
  }

  @Override
  public void writeEndElement() throws XMLStreamException {
    endEle();
  }

  @Override
  public void writeAttribute(String name, String val)
      throws XMLStreamException {
    env.addAttr(new QName(env.defaultUri(), name, env.defaultUriPrefix()), val);
  }

  @Override
  public void writeAttribute(String uri, String localName, String value)
      throws XMLStreamException {
    env.addAttr(new QName(uri, localName, env.prefixFor(uri)), value);
  }

  @Override
  public void writeAttribute(String prefix, String uri, String name, String val)
      throws XMLStreamException {
    env.addAttr(new QName(uri, name, prefix), val);
  }

  @Override
  public void writeNamespace(String prefix, String namespaceURI)
      throws XMLStreamException {
    if ((prefix == null) || prefix.equals("") || prefix.equals("xmlns")) {
      writeDefaultNamespace(namespaceURI);
      return;
    }
    namespace(prefix, namespaceURI);
  }

  @Override
  public void writeDefaultNamespace(String uri) throws XMLStreamException {
    namespace(env.defaultUriPrefix(), uri);
    setDefaultNamespace(uri);
  }

  @Override
  public void writeComment(String data) throws XMLStreamException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void writeProcessingInstruction(String target)
      throws XMLStreamException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void writeProcessingInstruction(String target, String data)
      throws XMLStreamException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void writeCData(String data) throws XMLStreamException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void writeDTD(String dtd) throws XMLStreamException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void writeEntityRef(String name) throws XMLStreamException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void writeCharacters(String text) throws XMLStreamException {
    try {
      chars(text.toCharArray(), 0, text.toCharArray().length);
    } catch (SAXException e) {
      throw new XMLStreamException(e);
    }
  }

  @Override
  public void writeCharacters(char[] text, int start, int len)
      throws XMLStreamException {
    try {
      chars(text, start, len);
    } catch (SAXException e) {
      throw new XMLStreamException(e);
    }
  }

  @Override
  public String getPrefix(String uri) throws XMLStreamException {
    return env.prefixFor(uri);
  }

  @Override
  public void setPrefix(String prefix, String uri) throws XMLStreamException {
    env.addPrefix(prefix, uri);
  }

  @Override
  public void setDefaultNamespace(String uri) throws XMLStreamException {
    env.setDefaultUri(uri);
  }

  @Override
  public void setNamespaceContext(NamespaceContext context)
      throws XMLStreamException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public NamespaceContext getNamespaceContext() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Object getProperty(String name) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void close() {}

  @Override
  public void flush() {}

  private void startDoc(String encoding, String version)
      throws XMLStreamException {
    if (encoding != null) {
      throw new UnsupportedOperationException("encoding");
    }
    if (version != null) {
      throw new UnsupportedOperationException("version");
    }
    try {
      out.startDocument();
    } catch (SAXException e) {
      throw new XMLStreamException(e);
    }
  }

  private void endDoc() throws XMLStreamException {
    try {
      out.endDocument();
    } catch (SAXException e) {
      throw new XMLStreamException(e);
    }
  }

  private void startEle(QName ele) throws XMLStreamException {
    flushStartElement();
    env.pushScope(ele);
    startEleFlushed = false;
  }

  private void endEle() throws XMLStreamException {
    flushStartElement();
    QName ele = env.popScope();
    try {
      out.endElement(ele.getNamespaceURI(), ele.getLocalPart(), qualify(ele));
    } catch (final SAXException e) {
      throw new XMLStreamException(e);
    }
  }

  private void checkCanWriteNamespace(String prefix, String uri) {
    if (env.isAtDocumentRoot() || startEleFlushed) {
      throw new IllegalStateException(
          "Invalid state: start tag is not opened at writeNamespace("
              + prefix + ", " + uri + ")");
    }
  }

  private void namespace(String prefix, String uri) throws XMLStreamException {
    checkCanWriteNamespace(prefix, uri);
//    writeAttribute(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, prefix, uri);
  }

  private void chars(char[] ch, int offset, int len)
      throws XMLStreamException,
      SAXException {
    flushStartElement();
    out.characters(ch, offset, len);
  }

  private void flushStartElement() throws XMLStreamException {
    if (!startEleFlushed) {
      Attributes attrs = env.getAttrs();
      QName curr = env.peek();
      try {
        out.startElement(
            curr.getNamespaceURI(),
            curr.getLocalPart(),
            qualify(curr),
            attrs);
      } catch (final SAXException e) {
        throw new XMLStreamException(e);
      }
    }
    startEleFlushed = true;
  }

  private static String qualify(QName qn) {
    if (qn.getPrefix().equals(XMLConstants.DEFAULT_NS_PREFIX)) {
      return qn.getLocalPart();
    }
    return qn.getPrefix() + ":" + qn.getLocalPart();
  }
}
