package koka.util.xml.transform;

import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.URIResolver;

import koka.util.net.URLs;

import com.google.common.annotations.VisibleForTesting;

public class ProperlyHandleResourcesInArchives implements URIResolver {
  @Override
  public Source resolve(String href, String base) {
    return toSource(resolveUrl(base, href));
  }

  private URL resolveUrl(String base, String href) {
    URL baseUrl = URLs.create(base);
    return href.isEmpty() ? baseUrl : URLs.create(baseUrl, href);
  }
  
  @VisibleForTesting
  Source toSource(URL location) {
    return URLs.asStreamSource(location);
  }
}
