package pro.komaru.tridot;

import net.minecraft.locale.Language;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.javafmlmod.*;
import pro.komaru.tridot.client.bow.*;
import pro.komaru.tridot.client.event.*;
import pro.komaru.tridot.client.gui.screen.*;
import pro.komaru.tridot.client.screenshake.*;
import pro.komaru.tridot.client.shader.postprocess.*;
import pro.komaru.tridot.client.sound.*;
import pro.komaru.tridot.client.splash.*;
import pro.komaru.tridot.client.tooltip.*;
import pro.komaru.tridot.core.config.*;
import pro.komaru.tridot.integration.client.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.event.lifecycle.*;
import pro.komaru.tridot.client.bow.BowHandler;
import pro.komaru.tridot.client.event.ClientTickHandler;
import pro.komaru.tridot.client.gui.screen.TridotMod;
import pro.komaru.tridot.client.gui.screen.TridotModsHandler;
import pro.komaru.tridot.client.gui.screen.TridotPanorama;
import pro.komaru.tridot.client.screenshake.ScreenshakeHandler;
import pro.komaru.tridot.client.shader.postprocess.PostProcessHandler;
import pro.komaru.tridot.client.sound.LoopedSoundInstance;
import pro.komaru.tridot.client.sound.TridotSoundInstance;
import pro.komaru.tridot.client.splash.SplashHandler;
import pro.komaru.tridot.client.tooltip.AttributeTooltipModifier;
import pro.komaru.tridot.client.tooltip.TooltipModifierHandler;
import pro.komaru.tridot.core.config.ClientConfig;
import pro.komaru.tridot.integration.client.ShadersIntegration;

import java.awt.*;

import static pro.komaru.tridot.TridotLib.*;

public class TridotLibClient{
    public static LoopedSoundInstance BOSS_MUSIC;
    public static TridotSoundInstance COOLDOWN_SOUND;
    public static TridotSoundInstance DUNGEON_MUSIC_INSTANCE;
    public static final ResourceLocation VANILLA_LOC = new ResourceLocation("textures/gui/bars.png");

    public static void clientSetup(final FMLClientSetupEvent event){
        setupMenu();
        setupSplashes();
        ShadersIntegration.init();
    }

    public static void clientInit() {
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.register(new ClientEvents());
    }

    public static TridotMod MOD_INSTANCE;
    public static TridotPanorama VANILLA_PANORAMA;
    public static void setupMenu(){
        MOD_INSTANCE = new TridotMod(TridotLib.ID, TridotLib.NAME, TridotLib.VERSION).setDev("-").setItem(new ItemStack(Items.PINK_PETALS))
        .setEdition(TridotLib.VERSION_NUMBER).setNameColor(new Color(254, 200, 207)).setVersionColor(new Color(92, 72, 90));

        VANILLA_PANORAMA = new TridotPanorama("minecraft:vanilla", Component.translatable("panorama.minecraft.vanilla")).setItem(new ItemStack(Items.GRASS_BLOCK));
        TridotModsHandler.registerPanorama(VANILLA_PANORAMA);
    }

    public static void setupSplashes(){
        SplashHandler.addSplash(8,"ru_ru", "Привет, Россия!");
        SplashHandler.addSplash(8,"uk_ua","Привіт, Україно!");
        SplashHandler.addSplash(8,"kk_kz","Сәлем, Қазақстан!");
        SplashHandler.addSplash(8,"en_us","Hello, USA!");
        SplashHandler.addSplash(8,"fr_fr","Bonjour, France!");
        SplashHandler.addSplash(8,"es_es","Hola, España!");
        SplashHandler.addSplash(8,"de_de","Hallo, Deutschland!");
        SplashHandler.addSplash(8,"it_it","Ciao, Italia!");
        SplashHandler.addSplash(8,"ja_jp","こんにちは、日本!");
        SplashHandler.addSplash(8,"ko_kr","안녕하세요, 대한민국!");
        SplashHandler.addSplash(8,"zh_cn","你好, 中国!");
        SplashHandler.addSplash(8,"ar_sa","مرحبا، العالم العربي!");
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void registerAttributeModifiers(FMLClientSetupEvent event){
            TooltipModifierHandler.register(new AttributeTooltipModifier(){
                public boolean isToolBase(AttributeModifier modifier, Player player, TooltipFlag flag){
                    return modifier.getId().equals(BASE_ENTITY_REACH_UUID);
                }
            });

            TooltipModifierHandler.register(new AttributeTooltipModifier(){
                public boolean isToolBase(AttributeModifier modifier, Player player, TooltipFlag flag){
                    return modifier.getId().equals(BASE_PROJECTILE_DAMAGE_UUID);
                }
            });

            TooltipModifierHandler.register(new AttributeTooltipModifier(){
                public boolean isToolBase(AttributeModifier modifier, Player player, TooltipFlag flag){
                    return modifier.getId().equals(BASE_DASH_DISTANCE_UUID);
                }
            });

            TooltipModifierHandler.register(new AttributeTooltipModifier(){
                public boolean isToolBase(AttributeModifier modifier, Player player, TooltipFlag flag){
                    return modifier.getId().equals(BASE_ATTACK_RADIUS_UUID);
                }
            });
        }
    }
}
