package koka.util.xml.trax;

import java.io.IOException;

import javax.xml.transform.sax.SAXSource;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

import com.google.common.annotations.Beta;

/**
 * Converts events that need to be sent to a {@link ContentHandler} into a
 * {@link javax.xml.transform.Source}.
 * <p>
 * Allows for efficiently streaming XML.
 */
@Beta
public abstract class PipedSAXSource extends SAXSource {
  protected PipedSAXSource() {
    setXMLReader(new CallWriteDuringSax());
  }

  protected abstract void writeTo(ContentHandler sink)
      throws IOException, SAXException;

  private class CallWriteDuringSax extends XMLFilterImpl {
    @Override
    public void parse(InputSource ignored) throws IOException, SAXException {
      writeTo(getContentHandler());
    }

    @Override
    public void setFeature(String name, boolean value) {}
  }
}
