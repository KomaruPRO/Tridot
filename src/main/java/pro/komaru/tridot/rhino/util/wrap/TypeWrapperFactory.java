package pro.komaru.tridot.rhino.util.wrap;

import pro.komaru.tridot.rhino.*;

/**
 * @author LatvianModder
 */
@FunctionalInterface
public interface TypeWrapperFactory<T> {
	interface Simple<T> extends TypeWrapperFactory<T> {
		T wrapSimple(Object o);

		@Override
		default T wrap(Context cx, Object o) {
			return wrapSimple(o);
		}
	}

	T wrap(Context cx, Object o);
}
