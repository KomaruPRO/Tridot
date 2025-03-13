package pro.komaru.tridot.ocore.interfaces;

import net.minecraft.world.item.*;
import pro.komaru.tridot.oregistry.*;

public interface CooldownReductionItem{
    default int getCooldownReduction(ItemStack stack) {
        return stack.getEnchantmentLevel(EnchantmentsRegistry.OVERDRIVE.get()) * 5;
    }
}
