package koka.util.xml.transform;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Map;

import javax.xml.transform.Transformer;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class TransformerUtilTest {
  @Test
  public void setParameters_NonEmptyMap_DelegatesToTransformerSetParameter() {
    Transformer on = mock(Transformer.class);

    Map<String, ?> with = ImmutableMap.of(
        "1", "a",
        "2", "b",
        "3", "c");
    TransformerUtil.setParameters(on, with);

    verify(on).setParameter("1", "a");
    verify(on).setParameter("2", "b");
    verify(on).setParameter("3", "c");
  }
}
