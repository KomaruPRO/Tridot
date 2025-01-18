package github.iri.tridot.rhino.mod.util;

import github.iri.tridot.rhino.Context;
import github.iri.tridot.rhino.NativeJavaMap;
import github.iri.tridot.rhino.Scriptable;
import github.iri.tridot.rhino.util.CustomJavaToJsWrapper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public record CompoundTagWrapper(CompoundTag tag) implements CustomJavaToJsWrapper {
	@Override
	public Scriptable convertJavaToJs(Context cx, Scriptable scope, Class<?> staticType) {
		return new NativeJavaMap(cx, scope, tag, NBTUtils.accessTagMap(tag), Tag.class, NBTUtils.VALUE_UNWRAPPER);
	}
}
