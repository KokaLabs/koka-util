package koka.util.xml.transform;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URL;

import javax.annotation.Nullable;
import javax.xml.transform.stream.StreamSource;

import koka.util.io.guava.FileInputSuppliers;
import koka.util.net.guava.URLInputSuppliers;

import com.google.common.io.InputSupplier;

/**
 * @since 0.7
 */
public final class StreamSourceSuppliers {
  public static InputSupplier<StreamSource> from(URL xml) {
    return URLInputSuppliers.streamSourceTo(xml);
  }

  public static InputSupplier<StreamSource> from(File xml) {
    return FileInputSuppliers.streamSourceTo(xml);
  }

  public static InputSupplier<StreamSource> fromSystemId(final String url) {
    return new InputSupplier<StreamSource>() {
      @Override
      public StreamSource getInput() {
        return new StreamSource(url);
      }
    };
  }

  public static InputSupplier<StreamSource> fromBytes(
      InputSupplier<? extends InputStream> xml) {
    return fromBytesAndSystemId(xml, null);
  }

  public static InputSupplier<StreamSource> fromBytesAndSystemId(
      final InputSupplier<? extends InputStream> xml,
      @Nullable final URI systemId) {
    return new InputSupplier<StreamSource>() {
      @Override
      public StreamSource getInput() throws IOException {
        return new StreamSource(xml.getInput(), toSystemIdOrNull(systemId));
      }
    };
  }

  public static InputSupplier<StreamSource> fromReader(
      InputSupplier<? extends Reader> xml) {
    return fromReaderAndSystemId(xml, null);
  }

  public static InputSupplier<StreamSource> fromReaderAndSystemId(
      final InputSupplier<? extends Reader> xml,
      @Nullable final URI systemId) {
    return new InputSupplier<StreamSource>() {
      @Override
      public StreamSource getInput() throws IOException {
        return new StreamSource(xml.getInput(), toSystemIdOrNull(systemId));
      }
    };
  }

  @Nullable
  private static String toSystemIdOrNull(@Nullable URI maybe) {
    return maybe != null ? maybe.toASCIIString() : null;
  }

  private StreamSourceSuppliers() {}
}
