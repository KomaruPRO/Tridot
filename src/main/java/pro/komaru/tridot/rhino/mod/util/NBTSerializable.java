package pro.komaru.tridot.rhino.mod.util;

import pro.komaru.tridot.rhino.util.RemapForJS;
import net.minecraft.nbt.Tag;

/**
 * @author LatvianModder
 */
public interface NBTSerializable {
	@RemapForJS("toNBT")
	Tag toNBTJS();
}