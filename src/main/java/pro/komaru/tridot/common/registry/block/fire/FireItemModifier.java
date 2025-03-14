package pro.komaru.tridot.common.registry.block.fire;

import net.minecraft.core.*;
import net.minecraft.sounds.*;
import net.minecraft.stats.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;

import java.util.*;

public class FireItemModifier{

    public static final Random random = new Random();

    public boolean isCreeperInteract(Entity entity, Player player, InteractionHand hand){
        return false;
    }

    public boolean isTntUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit){
        return false;
    }

    public void creeperInteract(Entity entity, Player player, InteractionHand hand){
        SoundEvent soundevent = SoundEvents.FLINTANDSTEEL_USE;
        entity.level().playSound(player, entity.getX(), entity.getY(), entity.getZ(), soundevent, entity.getSoundSource(), 1.0F, random.nextFloat() * 0.4F + 0.8F);
    }

    public void tntUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit){
        player.awardStat(Stats.ITEM_USED.get(player.getItemInHand(hand).getItem()));
    }
}
