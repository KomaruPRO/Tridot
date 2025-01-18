package github.iri.tridot.rhino.util;

import github.iri.tridot.rhino.Context;
import github.iri.tridot.rhino.Scriptable;

@FunctionalInterface
public interface ValueUnwrapper {
	ValueUnwrapper DEFAULT = (cx, scope, value) -> cx.getWrapFactory().wrap(cx, scope, value, value.getClass());

	Object unwrap(Context cx, Scriptable scope, Object value);
}
