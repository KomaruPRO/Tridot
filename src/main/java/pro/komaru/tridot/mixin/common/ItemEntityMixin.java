package pro.komaru.tridot.mixin.common;

import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import pro.komaru.tridot.common.registry.entity.misc.ItemEntityHandler;
import pro.komaru.tridot.common.registry.entity.misc.ItemEntityModifier;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin{

    @Inject(at = @At("HEAD"), method = "tick")
    public void tridot$tick(CallbackInfo ci){
        ItemEntity self = (ItemEntity)((Object)this);
        for(ItemEntityModifier modifier : ItemEntityHandler.getModifiers()){
            if(modifier.isItem(self.level(), self, self.getItem())){
                modifier.tick(self.level(), self, self.getItem());
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "hurt", cancellable = true)
    public void tridot$hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
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
