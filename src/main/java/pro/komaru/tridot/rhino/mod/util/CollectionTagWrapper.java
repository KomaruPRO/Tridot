package pro.komaru.tridot.rhino.mod.util;

import net.minecraft.nbt.*;
import pro.komaru.tridot.rhino.*;
import pro.komaru.tridot.rhino.util.*;

public record CollectionTagWrapper(CollectionTag<?> tag) implements CustomJavaToJsWrapper {
	@Override
	public Scriptable convertJavaToJs(Context cx, Scriptable scope, Class<?> staticType) {
		return new NativeJavaList(cx, scope, tag, tag, Tag.class, NBTUtils.VALUE_UNWRAPPER);
	}
}
