package koka.util.xml.trax;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;

public class SAXAsStAXTest {
  private final ContentHandler actual = Mockito.mock(ContentHandler.class);
  private final SAXAsStAX toTest = new SAXAsStAX(actual);

  @Rule
  public ExpectedException shouldFail = ExpectedException.none();

  @Test
  public void startDocumentDelegatesToContentHandler() throws Exception {
    toTest.writeStartDocument();
    Mockito.verify(actual).startDocument();
  }

  @Test
  public void endDocumentDelegatesToContentHandler() throws Exception {
    toTest.writeEndDocument();
    Mockito.verify(actual).endDocument();
  }

  @Test
  public void startEndElementDelegatesToContentHandler() throws Exception {
    toTest.writeStartElement("a");
    toTest.writeEndElement();
    verify(actual).startElement(eq(""), eq("a"), eq("a"), noAttrs());
    verify(actual).endElement(eq(""), eq("a"), eq("a"));
  }

  @Test
  public void startElementWillOnlyWriteOnce() throws Exception {
    toTest.writeStartElement("a");
    toTest.writeStartElement("b");
    toTest.writeEndElement();
    toTest.writeEndElement();
    verify(actual, times(1)).startElement(eq(""), eq("a"), eq("a"), noAttrs());
  }

  @Test
  public void startEndElementWithUriDelegatesToContentHandler()
      throws Exception {
    toTest.writeStartElement("urn:2", "b");
    toTest.writeEndElement();
    verify(actual).startElement(eq("urn:2"), eq("b"), eq("b"), noAttrs());
    verify(actual).endElement(eq("urn:2"), eq("b"), eq("b"));
  }

  @Test
  public void startEndElementWithPrefixAndUriDelegatesToContentHandler()
      throws Exception {
    toTest.writeStartElement("pee", "c", "urn:3");
    toTest.writeEndElement();
    verify(actual).startElement(eq("urn:3"), eq("c"), eq("pee:c"), noAttrs());
    verify(actual).endElement(eq("urn:3"), eq("c"), eq("pee:c"));
  }

  @Test
  public void attributesDelegateToContentHandler() throws Exception {
    toTest.writeStartElement("e");
    toTest.writeAttribute("a", "1");
    toTest.writeEndElement();
    AttributesImpl a = new AttributesImpl();
    a.addAttribute("", "a", "a", "", "1");
    verify(actual).startElement(eq(""), eq("e"), eq("e"), eqAttr(a));
  }

  @Test
  public void attributesWithUriDelegateToContentHandler() throws Exception {
    toTest.writeStartElement("e");
    toTest.writeAttribute("urn:1", "a", "1");
    toTest.writeEndElement();
    AttributesImpl a = new AttributesImpl();
    a.addAttribute("urn:1", "a", "a", "", "1");
    verify(actual).startElement(eq(""), eq("e"), eq("e"), eqAttr(a));
  }

  @Test
  public void attributesWithPrefixAndUriDelegateToContentHandler()
      throws Exception {
    toTest.writeStartElement("e");
    toTest.writeAttribute("p", "urn:1", "a", "1");
    toTest.writeEndElement();
    AttributesImpl a = new AttributesImpl();
    a.addAttribute("urn:1", "a", "p:a", "", "1");
    verify(actual).startElement(eq(""), eq("e"), eq("e"), eqAttr(a));
  }

  @Test
  public void attributeAfterFlushNotLost() throws Exception {
    toTest.writeStartElement("e");
    toTest.writeAttribute("p", "urn:1", "a", "1");
    toTest.flush();
    toTest.writeAttribute("b", "urn:2", "b", "2");
    toTest.writeEndElement();
    AttributesImpl a = new AttributesImpl();
    a.addAttribute("urn:1", "a", "p:a", "", "1");
    a.addAttribute("urn:2", "b", "b:b", "", "2");
    verify(actual).startElement(eq(""), eq("e"), eq("e"), eqAttr(a));
  }

  @Test
  public void charactersDelegateToContentHandler() throws Exception {
    char[] text = "foobar".toCharArray();
    toTest.writeCharacters(text, 2, 4);
    verify(actual).characters(text, 2, 4);
  }

  @Test
  public void charactersAsStringDelegateToContentHandler() throws Exception {
    String text = "a";
    toTest.writeCharacters(text);
    verify(actual).characters(text.toCharArray(), 0, text.length());
  }

  @Test
  public void prefixLookup() throws Exception {
    toTest.setPrefix("a", "urn:1");
    assertEquals("a", toTest.getPrefix("urn:1"));
  }

  @Test
  public void setPrefixWithoutElementDoesntSetAsDefault() throws Exception {
    toTest.setPrefix("a", "urn:1");
    toTest.writeStartElement("e");
    toTest.writeEndElement();
    verify(actual).startElement(eq(""), eq("e"), eq("e"), noAttrs());
  }

  @Test
  public void setPrefixWithoutElementAppliesToRoot() throws Exception {
    toTest.setPrefix("a", "urn:1");
    toTest.writeStartElement("urn:1", "e");
    toTest.writeEndElement();
    verify(actual).startElement(eq("urn:1"), eq("e"), eq("a:e"), noAttrs());
  }

  @Test
  public void setPrefixWithinElementAffectsChild() throws Exception {
    toTest.writeStartElement("a");
    toTest.setPrefix("p", "urn:1");
    toTest.writeStartElement("urn:1", "b");
    toTest.writeEndElement();
    toTest.writeEndElement();
    verify(actual).startElement(eq("urn:1"), eq("b"), eq("p:b"), noAttrs());
  }

  @Test
  public void setPrefixWithinElementLosesScope() throws Exception {
    toTest.setPrefix("z", "urn:1");
    toTest.writeStartElement("a");
    toTest.setPrefix("y", "urn:1");
    toTest.writeEndElement();
    toTest.writeStartElement("urn:1", "b");
    toTest.writeEndElement();
    verify(actual).startElement(eq("urn:1"), eq("b"), eq("z:b"), noAttrs());
  }

  @Test
  public void emptyElementDelegatesToContentHandler() throws Exception {
    toTest.writeEmptyElement("a");
    verify(actual).startElement(eq(""), eq("a"), eq("a"), noAttrs());
    verify(actual).endElement(eq(""), eq("a"), eq("a"));
  }

  @Test
  public void emptyElementWithUriDelegatesToContentHandler()
      throws Exception {
    toTest.writeEmptyElement("urn:2", "b");
    verify(actual).startElement(eq("urn:2"), eq("b"), eq("b"), noAttrs());
    verify(actual).endElement(eq("urn:2"), eq("b"), eq("b"));
  }

  @Test
  public void emptyElementWithPrefixAndUriDelegatesToContentHandler()
      throws Exception {
    toTest.writeEmptyElement("pee", "c", "urn:3");
    verify(actual).startElement(eq("urn:3"), eq("c"), eq("pee:c"), noAttrs());
    verify(actual).endElement(eq("urn:3"), eq("c"), eq("pee:c"));
  }

  @Test
  public void writeDefaultNamespaceBindsUri() throws Exception {
    toTest.writeStartElement("e");
    toTest.writeDefaultNamespace("urn:1");
    toTest.writeStartElement("f");
    toTest.writeEndElement();
    verify(actual).startElement(eq("urn:1"), eq("f"), eq("f"), noAttrs());
  }

  @Test
  public void writeDefaultNamespaceOnRootThrows() throws Exception {
    shouldFail.expect(IllegalStateException.class);
    toTest.writeDefaultNamespace("urn:1");
  }

  @Test
  public void writeDefaultNamespaceInContentThrows() throws Exception {
    toTest.writeStartElement("e");
    toTest.writeCharacters("sdf");
    shouldFail.expect(IllegalStateException.class);
    toTest.writeDefaultNamespace("urn:1");
  }

  @Test
  public void writeNamespaceOnRootThrows() throws Exception {
    shouldFail.expect(IllegalStateException.class);
    toTest.writeNamespace("n", "urn:1");
  }

  @Test
  public void writeNamespaceInContentThrows() throws Exception {
    toTest.writeStartElement("e");
    toTest.writeCharacters("sdf");
    shouldFail.expect(IllegalStateException.class);
    toTest.writeNamespace("n", "urn:1");
  }

  // TODO flush and close
  // TODO CDATA, procesing instructions, comments, etc
  // TODO
  // http://docs.oracle.com/javase/7/docs/
  // ...api/javax/xml/stream/XMLStreamWriter.html
  // TODO consistency between XMLStreamWriterImpl and SAXAsStAX

  private Attributes noAttrs() {
    return Matchers.argThat(new BaseMatcher<Attributes>() {
      @Override
      public boolean matches(Object item) {
        Attributes a = (Attributes) item;
        return a.getLength() == 0;
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Your compiler <3 you.");
      }
    });
  }

  private Attributes eqAttr(final AttributesImpl expected) {
    return Matchers.argThat(new BaseMatcher<Attributes>() {
      @Override
      public boolean matches(Object item) {
        AttributesImpl a = (AttributesImpl) item;
        int lengthA = a.getLength();
        int lengthExpected = expected.getLength();
        for (int i = 0; i < lengthA; i++) {
          if (!expected.getURI(i).equals(a.getURI(i))
              || !expected.getQName(i).equals(a.getQName(i))
              || !expected.getLocalName(i).equals(a.getLocalName(i))
              || !expected.getValue(i).equals(a.getValue(i))) {
            System.out.println(expected.getURI(i) + " vs. " + a.getURI(i));
            System.out.println(expected.getQName(i) + " vs. " + a.getQName(i));
            System.out.println(expected.getLocalName(i)
                + " vs. " + a.getLocalName(i));
            System.out.println(expected.getValue(i) + " vs. " + a.getValue(i));
            return false;
          }
        }
        return lengthA == lengthExpected;
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Your compiler <3 you.");
      }
    });
  }
}
