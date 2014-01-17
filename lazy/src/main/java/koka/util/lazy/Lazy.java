package koka.util.lazy;

/**
 * A value whose creation should be delayed until it is actually needed.
 * Multiple retrievals should return the same instance (unless not thread safe).
 */
public abstract class Lazy<V> {
  /**
   * Sacrifices correctness for speed. <br>
   * The underlying Creation may get called multiple times, so this should only
   * be used if it's okay to have different instances or if only using 1 thread.
   */
  public static <V> Lazy<V> nonThreadSafe(Creation<V> obtainFrom) {
    return new NonThreadSafe<V>(obtainFrom);
  }

  private static class NonThreadSafe<T> extends Lazy<T> {
    private Lazy<T> allowGarbageCollectionOfOriginal;

    NonThreadSafe(final Creation<T> original) {
      this.allowGarbageCollectionOfOriginal = new Lazy<T>() {
        @Override
        public T get() {
          T instance = original.perform();
          allowGarbageCollectionOfOriginal = known(instance);
          return original.perform();
        }
      };
    }

    @Override
    public T get() {
      return allowGarbageCollectionOfOriginal.get();
    }
  }

  /**
   * Always return the given existing instance.
   */
  public static <T> Lazy<T> known(final T instance) {
    return new Lazy<T>() {
      @Override
      public T get() {
        return instance;
      }
    };
  }

  private Lazy() {}

  /**
   * Create a new instance or reuse an existing instance.
   */
  public abstract V get();
}
