package pro.komaru.tridot.client.gfx.particle.type;

import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import pro.komaru.tridot.client.gfx.particle.SpriteParticle;
import pro.komaru.tridot.client.gfx.particle.options.SpriteParticleOptions;

public class SpriteParticleType extends AbstractParticleType<SpriteParticleOptions>{

    public SpriteParticleType(){
        super();
    }

    public static class Factory implements ParticleProvider<SpriteParticleOptions>{
        public Factory(SpriteSet sprite){
        }

        @Override
        public Particle createParticle(SpriteParticleOptions options, ClientLevel level, double x, double y, double z, double mx, double my, double mz){
            return new SpriteParticle(level, options, x, y, z, mx, my, mz);
        }
    }
}