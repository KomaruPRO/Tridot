package pro.komaru.tridot.client.gfx.particle.options;

import net.minecraft.core.particles.*;
import pro.komaru.tridot.client.gfx.particle.data.*;
import pro.komaru.tridot.client.gfx.particle.options.GenericParticleOptions.*;
import pro.komaru.tridot.client.gfx.particle.screen.*;
import pro.komaru.tridot.client.gfx.particle.type.*;
import pro.komaru.tridot.client.render.TridotRenderTypes.*;
import pro.komaru.tridot.util.*;

import java.util.function.*;

public class ScreenParticleOptions{
    public final ScreenParticleType<?> type;
    public static final ColorParticleData DEFAULT_COLOR = ColorParticleData.create(Col.white, Col.white).build();
    public static final GenericParticleData DEFAULT_GENERIC = GenericParticleData.create(1, 0).build();
    public static final SpinParticleData DEFAULT_SPIN = SpinParticleData.create(0).build();
    public static final ScreenSpriteParticleData DEFAULT_SPRITE = ScreenSpriteParticleData.RANDOM;

    public ColorParticleData colorData = DEFAULT_COLOR;
    public GenericParticleData transparencyData = DEFAULT_GENERIC;
    public GenericParticleData scaleData = DEFAULT_GENERIC;
    public SpinParticleData spinData = DEFAULT_SPIN;

    public ScreenParticleRenderType renderType = ScreenParticleRenderType.ADDITIVE;
    public ScreenSpriteParticleData spriteData = DEFAULT_SPRITE;
    public Consumer<GenericScreenParticle> actor;
    public DiscardFunctionType discardFunctionType = DiscardFunctionType.INVISIBLE;

    public int lifetime = 20;
    public int additionalLifetime = 0;
    public float gravity = 0;
    public float additionalGravity = 0;

    public boolean tracksStack;
    public double stackTrackXOffset;
    public double stackTrackYOffset;

    public ScreenParticleOptions(ScreenParticleType<?> type) {
        this.type = type;
    }
}