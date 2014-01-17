package koka.util.xml.sax;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * Forwards all calls to a delegate {@link SAXParserFactory}.
 * 
 * @since 0.7
 */
public abstract class ForwardingSAXParserFactory extends SAXParserFactory {
  protected abstract SAXParserFactory delegate();

  @Override
  public SAXParser newSAXParser()
      throws ParserConfigurationException,
        SAXException {
    return delegate().newSAXParser();
  }

  @Override
  public void setNamespaceAware(boolean awareness) {
    delegate().setNamespaceAware(awareness);
  }

  @Override
  public void setValidating(boolean validating) {
    delegate().setValidating(validating);
  }

  @Override
  public boolean isNamespaceAware() {
    return delegate().isNamespaceAware();
  }

  @Override
  public boolean isValidating() {
    return delegate().isValidating();
  }

  @Override
  public void setFeature(String name, boolean value)
      throws ParserConfigurationException,
        SAXNotRecognizedException,
        SAXNotSupportedException {
    delegate().setFeature(name, value);
  }

  @Override
  public boolean getFeature(String name)
      throws ParserConfigurationException,
        SAXNotRecognizedException,
        SAXNotSupportedException {
    return delegate().getFeature(name);
  }

  @Override
  public Schema getSchema() {
    return delegate().getSchema();
  }

  @Override
  public void setSchema(Schema schema) {
    delegate().setSchema(schema);
  }

  @Override
  public void setXIncludeAware(boolean state) {
    delegate().setXIncludeAware(state);
  }

  @Override
  public boolean isXIncludeAware() {
    return delegate().isXIncludeAware();
  }
}
