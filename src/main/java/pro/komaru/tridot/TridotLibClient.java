package pro.komaru.tridot;

import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.*;
import pro.komaru.tridot.client.event.*;
import net.minecraft.resources.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.event.lifecycle.*;
import pro.komaru.tridot.client.graphics.tooltip.*;
import pro.komaru.tridot.client.sound.LoopedSoundInstance;
import pro.komaru.tridot.client.sound.TridotSoundInstance;
import pro.komaru.tridot.client.graphics.gui.splash.SplashHandler;
import pro.komaru.tridot.integration.client.ShadersIntegration;

import java.awt.*;

import static pro.komaru.tridot.TridotLib.*;

public class TridotLibClient{
    public static LoopedSoundInstance BOSS_MUSIC;
    public static TridotSoundInstance COOLDOWN_SOUND;
    public static TridotSoundInstance DUNGEON_MUSIC_INSTANCE;
    public static final ResourceLocation VANILLA_LOC = new ResourceLocation("textures/gui/bars.png");

    public static void clientSetup(final FMLClientSetupEvent event){
        setupSplashes();
        ShadersIntegration.init();
    }

    public static void clientInit() {
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.register(new ClientEvents());
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
                    return modifier.getId().equals(BASE_PROJECTILE_DAMAGE_UUID);
                }
            });
        }
    }
}
