package koka.util.xml.trax;

import static javax.xml.transform.TransformerFactory.newInstance;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URI;

import javax.xml.transform.Result;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import koka.util.io.guava.PipedFromOutput;
import koka.util.xml.transform.StreamSourceSuppliers;

import com.google.common.annotations.Beta;
import com.google.common.io.InputSupplier;

@Beta
public final class Xml implements InputSupplier<SAXSource> {
  private static final TransformerFactory TRAX = newInstance();

  public static Xml of(InputSupplier<? extends SAXSource> from) {
    return new Xml(from);
  }

  private final InputSupplier<? extends SAXSource> from;

  private Xml(InputSupplier<? extends SAXSource> from) {
    this.from = from;
  }

  @Override
  public SAXSource getInput() throws IOException {
    return from.getInput();
  }

  public InputSupplier<StreamSource> asStreamSource() throws IOException {
    return StreamSourceSuppliers.fromBytesAndSystemId(asBytes(), sysIdOrNull());
  }

  public InputSupplier<InputStream> asBytes() {
    return new PipedFromOutput() {
      @Override
      protected void write(OutputStream to) throws IOException {
        copyTo(to);
      }
    };
  }

  public String contents() throws IOException {
    StringWriter tmp = new StringWriter();
    copyTo(new StreamResult(tmp));
    return tmp.toString();
  }

  public void copyTo(OutputStream to) throws IOException {
    copyTo(new StreamResult(to));
  }

  public void copyTo(Result sink) throws IOException {
    try {
      TRAX.newTransformer().transform(from.getInput(), sink);
    } catch (TransformerException e) {
      throw new IOException(e);
    }
  }

  private URI sysIdOrNull() throws IOException {
    String id = from.getInput().getSystemId();
    return id != null ? URI.create(id) : null;
  }
}
