package koka.util.xml.transform;

import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.URIResolver;

import koka.util.net.URLs;
import koka.util.net.resolver.URLResolver;

import com.google.common.annotations.VisibleForTesting;

public class ProperlyHandleResourcesInArchives implements URIResolver {
  private final URLResolver toUrl;

  public ProperlyHandleResourcesInArchives(URLResolver toUrl) {
    this.toUrl = toUrl;
  }

  @Override
  public Source resolve(String href, String base) {
    return toSource(toUrl.resolve(base, href));
  }

  @VisibleForTesting
  Source toSource(URL location) {
    return URLs.asStreamSource(location);
  }
}
