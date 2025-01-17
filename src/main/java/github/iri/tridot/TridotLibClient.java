package github.iri.tridot;

import github.iri.tridot.client.event.*;
import github.iri.tridot.client.gui.screen.*;
import github.iri.tridot.client.sound.*;
import github.iri.tridot.client.splash.*;
import github.iri.tridot.integration.client.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraftforge.common.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.event.lifecycle.*;

import java.awt.*;

public class TridotLibClient{
    public static TridotSoundInstance COOLDOWN_SOUND;
    public static TridotSoundInstance DUNGEON_MUSIC_INSTANCE;
    public static final ResourceLocation VANILLA_LOC = new ResourceLocation("textures/gui/bars.png");

    public static class ClientOnly{
        public static void clientInit(){
            IEventBus forgeBus = MinecraftForge.EVENT_BUS;
            forgeBus.register(new TridotClientEvents());
        }
    }

    public static void clientSetup(final FMLClientSetupEvent event){
        setupMenu();
        setupSplashes();
        ShadersIntegration.init();
    }

    public static TridotMod MOD_INSTANCE;
    public static TridotPanorama VANILLA_PANORAMA;
    public static TridotPanorama FLUFFY_PANORAMA;

    public static void setupMenu(){
        MOD_INSTANCE = new TridotMod(TridotLib.ID, TridotLib.NAME, TridotLib.VERSION).setDev("MaxBogomol").setItem(new ItemStack(Items.PINK_PETALS))
        .setEdition(TridotLib.VERSION_NUMBER).setNameColor(new Color(254, 200, 207)).setVersionColor(new Color(92, 72, 90))
        .setDescription(Component.translatable("mod_description.fluffy_fur"))
        .addGithubLink("https://github.com/MaxBogomol/FluffyFur")
        .addCurseForgeLink("https://www.curseforge.com/minecraft/mc-mods/fluffy-fur")
        .addModrinthLink("https://modrinth.com/mod/fluffy-fur")
        .addDiscordLink("https://discord.gg/cKf55qNugw");
        VANILLA_PANORAMA = new TridotPanorama("minecraft:vanilla", Component.translatable("panorama.minecraft.vanilla")).setItem(new ItemStack(Items.GRASS_BLOCK));
        FLUFFY_PANORAMA = new TridotPanorama(TridotLib.ID + ":fluffy_zone", Component.translatable("panorama.fluffy_fur.fluffy_zone"))
        .setMod(MOD_INSTANCE).setItem(new ItemStack(Items.CHERRY_LEAVES))
        .setTexture(new ResourceLocation(TridotLib.ID, "textures/gui/title/background/panorama"))
        .setLogo(new ResourceLocation(TridotLib.ID, "textures/gui/title/fluffy_fur.png"));

        registerMod(MOD_INSTANCE);
        registerPanorama(VANILLA_PANORAMA);
        registerPanorama(FLUFFY_PANORAMA);
    }

    public static void registerMod(TridotMod mod){
        TridotModsHandler.registerMod(mod);
    }

    public static void registerPanorama(TridotPanorama panorama){
        TridotModsHandler.registerPanorama(panorama);
    }

    public static void setupSplashes(){
        SplashHandler.addSplash("Привет, Россия!");
    }
}
