package pro.komaru.tridot.core.interfaces;

import pro.komaru.tridot.client.animation.*;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.*;
import pro.komaru.tridot.client.animation.ItemAnimation;

public interface ICustomAnimationItem{
    @OnlyIn(Dist.CLIENT)
    ItemAnimation getAnimation(ItemStack stack);
}
