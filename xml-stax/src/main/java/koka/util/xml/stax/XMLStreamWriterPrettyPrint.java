package koka.util.xml.stax;

import java.util.BitSet;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Decorates another {@link XMLStreamWriter} and produces indenting events.
 */
public final class XMLStreamWriterPrettyPrint
    extends ForwardingXMLStreamWriter {
  static final String NEW_LINE = "\n";

  private final XMLStreamWriter delegate;
  private final String indent;

  private final BitSet levelHasChild = new BitSet();
  private int currLevel = 1;

  public XMLStreamWriterPrettyPrint(XMLStreamWriter delegate, String indent) {
    this.delegate = delegate;
    this.indent = indent;
  }

  @Override
  protected XMLStreamWriter delegate() {
    return delegate;
  }

  @Override
  public void writeStartElement(String ln) throws XMLStreamException {
    indentStartElement();
    delegate().writeStartElement(ln);
  }

  @Override
  public void writeStartElement(String uri, String ln)
      throws XMLStreamException {
    indentStartElement();
    delegate().writeStartElement(uri, ln);
  }

  @Override
  public void writeStartElement(String pre, String ln, String uri)
      throws XMLStreamException {
    indentStartElement();
    delegate().writeStartElement(pre, ln, uri);
  }

  @Override
  public void writeEmptyElement(String ln) throws XMLStreamException {
    indentEmptyElement();
    delegate().writeEmptyElement(ln);
  }

  @Override
  public void writeEmptyElement(String uri, String ln)
      throws XMLStreamException {
    indentEmptyElement();
    delegate().writeEmptyElement(uri, ln);
  }

  @Override
  public void writeEmptyElement(String pre, String ln, String uri)
      throws XMLStreamException {
    indentEmptyElement();
    delegate().writeEmptyElement(pre, ln, uri);
  }

  @Override
  public void writeEndElement() throws XMLStreamException {
    indentEndElement();
    delegate().writeEndElement();
  }

  private void indentStartElement() throws XMLStreamException {
    writeIndentToCurrLevel();
    levelHasChild.clear(currLevel);
    ++currLevel;
  }

  private void indentEmptyElement() throws XMLStreamException {
    writeIndentToCurrLevel();
  }

  private void indentEndElement() throws XMLStreamException {
    --currLevel;
    if (levelHasChild.get(currLevel)) {
      writeIndentToCurrLevel();
    }
  }

  private void writeIndentToCurrLevel() throws XMLStreamException {
    levelHasChild.set(currLevel - 1);
    delegate().writeCharacters(NEW_LINE);
    for (int i = 1; i < currLevel; ++i) {
      delegate().writeCharacters(indent);
    }
  }
}
