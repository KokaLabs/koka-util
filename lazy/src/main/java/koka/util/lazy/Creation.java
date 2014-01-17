package koka.util.lazy;

/**
 * Event to instantiate a value.
 */
public interface Creation<V> {
  /**
   * Generate a fresh instance.
   */
  V perform();
}
