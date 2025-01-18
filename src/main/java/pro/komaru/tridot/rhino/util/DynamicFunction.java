package pro.komaru.tridot.rhino.util;

import pro.komaru.tridot.rhino.BaseFunction;
import pro.komaru.tridot.rhino.Context;
import pro.komaru.tridot.rhino.Scriptable;
import org.jetbrains.annotations.Nullable;

/**
 * @author LatvianModder
 */
public class DynamicFunction extends BaseFunction {
	private final Callback function;

	public DynamicFunction(Callback f) {
		function = f;
	}

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		return function.call(args);
	}

	@FunctionalInterface
	public interface Callback {
		@Nullable
		Object call(Object[] args);
	}
}