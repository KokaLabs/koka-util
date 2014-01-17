package koka.util.io.guava;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

abstract class Threading {
  static Threading freshExecutor() {
    return new Threading() {
      @Override
      public <V> Future<V> submit(Callable<V> task) {
        ThreadFactory tf = new ThreadFactoryBuilder().setDaemon(true).build();
        return Executors.newCachedThreadPool(tf).submit(task);
      }
    };
  }

  static Threading wrap(final ExecutorService actual) {
    return new Threading() {
      @Override
      public <V> Future<V> submit(Callable<V> task) {
        return actual.submit(task);
      }
    };
  }

  abstract <V> Future<V> submit(Callable<V> task);
}
