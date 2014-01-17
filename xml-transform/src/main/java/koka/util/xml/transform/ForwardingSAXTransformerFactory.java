package koka.util.xml.transform;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.XMLFilter;

public abstract class ForwardingSAXTransformerFactory
    extends SAXTransformerFactory {
  protected abstract SAXTransformerFactory delegate();

  @Override
  public TransformerHandler newTransformerHandler(Source src)
      throws TransformerConfigurationException {
    return delegate().newTransformerHandler(src);
  }

  @Override
  public TransformerHandler newTransformerHandler(Templates templates)
      throws TransformerConfigurationException {
    return delegate().newTransformerHandler(templates);
  }

  @Override
  public TransformerHandler newTransformerHandler()
      throws TransformerConfigurationException {
    return delegate().newTransformerHandler();
  }

  @Override
  public TemplatesHandler newTemplatesHandler()
      throws TransformerConfigurationException {
    return delegate().newTemplatesHandler();
  }

  @Override
  public XMLFilter newXMLFilter(Source src)
      throws TransformerConfigurationException {
    return delegate().newXMLFilter(src);
  }

  @Override
  public XMLFilter newXMLFilter(Templates templates)
      throws TransformerConfigurationException {
    return delegate().newXMLFilter(templates);
  }

  @Override
  public Transformer newTransformer(Source source)
      throws TransformerConfigurationException {
    return delegate().newTransformer(source);
  }

  @Override
  public Transformer newTransformer()
      throws TransformerConfigurationException {
    return delegate().newTransformer();
  }

  @Override
  public Templates newTemplates(Source source)
      throws TransformerConfigurationException {
    return delegate().newTemplates(source);
  }

  @Override
  public Source getAssociatedStylesheet(
      Source source, String media, String title, String charset)
      throws TransformerConfigurationException {
    return delegate().getAssociatedStylesheet(source, media, title, charset);
  }

  @Override
  public void setURIResolver(URIResolver resolver) {
    delegate().setURIResolver(resolver);
  }

  @Override
  public URIResolver getURIResolver() {
    return delegate().getURIResolver();
  }

  @Override
  public void setFeature(String name, boolean value)
      throws TransformerConfigurationException {
    delegate().setFeature(name, value);
  }

  @Override
  public boolean getFeature(String name) {
    return delegate().getFeature(name);
  }

  @Override
  public void setAttribute(String name, Object value) {
    delegate().setAttribute(name, value);
  }

  @Override
  public Object getAttribute(String name) {
    return delegate().getAttribute(name);
  }

  @Override
  public void setErrorListener(ErrorListener listener) {
    delegate().setErrorListener(listener);
  }

  @Override
  public ErrorListener getErrorListener() {
    return delegate().getErrorListener();
  }
}
