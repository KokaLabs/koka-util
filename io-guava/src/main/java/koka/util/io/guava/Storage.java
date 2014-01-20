package koka.util.io.guava;

import java.io.InputStream;
import java.io.OutputStream;

import com.google.common.io.InputSupplier;
import com.google.common.io.OutputSupplier;

/**
 * Location that holds data and can be read from and written to.
 * 
 * @deprecated No longer needed since {@link InMemoryStorage} is replaced with
 *             {@link InMemoryBytes}. To be removed once Guava removes
 *             {@link InputSupplier} in version 18.0
 */
@Deprecated
public interface Storage
    extends InputSupplier<InputStream>, OutputSupplier<OutputStream> {}
