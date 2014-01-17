package koka.util.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import com.google.common.annotations.Beta;

/**
 * Annotates a program element that is more widely visible than otherwise
 * necessary. If element is used for testing and is more widely visible, use
 * Guava's {@link com.google.common.annotations.VisibleForTesting}.
 * <p>
 * Signifies that a public API (public class, method or field) is subject to
 * incompatible changes, or even removal, in a future release. An API bearing
 * this annotation is exempt from any compatibility guarantees made by its
 * containing library. Note that the presence of this annotation implies nothing
 * about the quality or performance of the API in question, only the fact that
 * it is not "API-frozen."
 */
@Target({
  ElementType.ANNOTATION_TYPE,
  ElementType.CONSTRUCTOR,
  ElementType.FIELD,
  ElementType.METHOD,
  ElementType.TYPE })
@Documented
@Beta
public @interface Internal {}
