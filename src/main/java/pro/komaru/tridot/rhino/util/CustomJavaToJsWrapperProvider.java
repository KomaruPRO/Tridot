package pro.komaru.tridot.rhino.util;

import org.jetbrains.annotations.*;

@FunctionalInterface
public interface CustomJavaToJsWrapperProvider<T> {
	CustomJavaToJsWrapperProvider<?> NONE = object -> null;

	@Nullable
	CustomJavaToJsWrapper create(T object);
}
