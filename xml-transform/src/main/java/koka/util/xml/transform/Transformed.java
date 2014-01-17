package koka.util.xml.transform;

import java.io.IOException;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.google.common.io.InputSupplier;

/**
 * The filtered input after applying some transforms.
 * 
 * @since 0.3
 */
public class Transformed implements InputSupplier<SAXSource> {
  private final InputSupplier<? extends SAXSource> input;
  private final TemplatesChain toBeApplied;

  public Transformed(
      InputSupplier<? extends SAXSource> input,
      TemplatesChain toBeApplied) {
    this.input = input;
    this.toBeApplied = toBeApplied;
  }

  @Override
  public SAXSource getInput() throws IOException {
    SAXSource in = input.getInput();
    XMLReader real = in.getXMLReader();
    InputSource contents = in.getInputSource();
    try {
      return new SAXSource(toBeApplied.applyWhenReading(real), contents);
    } catch (TransformerConfigurationException e) {
      throw new IOException(e);
    }
  }
}
