package pro.komaru.tridot.rhino.util;

import pro.komaru.tridot.rhino.Context;
import pro.komaru.tridot.rhino.Scriptable;

@FunctionalInterface
public interface CustomJavaToJsWrapper {
	Scriptable convertJavaToJs(Context cx, Scriptable scope, Class<?> staticType);
}
