package koka.util.xml.transform;

import java.util.Iterator;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;

/**
 * Zero or more XSLs that should be efficiently applied in order. <br>
 * The output of a given XSL is streamed as input to the next XSL.
 * 
 * @author Booz Allen Hamilton
 */
public class TemplatesChain {
  private final SAXTransformerFactory sax;
  private final Iterable<? extends Templates> xsls;

  public TemplatesChain(
      SAXTransformerFactory sax,
      Iterable<? extends Templates> xsls) {
    this.sax = sax;
    this.xsls = xsls;
  }

  /**
   * Applies this series of transforms while piping XML.
   */
  public void transform(Source from, Result to) throws TransformerException {
    Transformer identity = sax.newTransformer();
    identity.transform(from, applyWhenWriting(to));
  }

  /**
   * Returned result filters anything written to it before passing to given out.
   * 
   * @since 0.2
   */
  public Result applyWhenWriting(Result to)
      throws TransformerConfigurationException {
    Iterator<? extends Templates> chain = xsls.iterator();

    if (!chain.hasNext()) {
      return to;
    }

    TransformerHandler first = sax.newTransformerHandler(chain.next());

    TransformerHandler curr = first;
    while (chain.hasNext()) {
      TransformerHandler next = sax.newTransformerHandler(chain.next());
      curr.setResult(new SAXResult(next));
      curr = next;
    }
    curr.setResult(to);

    return new SAXResult(first);
  }

  /**
   * Returns a reader which sends filtered events to its content handler.
   * 
   * @since 0.2
   */
  public XMLReader applyWhenReading(XMLReader from)
      throws TransformerConfigurationException {
    XMLReader curr = from;

    for (Templates xsl : xsls) {
      XMLFilter next = sax.newXMLFilter(xsl);
      next.setParent(curr);
      curr = next;
    }

    return curr;
  }
}
