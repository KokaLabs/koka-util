package koka.util.xml.transform;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.google.common.io.InputSupplier;

public final class SAXSourceSuppliers {
  /**
   * @since 0.7
   */
  public static InputSupplier<SAXSource> from(
      final SAXParserFactory xmlReaderProvider,
      final InputSupplier<? extends InputSource> xml) {
    return new InputSupplier<SAXSource>() {
      @Override
      public SAXSource getInput() throws IOException {
        try {
          XMLReader r = xmlReaderProvider.newSAXParser().getXMLReader();
          return new SAXSource(r, xml.getInput());
        } catch (ParserConfigurationException e) {
          throw new IOException(e);
        } catch (SAXException e) {
          throw new IOException(e);
        }
      }
    };
  }

  private SAXSourceSuppliers() {}
}
