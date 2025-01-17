package github.iri.tridot.mixin.client;

import github.iri.tridot.client.effect.*;
import github.iri.tridot.config.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(LightningBolt.class)
public abstract class LightningBoltMixin{

    @Shadow
    private int life;

    @Inject(at = @At("HEAD"), method = "tick")
    public void fluffy_fur$tick(CallbackInfo ci){
        LightningBolt self = (LightningBolt)((Object)this);
        Level level = self.level();
        if(level.isClientSide()){
            if(ClientConfig.LIGHTNING_BOLT_EFFECT.get()){
                Vec3 pos = self.position();
                if(life == 2){
                    TridotEffects.lightningBoltSpawnEffect(level, pos);
                }
                TridotEffects.lightningBoltTickEffect(level, pos);
            }
        }
    }
}
