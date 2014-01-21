package koka.util.net.guava;

import java.io.InputStream;
import java.net.URL;

import javax.xml.transform.stream.StreamSource;

import koka.util.net.URLs;

import org.xml.sax.InputSource;

import com.google.common.io.InputSupplier;
import com.google.common.io.Resources;

/**
 * Converts a {@link URL} to various types of {@link InputSupplier}.
 */
public final class URLInputSuppliers {
  private URLInputSuppliers() {}

  public static InputSupplier<InputSource> inputSourceTo(final URL location) {
    return new InputSupplier<InputSource>() {
      @Override
      public InputSource getInput() {
        return URLs.asInputSource(location);
      }
    };
  }

  public static InputSupplier<StreamSource> streamSourceTo(final URL location) {
    return new InputSupplier<StreamSource>() {
      @Override
      public StreamSource getInput() {
        return URLs.asStreamSource(location);
      }
    };
  }

  public static InputSupplier<InputStream> inputStreamTo(URL url) {
    return Resources.newInputStreamSupplier(url);
  }
}
