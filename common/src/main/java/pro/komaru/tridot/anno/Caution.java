package pro.komaru.tridot.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD, ElementType.CONSTRUCTOR})
public @interface Caution {
    String reason();
}
