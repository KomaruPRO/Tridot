package pro.komaru.tridot.rhino.mod.util;

import pro.komaru.tridot.rhino.Context;
import pro.komaru.tridot.rhino.Scriptable;
import pro.komaru.tridot.rhino.WrappedExecutable;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public record WrappedReflectionMethod(Method method) implements WrappedExecutable {
	public static WrappedExecutable of(Method method) {
		return method == null ? null : new WrappedReflectionMethod(method);
	}

	@Override
	public Object invoke(Context cx, Scriptable scope, Object self, Object[] args) throws Exception {
		return method.invoke(self, args);
	}

	@Override
	public boolean isStatic() {
		return Modifier.isStatic(method.getModifiers());
	}

	@Override
	public Class<?> getReturnType() {
		return method.getReturnType();
	}

	@Override
	@Nullable
	public Executable unwrap() {
		return method;
	}
}
