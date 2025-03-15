package pro.komaru.tridot.common.registry.item.types;

import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;
import pro.komaru.tridot.client.gfx.*;
import pro.komaru.tridot.client.gfx.particle.*;
import pro.komaru.tridot.client.gfx.particle.data.*;
import pro.komaru.tridot.client.render.*;
import pro.komaru.tridot.util.*;

public class TestItem extends Item{
    public TestItem(Properties pProperties){
        super(pProperties);
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        var rand = Tmp.rnd;
        net.minecraft.world.phys.Vec3 pos = new net.minecraft.world.phys.Vec3(playerIn.getX() + (rand.nextDouble() - 0.5f) / 6, playerIn.getY() + 0.4F, playerIn.getZ());

        Col particleColor = Col.pink;
        Col particleColorTo = Col.blue;

        ParticleBuilder.create(TridotParticles.HEART.get())
            .setRenderType(TridotRenderTypes.ADDITIVE_PARTICLE)

            .setScaleData(GenericParticleData.create(1 + Tmp.rnd.randomValueUpTo(0.15f), Tmp.rnd.randomValueUpTo(0.2f)).build())
            .setLifetime(100)
            .setColorData(ColorParticleData.create(particleColor, particleColorTo).build())
            .setVelocity((Tmp.rnd.nextDouble() / 5), 0.05f, (Tmp.rnd.nextDouble() / 5))
            .repeat(worldIn, pos.x, pos.y, pos.z, 1);


        return InteractionResultHolder.consume(itemstack);
    }

    public int getUseDuration(@NotNull ItemStack stack) {
        return 72000;
    }
}
