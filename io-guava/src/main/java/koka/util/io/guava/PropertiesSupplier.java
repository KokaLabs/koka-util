package koka.util.io.guava;

import static com.google.common.io.Closeables.close;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.google.common.io.InputSupplier;

public class PropertiesSupplier implements InputSupplier<Properties> {
  private final InputSupplier<? extends InputStream> linesFormat;

  /**
   * Input should be in simple line format.
   * 
   * @see {@link Properties#load(InputStream)}.
   */
  public PropertiesSupplier(InputSupplier<? extends InputStream> linesFormat) {
    this.linesFormat = linesFormat;
  }

  @Override
  public Properties getInput() throws IOException {
    boolean threw = true;
    InputStream in = linesFormat.getInput();
    try {
      Properties result = new Properties();
      result.load(in);
      threw = false;
      return result;
    } finally {
      close(in, threw);
    }
  }
}
