package koka.util.io.guava;

import static com.google.common.base.Charsets.UTF_8;
import koka.util.io.guava.PipedFromOutput;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Random;

import com.google.common.annotations.Beta;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closer;
import com.google.common.io.FileBackedOutputStream;
import com.google.common.io.InputSupplier;
import com.google.common.io.OutputSupplier;

/**
 * Assists in generating arbitrary random data in a useful format for tests in
 * an entirely streaming way.
 */
@Beta
public abstract class Bytes implements InputSupplier<InputStream> {
  private static final long SEED = 8L;

  public static Unsized random() {
    return random(SEED);
  }

  public static Unsized random(final long seed) {
    class EndlessRandom extends Bytes implements Unsized {
      private static final int MAX_BYTE = 0xFF;

      @Override
      public Bytes endless() {
        return this;
      }

      @Override
      public InputStream getInput() {
        return new InputStream() {
          private final Random byteGenerator = new Random(seed);

          @Override
          public int read(byte[] b, int off, int len) {
            byte[] tmp = new byte[len - off];
            byteGenerator.nextBytes(tmp);
            System.arraycopy(tmp, 0, b, off, len);
            return tmp.length;
          }

          @Override
          public int read(byte[] b) {
            byteGenerator.nextBytes(b);
            return b.length;
          }

          @Override
          public int read() {
            return byteGenerator.nextInt(MAX_BYTE + 1);
          }
        };
      }
    }
    return new EndlessRandom();
  }

  public static Bytes of(final InputSupplier<? extends InputStream> raw) {
    return new Bytes() {
      @Override
      public InputStream getInput() throws IOException {
        return raw.getInput();
      }
    };
  }

  public final Bytes limitedTo(long length) {
    return of(ByteStreams.slice(this, 0, length));
  }

  // Uncomment for optimization if commons-io is ever added 
//  /**
//   * faster implementation
//   */
//  private Bytes inBase64() {
//    final Bytes curr = this;
//    return of(new InputSupplier<InputStream>() {
//      @Override
//      public InputStream getInput() throws IOException {
//        boolean doEncode = true;
//        return new Base64InputStream(curr.getInput(), doEncode);
//      }
//    });
//  }

  public final Bytes in(final BaseEncoding as) {
//    if (as.equals(BaseEncoding.base64())) {
//      return this.inBase64();
//    }
    final Bytes curr = this;
    return of(new PipedFromOutput() {
      @Override
      protected void write(OutputStream to) throws IOException {
        OutputStream ignored = new FilterOutputStream(to) {
          @Override
          public void write(byte[] b) throws IOException {
            out.write(b);
          }

          @Override
          public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
          }

          @Override
          public void write(int b) throws IOException {
            out.write(b);
          }

          @Override
          public void close() {}
        };
        OutputStreamWriter out = new OutputStreamWriter(ignored, UTF_8);
        Closer c = Closer.create();
        try {
          ByteStreams.copy(curr, c.register(as.encodingStream(out)));
        } catch (Throwable e) {
          throw c.rethrow(e);
        } finally {
          c.close();
        }
      }
    });
  }

  public final void copyTo(OutputSupplier<? extends OutputStream> to)
      throws IOException {
    Closer c = Closer.create();
    try {
      copyTo(c.register(to.getOutput()));
    } catch (Throwable e) {
      throw c.rethrow(e);
    } finally {
      c.close();
    }
  }

  public void copyTo(OutputStream to) throws IOException {
    ByteStreams.copy(this, to);
  }

  @Beta
  final Bytes cacheToDisk() throws IOException {
    FileBackedOutputStream cache = new FileBackedOutputStream(1_000_000);
    ByteStreams.copy(this, cache);
    return Bytes.of(cache.getSupplier());
  }
  
  private Bytes() {}

  public interface Unsized {
    Bytes limitedTo(long length);

    Bytes endless();
  }
}
