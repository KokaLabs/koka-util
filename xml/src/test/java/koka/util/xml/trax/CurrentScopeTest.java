package koka.util.xml.trax;

import static javax.xml.XMLConstants.DEFAULT_NS_PREFIX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xml.sax.Attributes;

public class CurrentScopeTest {
  CurrentScope toTest = CurrentScope.ofDocumentRoot();
  QName dummyEle = new QName("uriE", "ele", "e");
  QName dummyAttr = new QName("uriA", "attr", "a");

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  @Test
  public void firstScopeOnStackIsDocumentRoot() {
    Assert.assertTrue(toTest.isAtDocumentRoot());
  }

  @Test
  public void removingRootScopeThrows() {
    exception.expect(IllegalStateException.class);
    toTest.popScope();
  }

  @Test
  public void addingScopeCanBePeeked() {
    toTest.pushScope(dummyEle);
    assertEquals(dummyEle, toTest.peek());
  }

  @Test
  public void addingScopeCanBePopped() {
    toTest.pushScope(dummyEle);
    assertEquals(dummyEle, toTest.popScope());
  }

  @Test
  public void addingAndRemovingReturnsToRoot() {
    toTest.pushScope(dummyEle);
    toTest.popScope();
    assertTrue(toTest.isAtDocumentRoot());
  }

  @Test
  public void addingAttributeToCurrentScope() {
    toTest.pushScope(dummyEle);
    assertTrue(toTest.getAttrs().getLength() == 0);
    toTest.addAttr(dummyAttr, "val");
    assertTrue(toTest.getAttrs().getLength() == 1);
  }

  @Test
  public void addingAttributeIsolatedToCurrentScope() {
    toTest.pushScope(dummyEle);
    toTest.addAttr(dummyAttr, "val");
    toTest.popScope();
    assertTrue(toTest.getAttrs().getLength() == 0);
  }

  @Test
  public void attributesAllRetrieved() {
    toTest.pushScope(dummyEle);
    toTest.addAttr(dummyAttr, "val");
    Attributes attrs = toTest.getAttrs();
    assertTrue(attrs.getLength() == 1);
    assertTrue(attrs.getLocalName(0).equals(dummyAttr.getLocalPart()));
    assertTrue(attrs.getURI(0).equals(dummyAttr.getNamespaceURI()));
    assertTrue(attrs.getQName(0).equals(CurrentScope.qualify(dummyAttr)));
  }

  @Test
  public void addingAttributesToRootNotPermitted() {
    exception.expect(IllegalStateException.class);
    toTest.addAttr(dummyAttr, "val");
  }

  @Test
  public void unmappedPrefixMatchesXmlCosntant() {
    toTest.pushScope(dummyEle);
    assertTrue(toTest.prefixFor("urn:a").equals(DEFAULT_NS_PREFIX));
  }

  @Test
  public void addingPrefixToCurrentScope() {
    toTest.pushScope(dummyEle);
    toTest.addPrefix("a", "urn:a");
    assertTrue(toTest.prefixFor("urn:a").equals("a"));
  }

  @Test
  public void addingPrefixIsolatedToCurrentScope() {
    toTest.pushScope(dummyEle);
    toTest.addAttr(dummyAttr, "val");
    toTest.popScope();
    assertTrue(toTest.getAttrs().getLength() == 0);
  }

  @Test
  public void defaultUriOfRootMatchedXmlConstant() {
    assertEquals(XMLConstants.NULL_NS_URI, toTest.defaultUri());
  }

  @Test
  public void defaultUriPrefixOfRootMatchesXmlConstant() {
    assertEquals(XMLConstants.DEFAULT_NS_PREFIX, toTest.defaultUriPrefix());
  }

  @Test
  public void setDefaultUriOnRootIsPermitted() {
    toTest.setDefaultUri("urn:a");
    assertEquals("urn:a", toTest.defaultUri());
  }

  @Test
  public void setDefaultUriOnEle() {
    toTest.pushScope(dummyEle);
    toTest.setDefaultUri("urn:a");
    assertEquals("urn:a", toTest.defaultUri());
  }

  @Test
  public void setDefaultUriOnEleLosesScope() {
    toTest.pushScope(dummyEle);
    toTest.setDefaultUri("urn:a");
    toTest.popScope();
    assertEquals(XMLConstants.NULL_NS_URI, toTest.defaultUri());
  }

  @Test
  public void pushingElementWithoutPrefixDefaultsToXmlConstant() {
    toTest.pushScope(new QName("foo"));
    QName maybe = toTest.popScope();
    assertEquals(XMLConstants.DEFAULT_NS_PREFIX, maybe.getPrefix());
  }

  @Test
  public void pushingElementWithoutUriDefaultsToXmlConstant() {
    toTest.pushScope(new QName("foo"));
    QName maybe = toTest.popScope();
    assertEquals(XMLConstants.NULL_NS_URI, maybe.getNamespaceURI());
  }
}
