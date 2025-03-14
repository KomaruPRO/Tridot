package pro.komaru.tridot.client.gfx.particle.type;

import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import pro.komaru.tridot.client.gfx.particle.ItemParticle;
import pro.komaru.tridot.client.gfx.particle.options.ItemParticleOptions;

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