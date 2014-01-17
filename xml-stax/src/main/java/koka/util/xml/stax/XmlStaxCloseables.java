package koka.util.xml.stax;

import static java.util.logging.Logger.getLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public final class XmlStaxCloseables {
  private XmlStaxCloseables() {}

  private static final Logger LOG =
      getLogger(XmlStaxCloseables.class.getName());

  public static void close(XMLStreamWriter mightFail, boolean swallowException)
      throws XMLStreamException {
    try {
      mightFail.close();
    } catch (XMLStreamException e) {
      handle(e, swallowException);
    }
  }

  public static void close(XMLEventWriter mightFail, boolean swallowException)
      throws XMLStreamException {
    try {
      mightFail.close();
    } catch (XMLStreamException e) {
      handle(e, swallowException);
    }
  }

  public static void close(XMLStreamReader mightFail, boolean swallowException)
      throws XMLStreamException {
    try {
      mightFail.close();
    } catch (XMLStreamException e) {
      handle(e, swallowException);
    }
  }

  public static void close(XMLEventReader mightFail, boolean swallowException)
      throws XMLStreamException {
    try {
      mightFail.close();
    } catch (XMLStreamException e) {
      handle(e, swallowException);
    }
  }

  private static void handle(XMLStreamException e, boolean swallow)
      throws XMLStreamException {
    if (swallow) {
      LOG.log(Level.WARNING, "exception while closing", e);
    } else {
      throw e;
    }
  }
}
