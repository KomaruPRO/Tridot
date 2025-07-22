package pro.komaru.tridot.api.interfaces;

import net.minecraft.world.item.*;
import pro.komaru.tridot.common.registry.EnchantmentsRegistry;

public interface CooldownReductionItem{
    default int getCooldownReduction(int cooldown, ItemStack stack) {
        int level = stack.getEnchantmentLevel(EnchantmentsRegistry.OVERDRIVE.get());
        float modifier = 1.0f - 0.1f * level;
        return Math.max(0, Math.round(cooldown * modifier));
    }
}
