package pro.komaru.tridot.rhino.util;

import pro.komaru.tridot.rhino.*;

@FunctionalInterface
public interface CustomJavaToJsWrapper {
	Scriptable convertJavaToJs(Context cx, Scriptable scope, Class<?> staticType);
}
