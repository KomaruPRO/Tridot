package github.iri.tridot.rhino.mod.util;

import github.iri.tridot.rhino.util.RemapForJS;
import net.minecraft.nbt.Tag;

/**
 * @author LatvianModder
 */
public interface NBTSerializable {
	@RemapForJS("toNBT")
	Tag toNBTJS();
}