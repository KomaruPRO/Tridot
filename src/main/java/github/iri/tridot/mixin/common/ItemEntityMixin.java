package github.iri.tridot.mixin.common;

import github.iri.tridot.registry.entity.misc.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin{

    @Inject(at = @At("HEAD"), method = "tick")
    public void fluffy_fur$tick(CallbackInfo ci){
        ItemEntity self = (ItemEntity)((Object)this);
        for(ItemEntityModifier modifier : ItemEntityHandler.getModifiers()){
            if(modifier.isItem(self.level(), self, self.getItem())){
                modifier.tick(self.level(), self, self.getItem());
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "hurt", cancellable = true)
    public void fluffy_fur$hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        ItemEntity self = (ItemEntity)((Object)this);
        for(ItemEntityModifier modifier : ItemEntityHandler.getModifiers()){
            if(modifier.isItem(self.level(), self, self.getItem())){
                if(modifier.rejectHurt(self.level(), self, self.getItem(), source, amount)){
                    cir.setReturnValue(false);
                }
            }
        }
    }
}
