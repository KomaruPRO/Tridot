package github.iri.tridot.mixin.client;

import github.iri.tridot.*;
import github.iri.tridot.client.effect.*;
import github.iri.tridot.config.*;
import net.minecraft.world.level.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Explosion.class)
public abstract class ExplosionMixin{

    @Final
    @Shadow
    private float radius;

    @Inject(at = @At("RETURN"), method = "finalizeExplosion")
    public void fluffy_fur$tick(boolean spawnParticles, CallbackInfo ci){
        if(TridotLib.proxy.getLevel().isClientSide()){
            if(ClientConfig.EXPLOSION_EFFECT.get()){
                if(spawnParticles){
                    Explosion self = (Explosion)((Object)this);
                    TridotEffects.explosionEffect(TridotLib.proxy.getLevel(), self.getPosition(), radius);
                }
            }
        }
    }
}
