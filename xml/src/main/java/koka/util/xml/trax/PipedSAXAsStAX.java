package koka.util.xml.trax;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public abstract class PipedSAXAsStAX extends PipedSAXSource {
  protected abstract void writeTo(XMLStreamWriter sink)
      throws IOException, XMLStreamException;

  @Override
  protected void writeTo(ContentHandler sink)
      throws IOException,
      SAXException {
    try {
      writeTo(new SAXAsStAX(sink));
    } catch (XMLStreamException e) {
      throw new SAXException(e);
    }
  }
}
