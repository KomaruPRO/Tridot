package pro.komaru.tridot.core.anno;

import pro.komaru.tridot.core.entity.ecs.EntityComp;
import pro.komaru.tridot.core.struct.enums.GameSide;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class CompAnnotations {
    /**
     * Specifies the side on which the component should be added.
     * This annotation is used to indicate whether the component is client-side, server-side, or both.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface ApplyOn {
        GameSide value();
    }
    /**
     * Specifies the dependencies of this component.
     * Components that are listed here will be processed before this component.
     * This annotation is used to indicate which components must be present before this one can be added.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface DependsOn {
        Class<? extends EntityComp>[] value();
    }
    /**
     * Indicates that the component should ignore dependencies' sides.
     * This annotation is used to allow components to be added without checking their dependencies or the game side.
     * Use with caution, as it can lead to unexpected behavior if not managed properly.
     * <p>
     * @implNote You can not use this annotation on component which side isn't BOTH
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface IgnoreDependenciesSide {
    }
}
