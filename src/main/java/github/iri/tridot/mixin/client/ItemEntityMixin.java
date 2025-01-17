package github.iri.tridot.mixin.client;

import github.iri.tridot.*;
import github.iri.tridot.common.interfaces.*;
import github.iri.tridot.config.*;
import net.minecraft.world.entity.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin{

    @Inject(at = @At("RETURN"), method = "tick")
    public void fluffy_fur$addParticles(CallbackInfo ci){
        ItemEntity self = (ItemEntity)((Object)this);
        if(self.level().isClientSide()){
            if(ClientConfig.ITEM_PARTICLE.get()){
                if(self.getItem().getItem() instanceof IParticleItem item){
                    item.addParticles(TridotLib.proxy.getLevel(), self);
                }
            }
        }
    }
}
