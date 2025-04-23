package pro.komaru.tridot.client.gfx;

import pro.komaru.tridot.Tridot;
import pro.komaru.tridot.client.gfx.particle.type.*;
import net.minecraft.client.*;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.registries.*;
import pro.komaru.tridot.client.gfx.particle.GenericParticle;
import pro.komaru.tridot.client.gfx.particle.ICustomParticleRender;
import pro.komaru.tridot.client.gfx.particle.behavior.ICustomBehaviorParticleRender;
import pro.komaru.tridot.client.render.LevelRenderHandler;

public class TridotParticles{
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Tridot.ID);
    public static RegistryObject<GenericParticleType> WISP = PARTICLES.register("wisp", GenericParticleType::new);
    public static RegistryObject<GenericParticleType> TINY_WISP = PARTICLES.register("tiny_wisp", GenericParticleType::new);
    public static RegistryObject<GenericParticleType> SPARKLE = PARTICLES.register("sparkle", GenericParticleType::new);
    public static RegistryObject<GenericParticleType> STAR = PARTICLES.register("star", GenericParticleType::new);
    public static RegistryObject<GenericParticleType> TINY_STAR = PARTICLES.register("tiny_star", GenericParticleType::new);
    public static RegistryObject<GenericParticleType> SQUARE = PARTICLES.register("square", GenericParticleType::new);
    public static RegistryObject<GenericParticleType> DOT = PARTICLES.register("dot", GenericParticleType::new);
    public static RegistryObject<GenericParticleType> CIRCLE = PARTICLES.register("circle", GenericParticleType::new);
    public static RegistryObject<GenericParticleType> TINY_CIRCLE = PARTICLES.register("tiny_circle", GenericParticleType::new);
    public static RegistryObject<GenericParticleType> HEART = PARTICLES.register("heart", GenericParticleType::new);
    public static RegistryObject<GenericParticleType> SKULL = PARTICLES.register("skull", GenericParticleType::new);
    public static RegistryObject<GenericParticleType> SMOKE = PARTICLES.register("smoke", GenericParticleType::new);
    public static RegistryObject<GenericParticleType> TRAIL = PARTICLES.register("trail", GenericParticleType::new);
    public static RegistryObject<ItemParticleType> ITEM = PARTICLES.register("item", ItemParticleType::new);
    public static RegistryObject<BlockParticleType> BLOCK = PARTICLES.register("block", BlockParticleType::new);
    public static RegistryObject<FluidParticleType> FLUID = PARTICLES.register("fluid", FluidParticleType::new);
    public static RegistryObject<SpriteParticleType> SPRITE = PARTICLES.register("sprite", SpriteParticleType::new);
    public static RegistryObject<LeavesParticleType> CHERRY_LEAVES = PARTICLES.register("cherry_leaves", LeavesParticleType::new);

    public static void register(IEventBus eventBus){
        PARTICLES.register(eventBus);
    }

    @Mod.EventBusSubscriber(modid = Tridot.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientRegistryEvents{
        @SubscribeEvent
        public static void registerParticles(RegisterParticleProvidersEvent event){
            ParticleEngine particleEngine = Minecraft.getInstance().particleEngine;
            particleEngine.register(WISP.get(), GenericParticleType.Factory::new);
            particleEngine.register(TINY_WISP.get(), GenericParticleType.Factory::new);
            particleEngine.register(SPARKLE.get(), GenericParticleType.Factory::new);
            particleEngine.register(STAR.get(), GenericParticleType.Factory::new);
            particleEngine.register(TINY_STAR.get(), GenericParticleType.Factory::new);
            particleEngine.register(SQUARE.get(), GenericParticleType.Factory::new);
            particleEngine.register(DOT.get(), GenericParticleType.Factory::new);
            particleEngine.register(CIRCLE.get(), GenericParticleType.Factory::new);
            particleEngine.register(TINY_CIRCLE.get(), GenericParticleType.Factory::new);
            particleEngine.register(HEART.get(), GenericParticleType.Factory::new);
            particleEngine.register(SKULL.get(), GenericParticleType.Factory::new);
            particleEngine.register(SMOKE.get(), GenericParticleType.Factory::new);
            particleEngine.register(TRAIL.get(), GenericParticleType.Factory::new);
            particleEngine.register(ITEM.get(), ItemParticleType.Factory::new);
            particleEngine.register(BLOCK.get(), BlockParticleType.Factory::new);
            particleEngine.register(FLUID.get(), FluidParticleType.Factory::new);
            particleEngine.register(SPRITE.get(), SpriteParticleType.Factory::new);
            particleEngine.register(CHERRY_LEAVES.get(), LeavesParticleType.Factory::new);
        }
    }

    public static void addParticleList(ICustomParticleRender particle){
        LevelRenderHandler.particleList.add(particle);
    }

    public static void addBehaviorParticleList(GenericParticle particle, ICustomBehaviorParticleRender behavior){
        LevelRenderHandler.behaviorParticleList.put(particle, behavior);
    }
}
