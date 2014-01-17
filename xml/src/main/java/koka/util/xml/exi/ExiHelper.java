package koka.util.xml.exi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.google.common.io.InputSupplier;

/**
 * Source: http://www.java2s.com/Code/Java/XML/SourceToInputSource.htm
 */
final class ExiHelper {
  static InputSupplier<InputSource> sourceToInputSource(
      final InputSupplier<? extends Source> source) {
    return new InputSupplier<InputSource>() {
      @Override
      public InputSource getInput() throws IOException {
        return sourceToInputSource(source.getInput());
      }
    };
  }

  static InputSource sourceToInputSource(Source unknown) throws IOException {
    try {
      if (unknown instanceof SAXSource) {
        return ((SAXSource) unknown).getInputSource();
      } else if (unknown instanceof DOMSource) {
        return dom((DOMSource) unknown);
      } else if (unknown instanceof StreamSource) {
        return stream((StreamSource) unknown);
      } else {
        return new InputSource(unknown.getSystemId());
      }
    } catch (TransformerException e) {
      throw new IOException(e);
    }
  }

  private ExiHelper() {}

  private static InputSource stream(final StreamSource ss) {
    InputSource isource = new InputSource(ss.getSystemId());
    isource.setByteStream(ss.getInputStream());
    isource.setCharacterStream(ss.getReader());
    isource.setPublicId(ss.getPublicId());
    return isource;
  }

  private static InputSource dom(final DOMSource source)
      throws TransformerException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Node node = source.getNode();
    if (node instanceof Document) {
      node = ((Document) node).getDocumentElement();
    }
    elementToStream((Element) node, baos);
    InputSource isource = new InputSource(source.getSystemId());
    isource.setByteStream(new ByteArrayInputStream(baos.toByteArray()));
    return isource;
  }

  private static void elementToStream(Element element, OutputStream out)
      throws TransformerException {
    DOMSource source = new DOMSource(element);
    StreamResult result = new StreamResult(out);
    TransformerFactory transFactory = TransformerFactory.newInstance();
    Transformer transformer = transFactory.newTransformer();
    transformer.transform(source, result);
  }
}
