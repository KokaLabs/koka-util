package koka.util.xml.stax;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Forwards all calls to a delegate {@link XMLStreamWriter}.
 */
public abstract class ForwardingXMLStreamWriter
    implements XMLStreamWriter {
  protected abstract XMLStreamWriter delegate();

  @Override
  public void writeStartElement(String ln) throws XMLStreamException {
    delegate().writeStartElement(ln);
  }

  @Override
  public void writeStartElement(String uri, String ln)
      throws XMLStreamException {
    delegate().writeStartElement(uri, ln);
  }

  @Override
  public void writeStartElement(String pre, String ln, String uri)
      throws XMLStreamException {
    delegate().writeStartElement(pre, ln, uri);
  }

  @Override
  public void writeEmptyElement(String uri, String ln)
      throws XMLStreamException {
    delegate().writeEmptyElement(uri, ln);
  }

  @Override
  public void writeEmptyElement(String pre, String ln, String uri)
      throws XMLStreamException {
    delegate().writeEmptyElement(pre, ln, uri);
  }

  @Override
  public void writeEmptyElement(String ln) throws XMLStreamException {
    delegate().writeEmptyElement(ln);
  }

  @Override
  public void writeEndElement() throws XMLStreamException {
    delegate().writeEndElement();
  }

  @Override
  public void writeEndDocument() throws XMLStreamException {
    delegate().writeEndDocument();
  }

  @Override
  public void close() throws XMLStreamException {
    delegate().close();
  }

  @Override
  public void flush() throws XMLStreamException {
    delegate().flush();
  }

  @Override
  public void writeAttribute(String ln, String value) throws XMLStreamException {
    delegate().writeAttribute(ln, value);
  }

  @Override
  public void writeAttribute(String pre, String uri, String name, String value)
      throws XMLStreamException {
    delegate().writeAttribute(pre, uri, name, value);
  }

  @Override
  public void writeAttribute(String uri, String ln, String value)
      throws XMLStreamException {
    delegate().writeAttribute(uri, ln, value);
  }

  @Override
  public void writeNamespace(String pre, String uri) throws XMLStreamException {
    delegate().writeNamespace(pre, uri);
  }

  @Override
  public void writeDefaultNamespace(String uri) throws XMLStreamException {
    delegate().writeDefaultNamespace(uri);
  }

  @Override
  public void writeComment(String data) throws XMLStreamException {
    delegate().writeComment(data);
  }

  @Override
  public void writeProcessingInstruction(String to) throws XMLStreamException {
    delegate().writeProcessingInstruction(to);
  }

  @Override
  public void writeProcessingInstruction(String target, String data)
      throws XMLStreamException {
    delegate().writeProcessingInstruction(target, data);
  }

  @Override
  public void writeCData(String data) throws XMLStreamException {
    delegate().writeCData(data);
  }

  @Override
  public void writeDTD(String dtd) throws XMLStreamException {
    delegate().writeDTD(dtd);
  }

  @Override
  public void writeEntityRef(String name) throws XMLStreamException {
    delegate().writeEntityRef(name);
  }

  @Override
  public void writeStartDocument() throws XMLStreamException {
    delegate().writeStartDocument();
  }

  @Override
  public void writeStartDocument(String version) throws XMLStreamException {
    delegate().writeStartDocument(version);
  }

  @Override
  public void writeStartDocument(String encoding, String version)
      throws XMLStreamException {
    delegate().writeStartDocument(encoding, version);
  }

  @Override
  public void writeCharacters(String text) throws XMLStreamException {
    delegate().writeCharacters(text);
  }

  @Override
  public void writeCharacters(char[] text, int start, int len)
      throws XMLStreamException {
    delegate().writeCharacters(text, start, len);
  }

  @Override
  public String getPrefix(String uri) throws XMLStreamException {
    return delegate().getPrefix(uri);
  }

  @Override
  public void setPrefix(String pre, String uri) throws XMLStreamException {
    delegate().setPrefix(pre, uri);
  }

  @Override
  public void setDefaultNamespace(String uri) throws XMLStreamException {
    delegate().setDefaultNamespace(uri);
  }

  @Override
  public void setNamespaceContext(NamespaceContext context)
      throws XMLStreamException {
    delegate().setNamespaceContext(context);
  }

  @Override
  public NamespaceContext getNamespaceContext() {
    return delegate().getNamespaceContext();
  }

  @Override
  public Object getProperty(String name) {
    return delegate().getProperty(name);
  }
}
