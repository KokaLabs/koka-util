// Needs nagasena

// package koka.util.xml.exi;
//
//import java.io.IOException;
//import java.io.OutputStream;
//
//import javax.xml.stream.XMLStreamException;
//import javax.xml.stream.XMLStreamWriter;
//
//import koka.util.io.guava.PipedFromOutput;
//import koka.util.xml.trax.SAXAsStAX;
//
//import com.google.common.annotations.Beta;
//
//@Beta
//public abstract class PipedExi extends PipedFromOutput {
//  protected abstract void writeTo(XMLStreamWriter to)
//      throws XMLStreamException, IOException;
//
//  protected GrammarCache configure() {
//    return new GrammarCache(GrammarOptions.DEFAULT_OPTIONS);
//  }
//
//  private XMLStreamWriter wrap(OutputStream out)
//      throws TransmogrifierException,
//      EXIOptionsException {
//    Transmogrifier t = new Transmogrifier();
//    t.setOutputStream(out);
//    t.setGrammarCache(configure());
//    return new SAXAsStAX(t.getSAXTransmogrifier());
//  }
//
//  @Override
//  protected void write(OutputStream to) throws IOException {
//    try {
//      PipedExi.this.writeTo(PipedExi.this.wrap(to));
//    } catch (TransmogrifierException e) {
//      throw new IOException(e);
//    } catch (XMLStreamException e) {
//      throw new IOException(e);
//    } catch (EXIOptionsException e) {
//      throw new IOException(e);
//    }
//  }
//}
