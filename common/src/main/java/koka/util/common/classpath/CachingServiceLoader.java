package koka.util.common.classpath;

import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentMap;

import koka.util.common.annotation.Internal;

import com.google.common.collect.Maps;

@Internal
public final class CachingServiceLoader {
  private static final ConcurrentMap<Class<?>, Iterable<?>> LOADED =
      Maps.newConcurrentMap();

  @SuppressWarnings("unchecked")
  public static <S> Iterable<S> load(Class<S> service) {
    if (!LOADED.containsKey(service)) {
      Iterable<S> load = ServiceLoader.load(service);
      Iterable<S> maybe = (Iterable<S>) LOADED.putIfAbsent(service, load);
      return maybe != null ? maybe : load;
    }
    return (Iterable<S>) LOADED.get(service);
  }

  private CachingServiceLoader() {}
}
