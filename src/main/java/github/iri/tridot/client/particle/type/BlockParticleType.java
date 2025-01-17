package github.iri.tridot.client.particle.type;

import github.iri.tridot.client.particle.*;
import github.iri.tridot.client.particle.options.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;

public class BlockParticleType extends AbstractParticleType<BlockParticleOptions>{

    public BlockParticleType(){
        super();
    }

    public static class Factory implements ParticleProvider<BlockParticleOptions>{
        public Factory(SpriteSet sprite){
        }

        @Override
        public Particle createParticle(BlockParticleOptions options, ClientLevel level, double x, double y, double z, double mx, double my, double mz){
            return new BlockParticle(level, options, x, y, z, mx, my, mz);
        }
    }
}