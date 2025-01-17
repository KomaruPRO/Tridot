package github.iri.tridot.client.particle.type;

import github.iri.tridot.client.particle.FluidParticle;
import github.iri.tridot.client.particle.options.FluidParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;

public class FluidParticleType extends AbstractParticleType<FluidParticleOptions>{

    public FluidParticleType(){
        super();
    }

    public static class Factory implements ParticleProvider<FluidParticleOptions>{
        public Factory(SpriteSet sprite){
        }

        @Override
        public Particle createParticle(FluidParticleOptions options, ClientLevel level, double x, double y, double z, double mx, double my, double mz){
            return new FluidParticle(level, options, x, y, z, mx, my, mz);
        }
    }
}