package pro.komaru.tridot.client.graphics.particle.type;

import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import pro.komaru.tridot.client.graphics.particle.GenericParticle;
import pro.komaru.tridot.client.graphics.particle.options.GenericParticleOptions;

public class GenericParticleType extends AbstractParticleType<GenericParticleOptions>{

    public GenericParticleType(){
        super();
    }

    public static class Factory implements ParticleProvider<GenericParticleOptions>{
        private final SpriteSet sprite;

        public Factory(SpriteSet sprite){
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(GenericParticleOptions options, ClientLevel level, double x, double y, double z, double mx, double my, double mz){
            return new GenericParticle(level, options, (ParticleEngine.MutableSpriteSet)sprite, x, y, z, mx, my, mz);
        }
    }
}