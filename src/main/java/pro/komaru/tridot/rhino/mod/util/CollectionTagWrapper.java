package pro.komaru.tridot.rhino.mod.util;

import pro.komaru.tridot.rhino.Context;
import pro.komaru.tridot.rhino.NativeJavaList;
import pro.komaru.tridot.rhino.Scriptable;
import pro.komaru.tridot.rhino.util.CustomJavaToJsWrapper;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.Tag;

public record CollectionTagWrapper(CollectionTag<?> tag) implements CustomJavaToJsWrapper {
	@Override
	public Scriptable convertJavaToJs(Context cx, Scriptable scope, Class<?> staticType) {
		return new NativeJavaList(cx, scope, tag, tag, Tag.class, NBTUtils.VALUE_UNWRAPPER);
	}
}
