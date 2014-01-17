package koka.util.xml.sax;

import java.io.IOException;

import koka.util.io.guava.Closeables2;

import org.xml.sax.InputSource;

public final class XmlSaxCloseables {
  private XmlSaxCloseables() {}

  public static void close(InputSource source, boolean swallow)
      throws IOException {
    Closeables2.close2(
        source.getByteStream(),
        source.getCharacterStream(),
        swallow);
  }
}
