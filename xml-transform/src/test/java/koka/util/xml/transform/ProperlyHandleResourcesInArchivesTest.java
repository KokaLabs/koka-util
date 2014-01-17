package koka.util.xml.transform;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

import javax.xml.transform.Source;

import koka.util.net.resolver.URLResolver;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProperlyHandleResourcesInArchivesTest {
  @Rule
  public final ExpectedException shouldFail = ExpectedException.none();

  private final URLResolver toUrl = mock(URLResolver.class);

  @Spy
  private final ProperlyHandleResourcesInArchives toTest =
      new ProperlyHandleResourcesInArchives(toUrl);

  @Test
  public void resolve_Valid_UsesReturnFromToSource() throws Exception {
    Source expected = mock(Source.class);

    willReturn(null).given(toUrl).resolve(null, null);
    willReturn(expected).given(toTest).toSource(null);

    Source result = toTest.resolve(null, null);

    assertThat(result).isSameAs(expected);
  }
}
