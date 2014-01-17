package koka.util.xml.transform;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

import com.google.common.collect.Lists;
import com.google.common.io.InputSupplier;

public final class TransformerUtil {
  private TransformerUtil() {}

  /**
   * Calls {@link Transformer#setParameter(String, Object)} against a map.
   */
  public static void setParameters(Transformer on, Map<String, ?> with) {
    for (Map.Entry<String, ?> e : with.entrySet()) {
      on.setParameter(e.getKey(), e.getValue());
    }
  }

  /**
   * @throws IOException Fail on open/close stream.
   * @throws TransformerConfigurationException Bad XSL.
   */
  public static List<Templates> newTemplates(
      TransformerFactory from,
      Iterable<? extends InputSupplier<? extends Source>> xsls)
      throws IOException, TransformerConfigurationException {
    List<Templates> result = Lists.newArrayList();
    for (InputSupplier<? extends Source> xsl : xsls) {
      result.add(newTemplates(from, xsl));
    }
    return result;
  }

  /**
   * @throws IOException Fail on open/close stream.
   * @throws TransformerConfigurationException Bad XSL.
   */
  public static Templates newTemplates(
      TransformerFactory from, InputSupplier<? extends Source> xsl)
      throws IOException, TransformerConfigurationException {
    boolean threw = true;
    Source in = xsl.getInput();
    try {
      Templates result = from.newTemplates(in);
      threw = false;
      return result;
    } finally {
      XmlTransformCloseables.close(in, threw);
    }
  }
}
