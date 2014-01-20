package koka.util.io.guava;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.transform.stream.StreamSource;

import org.xml.sax.InputSource;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;

/**
 * Converts a {@link File} to various types of {@link InputSupplier}.
 * 
 * @deprecated To be removed once Guava removes {@link InputSupplier} in version
 *             18.0
 */
@Deprecated
public final class FileInputSuppliers {
  private FileInputSuppliers() {}

  public static InputSupplier<InputSource> inputSourceTo(final File file) {
    return new InputSupplier<InputSource>() {
      @Override
      public InputSource getInput() {
        return new InputSource(toSystemId(file));
      }
    };
  }

  public static InputSupplier<StreamSource> streamSourceTo(final File file) {
    return new InputSupplier<StreamSource>() {
      @Override
      public StreamSource getInput() {
        return new StreamSource(file);
      }
    };
  }

  public static InputSupplier<FileInputStream> inputStreamTo(File file) {
    return Files.newInputStreamSupplier(file);
  }

  private static String toSystemId(File location) {
    return location.toURI().toASCIIString();
  }
}
