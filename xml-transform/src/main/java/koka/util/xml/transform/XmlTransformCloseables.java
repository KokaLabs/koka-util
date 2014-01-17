package koka.util.xml.transform;

import java.io.IOException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import koka.util.io.guava.Closeables2;
import koka.util.xml.sax.XmlSaxCloseables;
import koka.util.xml.stax.XmlStaxCloseables;

import org.xml.sax.InputSource;

public final class XmlTransformCloseables {
  private XmlTransformCloseables() {}

  public static void close(Source source, boolean swallow)
      throws IOException {
    if (source instanceof StreamSource) {
      close((StreamSource) source, swallow);
    } else if (source instanceof SAXSource) {
      close((SAXSource) source, swallow);
    } else if (source instanceof StAXSource) {
      closeWrapped((StAXSource) source, swallow);
    }
  }

  public static void close(Result result, boolean swallow)
      throws IOException {
    if (result instanceof StreamResult) {
      close((StreamResult) result, swallow);
    } else if (result instanceof StAXResult) {
      closeWrapped((StAXResult) result, swallow);
    }
  }

  public static void close(StreamSource stream, boolean swallow)
      throws IOException {
    Closeables2.close2(stream.getInputStream(), stream.getReader(), swallow);
  }

  public static void close(StreamResult stream, boolean swallow)
      throws IOException {
    Closeables2.close2(stream.getOutputStream(), stream.getWriter(), swallow);
  }

  public static void close(SAXSource sax, boolean swallow)
      throws IOException {
    InputSource maybe = sax.getInputSource();
    if (maybe != null) {
      XmlSaxCloseables.close(maybe, swallow);
    }
  }

  private static void closeWrapped(StAXSource stax, boolean swallow)
      throws IOException {
    try {
      close(stax, swallow);
    } catch (XMLStreamException e) {
      throw new IOException(e);
    }
  }

  private static void closeWrapped(StAXResult stax, boolean swallow)
      throws IOException {
    try {
      close(stax, swallow);
    } catch (XMLStreamException e) {
      throw new IOException(e);
    }
  }

  public static void close(StAXSource stax, boolean swallow)
      throws XMLStreamException {
    XMLEventReader maybe = stax.getXMLEventReader();
    if (maybe != null) {
      XmlStaxCloseables.close(maybe, swallow);
    } else {
      XmlStaxCloseables.close(stax.getXMLStreamReader(), swallow);
    }
  }

  public static void close(StAXResult stax, boolean swallow)
      throws XMLStreamException {
    XMLEventWriter maybe = stax.getXMLEventWriter();
    if (maybe != null) {
      XmlStaxCloseables.close(maybe, swallow);
    } else {
      XmlStaxCloseables.close(stax.getXMLStreamWriter(), swallow);
    }
  }
}
