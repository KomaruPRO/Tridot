package pro.komaru.tridot.api.interfaces;

import net.minecraft.world.item.*;
import pro.komaru.tridot.common.registry.EnchantmentsRegistry;

public interface CooldownReductionItem{
    default int getCooldownReduction(ItemStack stack) {
        return stack.getEnchantmentLevel(EnchantmentsRegistry.OVERDRIVE.get()) * 5;
    }
}
