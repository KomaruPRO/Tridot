package github.iri.tridot.mixin.common;

import github.iri.tridot.common.block.fire.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Creeper.class)
public abstract class CreeperMixin{

    @Inject(method = "mobInteract", at = @At("RETURN"), cancellable = true)
    private void fluffy_fur$mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir){
        Creeper self = (Creeper)((Object)this);
        for(FireItemModifier modifier : FireItemHandler.getModifiers()){
            if(modifier.isCreeperInteract(self, player, hand)){
                modifier.creeperInteract(self, player, hand);
                self.ignite();
                cir.setReturnValue(InteractionResult.sidedSuccess(self.level().isClientSide()));
            }
        }
    }
}
