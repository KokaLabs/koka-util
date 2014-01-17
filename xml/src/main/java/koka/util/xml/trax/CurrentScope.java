package koka.util.xml.trax;

import static com.google.common.collect.LinkedHashMultimap.create;
import static com.google.common.collect.Queues.newArrayDeque;
import static java.util.Arrays.asList;

import java.util.Deque;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;

final class CurrentScope {
  private final Deque<Scope> stack = newArrayDeque(asList(Scope.ofRoot()));

  static CurrentScope ofDocumentRoot() {
    return new CurrentScope();
  }

  private CurrentScope() {}

  String defaultUri() {
    return current().defaultUri();
  }

  void setDefaultUri(String uri) {
    current().setDefaultUri(uri);
  }

  String defaultUriPrefix() {
    return prefixFor(defaultUri());
  }

  String prefixFor(String uri) {
    for (Scope next : stack) {
      String maybe = next.firstPrefixMatching(uri);
      if (!maybe.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
        return maybe;
      }
    }
    return XMLConstants.DEFAULT_NS_PREFIX;
  }

  void addPrefix(String prefix, String uri) {
    current().addPrefix(prefix, uri);
  }

  Attributes getAttrs() {
    return current().attributes();
  }

  void addAttr(QName attribute, String value) {
    current().addAttr(attribute, value);
  }

  QName peek() {
    return current().ele();
  }

  QName popScope() {
    return stack.pop().ele();
  }

  void pushScope(QName element) {
    stack.push(Scope.of(element));
  }

  private Scope current() {
    return stack.peek();
  }

  boolean isAtDocumentRoot() {
    return current().isRoot();
  }

  private abstract static class Scope {
    private static Scope ofRoot() {
      return new Scope() {
        @Override
        QName ele() {
          throw new IllegalStateException(
              "Scope underflow: access to root QName is not permitted.");
        }

        @Override
        void addAttr(QName attribute, String value) {
          throw new IllegalStateException(
              "Scope underflow: cannot add attributes to root");
        }

        @Override
        boolean isRoot() {
          return true;
        }
      };
    }

    private static Scope of(final QName element) {
      return new Scope() {
        @Override
        QName ele() {
          return element;
        }

        @Override
        boolean isRoot() {
          return false;
        }

        @Override
        void addAttr(QName attribute, String value) {
          attrs().put(attribute, value);
        }
      };
    }

    private final Map<QName, String> attrs = Maps.newHashMap();
    private String defaultUri = XMLConstants.NULL_NS_URI;
    private final LinkedHashMultimap<String, String> uriToPrefixes = create();

    abstract QName ele();

    abstract boolean isRoot();

    Attributes attributes() {
      return new AdaptedAttributes(attrs());
    }

    private static final class AdaptedAttributes extends AttributesImpl {
      private static final String WTF_IS_TYPE = "";

      public AdaptedAttributes(Map<QName, String> from) {
        for (Entry<QName, String> attr : from.entrySet()) {
          QName key = attr.getKey();
          addAttribute(key.getNamespaceURI(), key.getLocalPart(), qualify(key),
              WTF_IS_TYPE, attr.getValue());
        }
      }
    }

    Map<QName, String> attrs() {
      return attrs;
    }

    String defaultUri() {
      return defaultUri;
    }

    void setDefaultUri(String uri) {
      defaultUri = uri;
    }

    abstract void addAttr(QName attribute, String value);

    private String firstPrefixMatching(String uri) {
      return uriToPrefixes.containsKey(uri)
          ? uriToPrefixes.get(uri).iterator().next()
          : XMLConstants.DEFAULT_NS_PREFIX;
    }

    private void addPrefix(String prefix, String uri) {
      uriToPrefixes.put(uri, prefix);
    }
  }

  @VisibleForTesting
  static String qualify(QName qn) {
    if (qn.getPrefix().equals(XMLConstants.DEFAULT_NS_PREFIX)) {
      return qn.getLocalPart();
    }
    return qn.getPrefix() + ":" + qn.getLocalPart();
  }
}
