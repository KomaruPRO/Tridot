package pro.komaru.tridot.api.interfaces;

import net.minecraft.world.item.*;
import pro.komaru.tridot.registry.*;

public interface CooldownReductionItem{
    default int getCooldownReduction(ItemStack stack) {
        return stack.getEnchantmentLevel(EnchantmentsRegistry.OVERDRIVE.get()) * 5;
    }
}
