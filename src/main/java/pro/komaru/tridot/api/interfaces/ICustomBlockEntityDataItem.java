package pro.komaru.tridot.api.interfaces;

import net.minecraft.nbt.*;
import net.minecraft.world.item.*;

public interface ICustomBlockEntityDataItem{
    CompoundTag getCustomBlockEntityData(ItemStack stack, CompoundTag nbt);
}
