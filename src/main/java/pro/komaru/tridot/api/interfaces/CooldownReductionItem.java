package pro.komaru.tridot.api.interfaces;

import net.minecraft.world.item.*;
import pro.komaru.tridot.common.registry.EnchantmentsRegistry;

public interface CooldownReductionItem{
    default int getCooldownReduction(int cooldown, ItemStack stack) {
        int level = stack.getEnchantmentLevel(EnchantmentsRegistry.OVERDRIVE.get());
        float modifier = 0.15f + ((level - 1) * 0.05f);
        return Math.max(0, Math.round(cooldown * modifier));
    }
}
