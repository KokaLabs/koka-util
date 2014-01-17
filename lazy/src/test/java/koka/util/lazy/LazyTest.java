package koka.util.lazy;

import static org.fest.assertions.api.Assertions.assertThat;

import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

public class LazyTest {
  private static final int LARGE_NUM_CHANCES_TO_CATCH_THREADING_ISSUES = 2000;

  @Test
  public void actuallyUsesCreation() {
    Creation<String> foo = new Creation<String>() {
      @Override
      public String perform() {
        return "foo";
      }
    };

    Lazy<String> toTest = Lazy.nonThreadSafe(foo);

    assertThat(toTest.get()).isEqualTo("foo");
  }

  @Test
  public void multipleCallsReturnSameInstance() {
    Creation<Integer> incrementing = new Creation<Integer>() {
      private int i = 0;

      @Override
      public Integer perform() {
        return i++;
      }
    };

    Lazy<Integer> toTest = Lazy.nonThreadSafe(incrementing);
    toTest.get();
    assertThat(toTest.get()).isEqualTo(0);
  }

  @Test
  public void shouldLoseReferenceToProviderAfterOneRetrieval() {
    final WeakReference<Creation<Object>> r =
        new WeakReference<Creation<Object>>(new Creation<Object>() {
          @Override
          public Object perform() {
            return "foo";
          }
        });

    Lazy<Object> toTest = Lazy.nonThreadSafe(r.get());
    toTest.get();

    System.gc();

    assertThat(r.get()).isNull();
  }

  @Test
  public void shouldNotGetNullPointerExceptionDespiteNotBeingThreadSafe()
      throws Exception {
    int availableProcessors = Runtime.getRuntime().availableProcessors();
    Executor limited = Executors.newFixedThreadPool(availableProcessors);

    final CountDownLatch startLine = new CountDownLatch(1);
    final CountDownLatch goal = new CountDownLatch(availableProcessors);
    final AtomicBoolean failed = new AtomicBoolean();

    for (int t = 0; t < LARGE_NUM_CHANCES_TO_CATCH_THREADING_ISSUES; ++t) {
      final Lazy<Object> toTest = Lazy.nonThreadSafe(new Creation<Object>() {
        @Override
        public Object perform() {
          return "foo";
        }
      });

      Runnable command = new Runnable() {
        @Override
        public void run() {
          try {
            startLine.await();
            toTest.get();
            goal.countDown();
          } catch (Exception e) {
            failed.set(true);
          }
        }
      };

      for (int i = 0; i < availableProcessors; i++) {
        limited.execute(command);
      }

      startLine.countDown();

      goal.await(10, TimeUnit.MILLISECONDS);

      assertThat(failed.get()).isFalse();
    }
  }
}
