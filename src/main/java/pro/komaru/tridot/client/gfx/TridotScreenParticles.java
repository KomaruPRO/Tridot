package pro.komaru.tridot.client.gfx;

import net.minecraft.client.*;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.*;
import net.minecraft.resources.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.registries.*;
import pro.komaru.tridot.*;
import pro.komaru.tridot.client.gfx.particle.*;
import pro.komaru.tridot.client.gfx.particle.behavior.*;
import pro.komaru.tridot.client.gfx.particle.options.*;
import pro.komaru.tridot.client.gfx.particle.screen.*;
import pro.komaru.tridot.client.gfx.particle.type.*;
import pro.komaru.tridot.client.render.*;

import java.util.*;

public class TridotScreenParticles{
    public static final ArrayList<ScreenParticleType<?>> PARTICLE_TYPES = new ArrayList<>();

    public static ScreenParticleType<ScreenParticleOptions> WISP = registerType(new TridotScreenParticleType());
    public static ScreenParticleType<ScreenParticleOptions> TINY_WISP = registerType(new TridotScreenParticleType());
    public static ScreenParticleType<ScreenParticleOptions> SPARKLE = registerType(new TridotScreenParticleType());
    public static ScreenParticleType<ScreenParticleOptions> STAR = registerType(new TridotScreenParticleType());
    public static ScreenParticleType<ScreenParticleOptions> SQUARE = registerType(new TridotScreenParticleType());
    public static ScreenParticleType<ScreenParticleOptions> DOT = registerType(new TridotScreenParticleType());
    public static ScreenParticleType<ScreenParticleOptions> CIRCLE = registerType(new TridotScreenParticleType());
    public static ScreenParticleType<ScreenParticleOptions> TINY_CIRCLE = registerType(new TridotScreenParticleType());
    public static ScreenParticleType<ScreenParticleOptions> HEART = registerType(new TridotScreenParticleType());
    public static ScreenParticleType<ScreenParticleOptions> SKULL = registerType(new TridotScreenParticleType());

    public static void registerParticleFactory(RegisterParticleProvidersEvent event) {
        registerProvider(WISP, new TridotScreenParticleType.Factory(getSpriteSet(TridotLib.loc("wisp"))));
        registerProvider(TINY_WISP, new TridotScreenParticleType.Factory(getSpriteSet(TridotLib.loc("tiny_wisp"))));
        registerProvider(SPARKLE, new TridotScreenParticleType.Factory(getSpriteSet(TridotLib.loc("sparkle"))));
        registerProvider(STAR, new TridotScreenParticleType.Factory(getSpriteSet(TridotLib.loc("star"))));
        registerProvider(SQUARE, new TridotScreenParticleType.Factory(getSpriteSet(TridotLib.loc("square"))));
        registerProvider(DOT, new TridotScreenParticleType.Factory(getSpriteSet(TridotLib.loc("dot"))));
        registerProvider(CIRCLE, new TridotScreenParticleType.Factory(getSpriteSet(TridotLib.loc("circle"))));
        registerProvider(TINY_CIRCLE, new TridotScreenParticleType.Factory(getSpriteSet(TridotLib.loc("tiny_circle"))));
        registerProvider(HEART, new TridotScreenParticleType.Factory(getSpriteSet(TridotLib.loc("heart"))));
        registerProvider(SKULL, new TridotScreenParticleType.Factory(getSpriteSet(TridotLib.loc("skull"))));
    }

    public static <T extends ScreenParticleOptions> ScreenParticleType<T> registerType(ScreenParticleType<T> type) {
        PARTICLE_TYPES.add(type);
        return type;
    }

    public static <T extends ScreenParticleOptions> void registerProvider(ScreenParticleType<T> type, ScreenParticleType.ParticleProvider<T> provider) {
        type.provider = provider;
    }

    public static SpriteSet getSpriteSet(ResourceLocation resourceLocation) {
        return Minecraft.getInstance().particleEngine.spriteSets.get(resourceLocation);
    }
}
