package koka.util.net.resolver;

import static org.fest.assertions.api.Assertions.assertThat;

import java.net.URL;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class LenientURLResolverTest {
  @Rule
  public final ExpectedException shouldFail = ExpectedException.none();

  private final LenientURLResolver toTest = new LenientURLResolver();

  @Test
  public void resolve_BadBase_Throws() {
    String base = "bad_url";
    String href = "";

    shouldFail.expect(IllegalArgumentException.class);
    shouldFail.expectMessage(base);

    toTest.resolve(base, href);
  }

  @Test
  public void resolve_GoodBaseBadHref_Throws() {
    String base = "file:/valid";
    String href = "bad:/href";

    shouldFail.expect(IllegalArgumentException.class);
    shouldFail.expectMessage(base);
    shouldFail.expectMessage(href);

    toTest.resolve(base, href);
  }

  @Test
  public void resolve_RegularFile() {
    String base = "file:/a/b/";
    String href = "foo.txt";

    URL result = toTest.resolve(base, href);

    assertThat(result.toString()).isEqualTo(base + href);
  }

  @Test
  public void resolve_BaseContainsFile_StripsOffBaseFile() {
    String base = "file:/a/bar.txt";
    String href = "foo.txt";

    URL result = toTest.resolve(base, href);

    assertThat(result.toString()).isEqualTo("file:/a/foo.txt");
  }

  @Test
  public void resolve_BaseInJar() {
    String base = "jar:file:/fake.jar!/a/b/";
    String href = "foo.txt";

    URL result = toTest.resolve(base, href);

    assertThat(result.toString()).isEqualTo(base + href);
  }

  @Test
  public void resolve_AbsoluteHrefInJar_UsesHref() {
    String base = "file:/a/b.jar";
    String href = "jar:file:c/d.jar!/e/f.txt";

    URL result = toTest.resolve(base, href);

    assertThat(result.toString()).isEqualTo("jar:file:c/d.jar!/e/f.txt");
  }

  @Test
  public void resolve_EmptyHref_ReturnsBase() {
    String base = "jar:file:/fake.jar!/a/b/c/d.txt";
    String href = "";

    URL result = toTest.resolve(base, href);

    assertThat(result.toString()).isEqualTo(base);
  }
}
