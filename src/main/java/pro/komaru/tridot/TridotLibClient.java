package pro.komaru.tridot;

import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.*;
import net.minecraft.resources.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.event.lifecycle.*;
import pro.komaru.tridot.client.*;
import pro.komaru.tridot.client.gfx.*;
import pro.komaru.tridot.client.render.gui.particle.*;
import pro.komaru.tridot.client.tooltip.*;
import pro.komaru.tridot.client.sound.LoopedSoundInstance;
import pro.komaru.tridot.client.sound.TridotSoundInstance;
import pro.komaru.tridot.client.compatibility.ShadersIntegration;
import pro.komaru.tridot.common.registry.item.components.*;
import pro.komaru.tridot.common.registry.item.components.client.*;

import static pro.komaru.tridot.Tridot.*;

public class TridotLibClient{
    public static LoopedSoundInstance BOSS_MUSIC;
    public static TridotSoundInstance COOLDOWN_SOUND;
    public static TridotSoundInstance DUNGEON_MUSIC_INSTANCE;

    public static void clientSetup(final FMLClientSetupEvent event){
        ShadersIntegration.init();
    }

    public static void clientInit() {
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.register(new ClientEvents());
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void registerComponents(RegisterClientTooltipComponentFactoriesEvent e) {
            e.register(SeparatorComponent.class, c -> SeparatorClientComponent.create(c.component()));
            e.register(LineSeparatorComponent.class, c -> LineSeparatorClientComponent.create());
            e.register(AbilityComponent.class, c -> AbilityClientComponent.create(c.component(), c.icon(), c.paddingTop(), c.iconSize()));
            e.register(TextComponent.class, c -> TextClientComponent.create(c.component()));
            e.register(EffectsListComponent.class, c -> EffectListClientComponent.create(c.list(), c.component()));
            e.register(EmptyComponent.class, c -> EmptyClientComponent.create(c.height()));
        }

        @SubscribeEvent
        public static void registerOverlays(RegisterGuiOverlaysEvent event){
            event.registerAboveAll("boss_bars", BossBarsOverlay.INSTANCE);
        }

        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            ParticleEmitterHandler.registerEmitters(event);
        }

        @SubscribeEvent
        public static void registerParticleFactory(RegisterParticleProvidersEvent event) {
            TridotScreenParticles.registerParticleFactory(event);
        }

        @SubscribeEvent
        public static void registerAttributeModifiers(FMLClientSetupEvent event){
            TooltipModifierHandler.add(BASE_PROJECTILE_DAMAGE_UUID);
        }
    }
}
