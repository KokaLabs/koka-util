package koka.util.net;

import static org.fest.assertions.api.Assertions.assertThat;

import java.net.URL;
import java.net.URLConnection;

import org.junit.Test;

public class URLsTest {
  @Test(expected = IllegalArgumentException.class)
  public void create_InvalidThrows() {
    URLs.create("!");
  }

  @Test
  public void create_Okay() {
    assertThat(URLs.create("file://foo")).isNotNull();
  }

  @Test
  public void openConnectionWithTimeout_CanGetSameValue() throws Exception {
    int expected = 1;
    URL to = new URL("file:///foo/");
    URLConnection c = URLs.openConnectionWithConnectTimeout(to, expected);
    assertThat(c.getConnectTimeout()).isEqualTo(expected);
  }
}
