package koka.util.io.guava;

import java.io.InputStream;
import java.io.OutputStream;

import com.google.common.io.InputSupplier;
import com.google.common.io.OutputSupplier;

/**
 * Location that holds data and can be read from and written to.
 */
public interface Storage
    extends InputSupplier<InputStream>, OutputSupplier<OutputStream> {}
