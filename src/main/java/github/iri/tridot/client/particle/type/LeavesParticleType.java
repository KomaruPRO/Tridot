package github.iri.tridot.client.particle.type;

import github.iri.tridot.client.particle.*;
import github.iri.tridot.client.particle.options.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;

public class LeavesParticleType extends AbstractParticleType<GenericParticleOptions>{

    public LeavesParticleType(){
        super();
    }

    public static class Factory implements ParticleProvider<GenericParticleOptions>{
        private final SpriteSet sprite;

        public Factory(SpriteSet sprite){
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(GenericParticleOptions options, ClientLevel level, double x, double y, double z, double mx, double my, double mz){
            return new LeavesParticle(level, options, (ParticleEngine.MutableSpriteSet)sprite, x, y, z, mx, my, mz);
        }
    }
}