package pro.komaru.tridot.rhino.mod.util;

import pro.komaru.tridot.rhino.Context;
import pro.komaru.tridot.rhino.NativeJavaMap;
import pro.komaru.tridot.rhino.Scriptable;
import pro.komaru.tridot.rhino.util.CustomJavaToJsWrapper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public record CompoundTagWrapper(CompoundTag tag) implements CustomJavaToJsWrapper {
	@Override
	public Scriptable convertJavaToJs(Context cx, Scriptable scope, Class<?> staticType) {
		return new NativeJavaMap(cx, scope, tag, NBTUtils.accessTagMap(tag), Tag.class, NBTUtils.VALUE_UNWRAPPER);
	}
}
