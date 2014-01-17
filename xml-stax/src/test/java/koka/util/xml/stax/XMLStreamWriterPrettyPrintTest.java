package koka.util.xml.stax;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import javax.xml.stream.XMLStreamWriter;

import org.junit.Test;
import org.mockito.InOrder;

public class XMLStreamWriterPrettyPrintTest {
  private final XMLStreamWriter delegate = mock(XMLStreamWriter.class);
  private final String indent = "\t";

  private final XMLStreamWriterPrettyPrint toTest =
      new XMLStreamWriterPrettyPrint(delegate, indent);

  private final InOrder next = inOrder(delegate);

  private XMLStreamWriter nextVerify() {
    return next.verify(delegate);
  }

  @Test
  public void write_JustRootElement_NoAdditionalEvents() throws Exception {
    toTest.writeStartDocument();
    toTest.writeStartElement("foo");
    toTest.writeEndElement();
    toTest.writeEndDocument();

    nextVerify().writeStartDocument();
    nextVerify().writeStartElement("foo");
    nextVerify().writeEndElement();
    nextVerify().writeEndDocument();
    next.verifyNoMoreInteractions();
  }

  @Test
  public void write_MultipleChildren_NewlineBetweenThem() throws Exception {
    toTest.writeStartDocument();
    toTest.writeStartElement("root");

    toTest.writeStartElement("child1");
    toTest.writeEndElement();

    toTest.writeStartElement("child2");
    toTest.writeEndElement();

    toTest.writeEndElement();
    toTest.writeEndDocument();

    nextVerify().writeStartElement("child1");
    nextVerify().writeEndElement();
    nextVerify().writeCharacters(XMLStreamWriterPrettyPrint.NEW_LINE);
    nextVerify().writeStartElement("child2");
  }

  @Test
  public void write_RootWithChild_IndentsChild() throws Exception {
    toTest.writeStartDocument();
    toTest.writeStartElement("parent");
    toTest.writeStartElement("child");
    writeEndElement(toTest, 2);
    toTest.writeEndDocument();

    nextVerify().writeStartElement("parent");
    nextVerify().writeCharacters(indent);
    nextVerify().writeStartElement("child");
  }

  @Test
  public void write_EmptyChild_IdentsEmpty() throws Exception {
    toTest.writeStartDocument();
    toTest.writeStartElement("root");
    toTest.writeEmptyElement("emptyChild");
    toTest.writeEndElement();
    toTest.writeEndDocument();

    nextVerify().writeStartElement("root");
    nextVerify().writeCharacters(indent);
    nextVerify().writeEmptyElement("emptyChild");
  }

  @Test
  public void write_EmptyChildNamespace_IdentsEmpty() throws Exception {
    toTest.writeStartDocument();
    toTest.writeStartElement("root");
    toTest.writeEmptyElement("ns", "emptyChild");
    toTest.writeEndElement();
    toTest.writeEndDocument();

    nextVerify().writeStartElement("root");
    nextVerify().writeCharacters(indent);
    nextVerify().writeEmptyElement("ns", "emptyChild");
  }

  @Test
  public void write_EmptyChildPrefixAndNamespace_IdentEmpty() throws Exception {
    toTest.writeStartDocument();
    toTest.writeStartElement("root");
    toTest.writeEmptyElement("pre", "emptyChild", "ns");
    toTest.writeEndElement();
    toTest.writeEndDocument();

    nextVerify().writeStartElement("root");
    nextVerify().writeCharacters(indent);
    nextVerify().writeEmptyElement("pre", "emptyChild", "ns");
  }

  @Test
  public void write_ThreeLevels_IdentsEndElementOfSecond() throws Exception {
    toTest.writeStartElement("root");
    toTest.writeStartElement("two");
    toTest.writeEmptyElement("three");
    toTest.writeEndElement();

    nextVerify().writeEmptyElement("three");
    nextVerify().writeCharacters(indent);
    nextVerify().writeEndElement();
    next.verifyNoMoreInteractions();
  }

  @Test
  public void write_DeeplyNestedWithMixedUsage_StillIdentsLevelMinusOne()
      throws Exception {
    toTest.writeStartDocument();
    toTest.writeStartElement("one");
    toTest.writeStartElement("ns2", "two");
    toTest.writeStartElement("prefix3", "three", "ns3");
    toTest.writeStartElement("ns4", "four");
    toTest.writeStartElement("five");
    toTest.writeStartElement("six");
    toTest.writeStartElement("seven");
    writeEndElement(toTest, 7);
    toTest.writeEndDocument();

    nextVerify().writeStartElement("six");
    next.verify(delegate, times(6)).writeCharacters(indent);
    nextVerify().writeStartElement("seven");
  }

  @Test
  public void childWithContentFollowedBySiblingWithoutContent_ThatSiblingShouldNotBeIndented()
      throws Exception {
    toTest.writeStartElement("root");
    toTest.writeStartElement("childWithContent");
    toTest.writeEmptyElement("theContent");
    toTest.writeEndElement();
    toTest.writeStartElement("siblingWithoutContent");
    toTest.writeEndElement();

    nextVerify().writeStartElement("siblingWithoutContent");
    next.verify(delegate, never()).writeCharacters(indent);
  }

  @Test
  public void emptyRootShouldNotCauseException() throws Exception {
    toTest.writeEmptyElement("root");
  }

  private static void writeEndElement(XMLStreamWriter w, int times)
      throws Exception {
    for (int i = 0; i < times; ++i) {
      w.writeEndElement();
    }
  }
}
