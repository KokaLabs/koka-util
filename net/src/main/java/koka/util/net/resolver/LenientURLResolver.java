package koka.util.net.resolver;

import java.net.URL;

import koka.util.net.URLs;

/**
 * Lenient resolution against base. <br>
 * E.G. if base points to a file, will resolve against that file's parent
 * directory.
 */
public class LenientURLResolver implements URLResolver {
  @Override
  public URL resolve(String base, String href) {
    URL baseUrl = URLs.create(base);
    if (href.isEmpty()) {
      return baseUrl;
    }
    return URLs.create(baseUrl, href);
  }
}
