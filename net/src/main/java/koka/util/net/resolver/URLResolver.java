package koka.util.net.resolver;

import java.net.URL;

public interface URLResolver {
  /**
   * Intreprets a relative path.
   * 
   * @throws IllegalArgumentException invalid/unknown protocol in {@code base}.
   */
  URL resolve(String base, String href);
}
