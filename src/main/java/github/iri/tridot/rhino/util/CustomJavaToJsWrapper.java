package github.iri.tridot.rhino.util;

import github.iri.tridot.rhino.Context;
import github.iri.tridot.rhino.Scriptable;

@FunctionalInterface
public interface CustomJavaToJsWrapper {
	Scriptable convertJavaToJs(Context cx, Scriptable scope, Class<?> staticType);
}
