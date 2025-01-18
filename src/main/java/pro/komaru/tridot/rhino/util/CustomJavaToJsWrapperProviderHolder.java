package pro.komaru.tridot.rhino.util;

import org.jetbrains.annotations.*;

import java.util.function.*;

public record CustomJavaToJsWrapperProviderHolder<T>(Predicate<T> predicate, CustomJavaToJsWrapperProvider<T> provider) {
	public record PredicateFromClass<T>(Class<T> type) implements Predicate<T> {
		@Override
		public boolean test(T object) {
			return type.isAssignableFrom(object.getClass());
		}
	}

	@Nullable
	public CustomJavaToJsWrapperProvider<T> create(T object) {
		if (predicate.test(object)) {
			return provider;
		}

		return null;
	}
}
