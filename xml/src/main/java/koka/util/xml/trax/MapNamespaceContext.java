package koka.util.xml.trax;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import com.google.common.base.Objects;
import com.google.common.collect.Iterators;

public class MapNamespaceContext implements NamespaceContext {
  private final Map<String, String> namespaceUriToPrefix;

  public MapNamespaceContext(Map<String, String> namespaceUriToPrefix) {
    this.namespaceUriToPrefix = namespaceUriToPrefix;
  }

  @Override
  public String getNamespaceURI(String prefix) {
    for (Entry<String, String> e : namespaceUriToPrefix.entrySet()) {
      if (e.getValue().equals(prefix)) {
        return e.getKey();
      }
    }
    return XMLConstants.NULL_NS_URI;
  }

  @Override
  public String getPrefix(String namespaceURI) {
    return namespaceUriToPrefix.containsKey(namespaceURI)
        ? namespaceUriToPrefix.get(namespaceURI)
        : XMLConstants.DEFAULT_NS_PREFIX;
  }

  @Override
  public Iterator<String> getPrefixes(String namespaceURI) {
    return namespaceUriToPrefix.containsKey(namespaceURI)
        ? Iterators.singletonIterator(namespaceUriToPrefix.get(namespaceURI))
        : Iterators.<String> emptyIterator();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(namespaceUriToPrefix.hashCode());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if ((obj != null) && getClass().equals(obj.getClass())) {
      final MapNamespaceContext other = (MapNamespaceContext) obj;
      return Objects.equal(namespaceUriToPrefix, other.namespaceUriToPrefix);
    }
    return false;
  }
}
