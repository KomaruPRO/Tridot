package github.iri.tridot.integration.common.curios;

import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;

public interface ICurioItemTexture{
    ResourceLocation getTexture(ItemStack stack, LivingEntity entity);
}
