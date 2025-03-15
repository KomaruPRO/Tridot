package pro.komaru.tridot.client.gfx.particle.options;

import pro.komaru.tridot.client.gfx.particle.data.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.*;
import pro.komaru.tridot.client.render.TridotRenderTypes;
import pro.komaru.tridot.client.gfx.particle.GenericParticle;
import pro.komaru.tridot.client.gfx.particle.GenericParticleRenderType;
import pro.komaru.tridot.client.gfx.particle.behavior.ParticleBehavior;
import pro.komaru.tridot.util.*;

import java.util.*;
import java.util.function.*;

public class GenericParticleOptions implements ParticleOptions{

    ParticleType<?> type;

    public RenderType renderType = TridotRenderTypes.ADDITIVE_PARTICLE;
    public ParticleRenderType particleRenderType = GenericParticleRenderType.INSTANCE;

    public ParticleBehavior behavior = null;

    public static final ColorParticleData DEFAULT_COLOR = ColorParticleData.create(Col.white, Col.white).build();
    public static final GenericParticleData DEFAULT_GENERIC = GenericParticleData.create(1, 0).build();
    public static final SpinParticleData DEFAULT_SPIN = SpinParticleData.create(0).build();
    public static final LightParticleData DEFAULT_LIGHT = LightParticleData.GLOW;
    public static final SpriteParticleData DEFAULT_SPRITE = SpriteParticleData.RANDOM;

    public ColorParticleData colorData = DEFAULT_COLOR;
    public GenericParticleData transparencyData = DEFAULT_GENERIC;
    public GenericParticleData scaleData = DEFAULT_GENERIC;
    public SpinParticleData spinData = DEFAULT_SPIN;
    public LightParticleData lightData = DEFAULT_LIGHT;
    public SpriteParticleData spriteData = DEFAULT_SPRITE;

    public final Collection<Consumer<GenericParticle>> tickActors = new ArrayList<>();
    public final Collection<Consumer<GenericParticle>> spawnActors = new ArrayList<>();
    public final Collection<Consumer<GenericParticle>> renderActors = new ArrayList<>();

    public enum DiscardFunctionType{
        NONE, INVISIBLE, ENDING_CURVE_INVISIBLE
    }

    public DiscardFunctionType discardFunctionType = DiscardFunctionType.INVISIBLE;

    public int lifetime = 20;
    public int additionalLifetime = 0;
    public float gravity = 0;
    public float additionalGravity = 0;
    public float friction = 0.98f;
    public float additionalFriction = 0;
    public boolean shouldCull = false;
    public boolean shouldRenderTraits = true;
    public boolean hasPhysics = true;

    public GenericParticleOptions(ParticleType<?> type){
        this.type = type;
    }

    @Override
    public ParticleType<?> getType(){
        return type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer){

    }

    @Override
    public String writeToString(){
        return getClass().getSimpleName();
    }
}