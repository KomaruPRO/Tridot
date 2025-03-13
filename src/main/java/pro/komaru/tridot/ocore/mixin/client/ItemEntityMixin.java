package pro.komaru.tridot.ocore.mixin.client;

import net.minecraft.world.entity.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import pro.komaru.tridot.*;
import pro.komaru.tridot.ocore.config.*;
import pro.komaru.tridot.ocore.interfaces.*;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin{

    @Inject(at = @At("RETURN"), method = "tick")
    public void tridot$addParticles(CallbackInfo ci){
        ItemEntity self = (ItemEntity)((Object)this);
        if(self.level().isClientSide()){
            if(ClientConfig.ITEM_PARTICLE.get()){
                if(self.getItem().getItem() instanceof ParticleItemEntity item){
                    item.spawnParticles(TridotLib.proxy.getLevel(), self);
                }
            }
        }
    }
}
