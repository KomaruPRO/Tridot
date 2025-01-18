package pro.komaru.tridot.core.interfaces;

import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.*;
import pro.komaru.tridot.client.graphics.render.animation.*;

public interface ICustomAnimationItem{
    @OnlyIn(Dist.CLIENT)
    ItemAnimation getAnimation(ItemStack stack);
}
