package pro.komaru.tridot.oclient.graphics.particle;

import net.minecraft.client.multiplayer.*;
import pro.komaru.tridot.oclient.graphics.particle.options.SpriteParticleOptions;

public class SpriteParticle extends GenericParticle{

    public SpriteParticle(ClientLevel level, SpriteParticleOptions options, double x, double y, double z, double vx, double vy, double vz){
        super(level, options, null, x, y, z, vx, vy, vz);
        this.setSprite(options.sprite);
    }
}
