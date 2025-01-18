package pro.komaru.tridot.rhino.mod.util;

import net.minecraft.nbt.*;
import pro.komaru.tridot.rhino.*;
import pro.komaru.tridot.rhino.util.*;

public record CompoundTagWrapper(CompoundTag tag) implements CustomJavaToJsWrapper {
	@Override
	public Scriptable convertJavaToJs(Context cx, Scriptable scope, Class<?> staticType) {
		return new NativeJavaMap(cx, scope, tag, NBTUtils.accessTagMap(tag), Tag.class, NBTUtils.VALUE_UNWRAPPER);
	}
}
