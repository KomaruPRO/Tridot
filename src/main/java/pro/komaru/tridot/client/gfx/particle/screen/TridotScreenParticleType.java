package pro.komaru.tridot.client.gfx.particle.screen;

import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import org.jetbrains.annotations.*;
import pro.komaru.tridot.client.gfx.particle.*;
import pro.komaru.tridot.client.gfx.particle.options.*;
import pro.komaru.tridot.client.gfx.particle.type.*;

public class TridotScreenParticleType extends ScreenParticleType<ScreenParticleOptions>{
    public static class Factory implements ParticleProvider<ScreenParticleOptions> {
        public final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public ScreenParticle createParticle(ClientLevel pLevel, ScreenParticleOptions options, double x, double y, double pXSpeed, double pYSpeed){
            return new GenericScreenParticle(pLevel, options, (ParticleEngine.MutableSpriteSet) sprite, x, y, pXSpeed, pYSpeed);
        }
    }
}