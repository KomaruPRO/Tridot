package pro.komaru.tridot.oclient.graphics.particle.options;

import net.minecraft.client.renderer.texture.*;
import net.minecraft.core.particles.*;

public class SpriteParticleOptions extends GenericParticleOptions{
    public final TextureAtlasSprite sprite;

    public SpriteParticleOptions(ParticleType<?> type, TextureAtlasSprite sprite){
        super(type);
        this.sprite = sprite;
    }
}
