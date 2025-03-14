package pro.komaru.tridot.core.rhino;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {
    String value() default "";
}
