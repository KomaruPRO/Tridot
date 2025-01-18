package pro.komaru.tridot.mixin.client;

import pro.komaru.tridot.*;
import pro.komaru.tridot.client.effect.*;
import pro.komaru.tridot.core.config.*;
import net.minecraft.world.level.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import pro.komaru.tridot.TridotLib;
import pro.komaru.tridot.client.effect.TridotEffects;
import pro.komaru.tridot.core.config.ClientConfig;

@Mixin(Explosion.class)
public abstract class ExplosionMixin{

    @Final
    @Shadow
    private float radius;

    @Inject(at = @At("RETURN"), method = "finalizeExplosion")
    public void tridot$tick(boolean spawnParticles, CallbackInfo ci){
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
