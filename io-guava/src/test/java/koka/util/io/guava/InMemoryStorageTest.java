package koka.util.io.guava;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.google.common.io.ByteStreams;

public class InMemoryStorageTest {
  private final InMemoryStorage toTest = new InMemoryStorage();

  @Test
  public void readAndWriteToSelf_RemainsEmpty() throws Exception {
    ByteStreams.copy(toTest, toTest);

    assertThat(toTest.getInput().read()).isEqualTo(-1);
  }

  @Test
  public void writtenData_IsReadOutTheSame() throws Exception {
    byte[] bytes = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

    ByteStreams.write(bytes, toTest);

    byte[] read = ByteStreams.toByteArray(toTest);
    assertThat(read).isEqualTo(bytes);
  }

  @Test
  public void writeMultipleTimes_AppendsDataToEnd() throws Exception {
    byte[] one = { 1 };
    byte[] two = { 2 };

    ByteStreams.write(one, toTest);
    ByteStreams.write(two, toTest);

    byte[] read = ByteStreams.toByteArray(toTest);
    assertThat(read).isEqualTo(new byte[] { 1, 2 });
  }
}
