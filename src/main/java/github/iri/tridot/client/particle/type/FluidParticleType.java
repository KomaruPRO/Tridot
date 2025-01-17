package github.iri.tridot.client.particle.type;

import github.iri.tridot.client.particle.*;
import github.iri.tridot.client.particle.options.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;

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