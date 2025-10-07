package pro.komaru.tridot.common.registry.item;

import net.minecraft.world.inventory.tooltip.*;
import net.minecraft.world.item.*;
import pro.komaru.tridot.util.struct.data.*;

public interface TooltipComponentItem{
    Seq<TooltipComponent> getTooltips(ItemStack pStack);
}