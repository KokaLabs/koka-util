package koka.util.xml.stax;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Prevent namespace prefixes from being passed to a {@link XMLStreamWriter}.
 */
public class XMLStreamWriterIgnorePrefixes
    extends ForwardingXMLStreamWriter {
  private final XMLStreamWriter delegate;

  public XMLStreamWriterIgnorePrefixes(XMLStreamWriter delegate) {
    this.delegate = delegate;
  }

  @Override
  protected XMLStreamWriter delegate() {
    return delegate;
  }

  @Override
  public void writeStartElement(String pre, String ln, String uri)
      throws XMLStreamException {
    delegate().writeStartElement(uri, ln);
  }

  @Override
  public void writeEmptyElement(String pre, String ln, String uri)
      throws XMLStreamException {
    delegate().writeEmptyElement(uri, ln);
  }

  @Override
  public void writeAttribute(String pre, String uri, String ln, String value)
      throws XMLStreamException {
    delegate().writeAttribute(uri, ln, value);
  }

  @Override
  public void writeNamespace(String pre, String uri) {}

  @Override
  public void setNamespaceContext(NamespaceContext context) {}

  @Override
  public void setPrefix(String pre, String uri) {}
}
