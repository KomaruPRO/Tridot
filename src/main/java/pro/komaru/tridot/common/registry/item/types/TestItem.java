package pro.komaru.tridot.common.registry.item.types;

import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;
import pro.komaru.tridot.client.gfx.*;
import pro.komaru.tridot.client.gfx.particle.*;
import pro.komaru.tridot.util.*;
import pro.komaru.tridot.util.phys.*;

public class TestItem extends Item{
    public TestItem(Properties pProperties){
        super(pProperties);
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        ParticleBuilder.create(TridotParticles.SMOKE)
        .repeat(worldIn, new Vec3(playerIn.getX()  + Tmp.rnd.nextDouble(), playerIn.getY()  + Tmp.rnd.nextDouble(), playerIn.getZ()  + Tmp.rnd.nextDouble()), 15);
        return InteractionResultHolder.consume(itemstack);
    }

    public int getUseDuration(@NotNull ItemStack stack) {
        return 72000;
    }
}
