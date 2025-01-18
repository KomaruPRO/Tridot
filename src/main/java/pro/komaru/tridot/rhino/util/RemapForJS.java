package pro.komaru.tridot.rhino.util;

import java.lang.annotation.*;

/**
 * Allows you to change field or method name
 *
 * @author LatvianModder
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface RemapForJS {
	String value();
}
