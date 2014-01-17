package koka.util.xml.transform;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.xml.sax.ContentHandler;

import com.google.common.collect.Lists;

public class TemplatesChainTest {
  private final Transformer transformer = mock(Transformer.class);

  private final SAXTransformerFactory factory =
      mock(SAXTransformerFactory.class);
  private final List<Templates> chain = Lists.newArrayList();

  private final TemplatesChain toTest = new TemplatesChain(factory, chain);

  @Before
  public void setup() throws Exception {
    given(factory.newTransformer()).willReturn(transformer);
  }

  @Test
  public void transform_NoXsls_PlainTransform() throws Exception {
    Source in = mock(Source.class);
    Result out = mock(Result.class);

    toTest.transform(in, out);

    verify(transformer).transform(in, out);
  }

  @Test
  public void transform_OneXsl_PlainTransform() throws Exception {
    TransformerHandler only = mock(TransformerHandler.class);

    setupFactoryToReturn(only);

    Source in = mock(Source.class);
    Result out = mock(Result.class);

    toTest.transform(in, out);

    assertThatLinkExistsBetween(only, out);
    assertThatTriedToTransform(in, only);
  }

  @Test
  public void transform_MultipleXsl_PlainTransform() throws Exception {
    TransformerHandler a = mock(TransformerHandler.class);
    TransformerHandler b = mock(TransformerHandler.class);
    TransformerHandler c = mock(TransformerHandler.class);

    setupFactoryToReturn(a, b, c);

    Source in = mock(Source.class);
    Result out = mock(Result.class);

    toTest.transform(in, out);

    assertThatLinkExistsBetween(a, b);
    assertThatLinkExistsBetween(b, c);
    assertThatLinkExistsBetween(c, out);
    assertThatTriedToTransform(in, a);
  }

  private void setupFactoryToReturn(
      TransformerHandler first, TransformerHandler... rest) throws Exception {
    chain.add(null);
    for (int i = 0; i < rest.length; ++i) {
      chain.add(null);
    }

    given(factory.newTransformerHandler((Templates) null)).willReturn(
        first,
        rest);
  }

  private void assertThatTriedToTransform(Source from, TransformerHandler to)
      throws Exception {
    ArgumentCaptor<Result> result = ArgumentCaptor.forClass(Result.class);
    verify(transformer).transform(eq(from), result.capture());
    assertThat(contentHandlerOf(result)).isEqualTo(to);
  }

  private static void assertThatLinkExistsBetween(
      TransformerHandler from, TransformerHandler to) {
    ArgumentCaptor<Result> result = captureSetResult(from);
    assertThat(contentHandlerOf(result)).isEqualTo(to);
  }

  private static void assertThatLinkExistsBetween(
      TransformerHandler from, Result to) {
    ArgumentCaptor<Result> result = captureSetResult(from);
    assertThat(result.getValue()).isEqualTo(to);
  }

  private static ArgumentCaptor<Result> captureSetResult(TransformerHandler from) {
    ArgumentCaptor<Result> result = ArgumentCaptor.forClass(Result.class);
    verify(from).setResult(result.capture());
    return result;
  }

  private static ContentHandler contentHandlerOf(ArgumentCaptor<Result> to) {
    Result value = to.getValue();
    SAXResult actually = (SAXResult) value;
    return actually.getHandler();
  }
}
