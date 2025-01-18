package github.iri.tridot.rhino.mod.util;

import github.iri.tridot.rhino.Context;
import github.iri.tridot.rhino.NativeJavaList;
import github.iri.tridot.rhino.Scriptable;
import github.iri.tridot.rhino.util.CustomJavaToJsWrapper;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.Tag;

public record CollectionTagWrapper(CollectionTag<?> tag) implements CustomJavaToJsWrapper {
	@Override
	public Scriptable convertJavaToJs(Context cx, Scriptable scope, Class<?> staticType) {
		return new NativeJavaList(cx, scope, tag, tag, Tag.class, NBTUtils.VALUE_UNWRAPPER);
	}
}
