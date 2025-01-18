package pro.komaru.tridot.utilities.struct;

import net.minecraft.nbt.*;
import net.minecraft.server.level.*;
import net.minecraftforge.common.util.*;

public interface CapImpl extends INBTSerializable<CompoundTag> {
    void sync(ServerPlayer player);
}
