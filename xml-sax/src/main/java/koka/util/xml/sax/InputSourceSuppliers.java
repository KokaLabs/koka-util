package koka.util.xml.sax;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URL;

import javax.annotation.Nullable;

import koka.util.io.guava.FileInputSuppliers;
import koka.util.net.guava.URLInputSuppliers;

import org.xml.sax.InputSource;

import com.google.common.io.InputSupplier;

/**
 * @since 0.7
 */
public final class InputSourceSuppliers {
  public static InputSupplier<InputSource> from(URL xml) {
    return URLInputSuppliers.inputSourceTo(xml);
  }

  public static InputSupplier<InputSource> from(File xml) {
    return FileInputSuppliers.inputSourceTo(xml);
  }

  public static InputSupplier<InputSource> fromSystemId(final String url) {
    return new InputSupplier<InputSource>() {
      @Override
      public InputSource getInput() {
        return new InputSource(url);
      }
    };
  }

  public static InputSupplier<InputSource> fromBytes(
      InputSupplier<? extends InputStream> xml) {
    return fromBytesAndSystemId(xml, null);
  }

  public static InputSupplier<InputSource> fromBytesAndSystemId(
      final InputSupplier<? extends InputStream> xml,
      @Nullable final URI systemId) {
    return new InputSupplier<InputSource>() {
      @Override
      public InputSource getInput() throws IOException {
        return plus(new InputSource(xml.getInput()), systemId);
      }
    };
  }

  public static InputSupplier<InputSource> fromReader(
      InputSupplier<? extends Reader> xml) {
    return fromReaderAndSystemId(xml, null);
  }

  public static InputSupplier<InputSource> fromReaderAndSystemId(
      final InputSupplier<? extends Reader> xml,
      @Nullable final URI systemId) {
    return new InputSupplier<InputSource>() {
      @Override
      public InputSource getInput() throws IOException {
        return plus(new InputSource(xml.getInput()), systemId);
      }
    };
  }

  private static InputSource plus(InputSource is, @Nullable URI systemId) {
    if (systemId != null) {
      is.setSystemId(systemId.toASCIIString());
    }
    return is;
  }

  private InputSourceSuppliers() {}
}
