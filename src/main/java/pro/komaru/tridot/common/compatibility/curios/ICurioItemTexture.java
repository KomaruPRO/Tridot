package pro.komaru.tridot.common.compatibility.curios;

import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;

public interface ICurioItemTexture{
    ResourceLocation getTexture(ItemStack stack, LivingEntity entity);
}
