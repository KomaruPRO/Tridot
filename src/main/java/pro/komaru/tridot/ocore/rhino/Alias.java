package pro.komaru.tridot.ocore.rhino;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Alias {
    String value() default "";
}
