package github.iri.tridot.client.particle;

import github.iri.tridot.client.particle.options.*;
import net.minecraft.client.multiplayer.*;

public class SpriteParticle extends GenericParticle{

    public SpriteParticle(ClientLevel level, SpriteParticleOptions options, double x, double y, double z, double vx, double vy, double vz){
        super(level, options, null, x, y, z, vx, vy, vz);
        this.setSprite(options.sprite);
    }
}
