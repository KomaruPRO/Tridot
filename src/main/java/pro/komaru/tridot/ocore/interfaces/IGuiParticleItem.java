package pro.komaru.tridot.ocore.interfaces;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

public interface IGuiParticleItem{
    void renderParticle(PoseStack poseStack, LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset);
}
