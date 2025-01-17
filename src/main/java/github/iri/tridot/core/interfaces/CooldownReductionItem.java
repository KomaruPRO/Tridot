package github.iri.tridot.core.interfaces;

import github.iri.tridot.registry.*;
import net.minecraft.world.item.*;

public interface CooldownReductionItem{
    default int getCooldownReduction(ItemStack stack) {
        return stack.getEnchantmentLevel(EnchantmentsRegistry.OVERDRIVE.get()) * 5;
    }
}
