package github.iri.tridot.mixin.common;

import github.iri.tridot.common.block.fire.*;
import net.minecraft.core.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(TntBlock.class)
public abstract class TntBlockMixin{

    @Inject(method = "use", at = @At("RETURN"), cancellable = true)
    private void fluffy_fur$use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir){
        TntBlock self = (TntBlock)((Object)this);
        for(FireItemModifier modifier : FireItemHandler.getModifiers()){
            if(modifier.isTntUse(state, level, pos, player, hand, hit)){
                modifier.tntUse(state, level, pos, player, hand, hit);
                self.onCaughtFire(state, level, pos, hit.getDirection(), player);
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
                cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide()));
            }
        }
    }
}
