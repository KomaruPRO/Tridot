package pro.komaru.tridot.rhino.util;

import java.lang.annotation.*;

/**
 * Allows you to change field or method name on class scale with prefix
 *
 * @author LatvianModder
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Repeatable(RemapPrefixForJSRep.class)
public @interface RemapPrefixForJS {
	String value();
}
