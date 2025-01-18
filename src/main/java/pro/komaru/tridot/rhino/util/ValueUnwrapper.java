package pro.komaru.tridot.rhino.util;

import pro.komaru.tridot.rhino.Context;
import pro.komaru.tridot.rhino.Scriptable;

@FunctionalInterface
public interface ValueUnwrapper {
	ValueUnwrapper DEFAULT = (cx, scope, value) -> cx.getWrapFactory().wrap(cx, scope, value, value.getClass());

	Object unwrap(Context cx, Scriptable scope, Object value);
}
