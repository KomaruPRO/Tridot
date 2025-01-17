package github.iri.tridot.client.particle.type;

import github.iri.tridot.client.particle.*;
import github.iri.tridot.client.particle.options.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;

public class ItemParticleType extends AbstractParticleType<ItemParticleOptions>{

    public ItemParticleType(){
        super();
    }

    public static class Factory implements ParticleProvider<ItemParticleOptions>{
        public Factory(SpriteSet sprite){
        }

        @Override
        public Particle createParticle(ItemParticleOptions options, ClientLevel level, double x, double y, double z, double mx, double my, double mz){
            return new ItemParticle(level, options, x, y, z, mx, my, mz);
        }
    }
}