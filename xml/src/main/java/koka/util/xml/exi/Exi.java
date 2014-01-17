// Needs nagasena

//package koka.util.xml.exi;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.Arrays;
//
//import javax.xml.transform.sax.SAXSource;
//
//import koka.util.common.annotation.Internal;
//import koka.util.io.guava.PipedFromOutput;
//import koka.util.xml.trax.PipedSAXSource;
//import koka.util.xml.trax.Xml;
//
//import org.xml.sax.ContentHandler;
//import org.xml.sax.InputSource;
//import org.xml.sax.SAXException;
//
//import com.google.common.annotations.Beta;
//import com.google.common.io.ByteStreams;
//import com.google.common.io.InputSupplier;
//
//@Beta
//public class Exi implements InputSupplier<InputStream> {
//  private final InputSupplier<? extends InputStream> from;
//  private final GrammarCache config;
//
//  public Exi(
//      InputSupplier<? extends InputStream> alreadyEncoded,
//      GrammarCache config) {
//    from = alreadyEncoded;
//    this.config = config;
//  }
//
//  public void copyTo(OutputStream out) throws IOException {
//    ByteStreams.copy(from, out);
//  }
//
//  @Override
//  public InputStream getInput() throws IOException {
//    return from.getInput();
//  }
//
//  @Internal
//  public String contents() throws IOException {
//    ByteArrayOutputStream tmp = new ByteArrayOutputStream();
//    copyTo(tmp);
//    return Arrays.toString(tmp.toByteArray());
//  }
//
//  public Xml decode() throws EXIOptionsException {
//    final EXIReader reader = new EXIReader();
//    reader.setGrammarCache(config);
//    return Xml.of(new InputSupplier<SAXSource>() {
//      @Override
//      public SAXSource getInput() throws IOException {
//        return new PipedSAXSource() {
//          @Override
//          protected void writeTo(ContentHandler sink)
//              throws IOException, SAXException {
//            reader.setContentHandler(sink);
//            reader.parse(new InputSource(from.getInput()));
//          }
//        };
//      }
//    });
//  }
//
//// commented out while throwing null pointer
////  public static Exi encode(Xml source, GrammarCache config) {
////    return new Exi(encoded(source, config), config);
////  }
//
//  private static InputSupplier<? extends InputStream> encoded(
//      final Xml source,
//      final GrammarCache config) {
//    return new PipedFromOutput() {
//      @Override
//      protected void write(OutputStream to) throws IOException {
//        try {
//          Transmogrifier t = new Transmogrifier();
//          t.setGrammarCache(config);
//          t.setOutputStream(to);
//          t.encode(ExiHelper.sourceToInputSource(source).getInput());
//        } catch (TransmogrifierException e) {
//          throw new IOException(e);
//        } catch (EXIOptionsException e) {
//          throw new IOException(e);
//        }
//      }
//    };
//  }
//}
