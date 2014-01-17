package koka.util.net;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.transform.stream.StreamSource;

import org.xml.sax.InputSource;

/**
 * Extension methods on a {@link URL}.
 */
public final class URLs {
  private URLs() {}

  /**
   * Same as {@link URL#URL(String)}, but with the exception wrapped.
   * 
   * @throws IllegalArgumentException unknown protocol
   */
  public static URL create(String spec) {
    return create(null, spec);
  }

  /**
   * Same as {@link URL#URL(URL, String)}, but with the exception wrapped.
   * 
   * @throws IllegalArgumentException protocol not found or unknown
   */
  public static URL create(URL context, String spec) {
    try {
      return new URL(context, spec);
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException(
          String.format("Bad context(%s) with spec(%s)", context, spec),
          e);
    }
  }

  public static StreamSource asStreamSource(URL location) {
    return new StreamSource(toSystemId(location));
  }

  public static InputSource asInputSource(URL location) {
    return new InputSource(toSystemId(location));
  }

  private static String toSystemId(URL location) {
    return URI.create(location.toString()).toASCIIString();
  }

  public static URLConnection openConnectionWithConnectTimeout(
      URL to, int timeoutAfterMillis) throws IOException {
    URLConnection c = to.openConnection();
    c.setConnectTimeout(timeoutAfterMillis);
    return c;
  }

  public static void checkCanConnect(URLConnection to) throws IOException {
    to.connect();
    if (to instanceof HttpURLConnection) {
      HttpURLConnection http = (HttpURLConnection) to;
      http.disconnect();
    }
  }
}
