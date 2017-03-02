package koka.util.io.guava;

import static com.google.common.base.Charsets.UTF_8;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Random;

import com.google.common.annotations.Beta;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteSource;
import com.google.common.io.Closer;
import com.google.common.io.FileBackedOutputStream;

/**
 * Assists in generating arbitrary random data in a useful format for tests in
 * an entirely streaming way.
 */
@Beta
public abstract class Bytes extends ByteSource {
  private static final long SEED = 8L;

  public static Bytes random() {
    return random(SEED);
  }

  public static Bytes random(final long seed) {
    class EndlessRandom extends Bytes {
      private static final int MAX_BYTE = 0xFF;

      @Override
      public InputStream openStream() {
        return new InputStream() {
          private final Random byteGenerator = new Random(seed);

          @Override
          public int read(byte[] b, int off, int len) {
            byte[] tmp = new byte[len];
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

  public static Bytes of(final ByteSource raw) {
    return new Bytes() {
      @Override
      public InputStream openStream() throws IOException {
        return raw.openStream();
      }
    };
  }
  
  @Override
  public Bytes slice(long offset, long length) {
    return of(super.slice(offset, length));
  }

  // Uncomment for optimization if commons-io is ever added
  // /**
  // * faster implementation
  // */
  // private Bytes inBase64() {
  // final Bytes curr = this;
  // return of(new InputSupplier<InputStream>() {
  // @Override
  // public InputStream getInput() throws IOException {
  // boolean doEncode = true;
  // return new Base64InputStream(curr.getInput(), doEncode);
  // }
  // });
  // }

  public final Bytes in(final BaseEncoding as) {
    // if (as.equals(BaseEncoding.base64())) {
    // return this.inBase64();
    // }
    final Bytes curr = this;
    return of(new PipedByteSource() {
      @Override
      protected void passThrough(OutputStream to) throws IOException {
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
          curr.copyTo(c.register(as.encodingStream(out)));
        } catch (Throwable e) {
          throw c.rethrow(e);
        } finally {
          c.close();
        }
      }
    });
  }

  @Beta
  public final Bytes cacheToDisk() throws IOException {
    FileBackedOutputStream cache = new FileBackedOutputStream(1_000_000);
    this.copyTo(cache);
    return Bytes.of(cache.asByteSource());
  }

  private Bytes() {}
}
