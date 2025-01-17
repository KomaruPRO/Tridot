package github.iri.tridot.common.interfaces;

import github.iri.tridot.client.animation.*;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.*;

public interface ICustomAnimationItem{
    @OnlyIn(Dist.CLIENT)
    ItemAnimation getAnimation(ItemStack stack);
}
