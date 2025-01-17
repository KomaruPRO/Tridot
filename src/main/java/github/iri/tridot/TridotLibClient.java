package github.iri.tridot;

import github.iri.tridot.client.bow.*;
import github.iri.tridot.client.event.*;
import github.iri.tridot.client.gui.screen.*;
import github.iri.tridot.client.screenshake.*;
import github.iri.tridot.client.shader.postprocess.*;
import github.iri.tridot.client.sound.*;
import github.iri.tridot.client.splash.*;
import github.iri.tridot.client.tooltip.*;
import github.iri.tridot.core.config.*;
import github.iri.tridot.integration.client.*;
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

import java.awt.*;

import static github.iri.tridot.TridotLib.*;

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

    public static TridotMod MOD_INSTANCE;
    public static TridotPanorama VANILLA_PANORAMA;
    public static void setupMenu(){
        MOD_INSTANCE = new TridotMod(TridotLib.ID, TridotLib.NAME, TridotLib.VERSION).setDev("-").setItem(new ItemStack(Items.PINK_PETALS))
        .setEdition(TridotLib.VERSION_NUMBER).setNameColor(new Color(254, 200, 207)).setVersionColor(new Color(92, 72, 90));

        VANILLA_PANORAMA = new TridotPanorama("minecraft:vanilla", Component.translatable("panorama.minecraft.vanilla")).setItem(new ItemStack(Items.GRASS_BLOCK));
        TridotModsHandler.registerPanorama(VANILLA_PANORAMA);
    }

    public static void setupSplashes(){
        SplashHandler.addSplash("Привет, Россия!");
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

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public void onOpenScreen(ScreenEvent.Opening event){
            panoramaScreen(event.getCurrentScreen());
            panoramaScreen(event.getNewScreen());
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public void onOpenScreenFirst(ScreenEvent.Opening event){
            resetPanoramaScreen(event.getCurrentScreen());
            resetPanoramaScreen(event.getNewScreen());
        }

        public static void panoramaScreen(Screen screen){
            if(screen instanceof TitleScreen titleScreen){
                TridotPanorama panorama = TridotModsHandler.getPanorama(ClientConfig.PANORAMA.get());
                if(panorama != null && !panorama.equals(TridotLibClient.VANILLA_PANORAMA)){
                    boolean setPanorama = !TitleScreen.CUBE_MAP.images[0].equals(panorama.getTexture());
                    if(setPanorama){
                        TridotModsHandler.setOpenPanorama(titleScreen, panorama);
                    }
                }
            }
        }

        public static void resetPanoramaScreen(Screen screen){
            if(screen instanceof TitleScreen titleScreen){
                TridotPanorama panorama = TridotModsHandler.getPanorama(ClientConfig.PANORAMA.get());
                if(panorama != null || panorama.equals(TridotLibClient.VANILLA_PANORAMA)){
                    TridotModsHandler.setOpenPanorama(titleScreen, TridotLibClient.VANILLA_PANORAMA);
                }
            }
        }

        @SubscribeEvent
        public void clientTick(TickEvent.ClientTickEvent event){
            Minecraft minecraft = Minecraft.getInstance();
            ClientTickHandler.clientTick(event);
            if(event.phase == TickEvent.Phase.END){
                if(minecraft.isPaused()) return;
                Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
                ScreenshakeHandler.clientTick(camera);
                PostProcessHandler.tick();
            }
        }

        @SubscribeEvent
        public void renderTick(TickEvent.RenderTickEvent event){
            ClientTickHandler.renderTick(event);
        }

        @SubscribeEvent
        public void getFovModifier(ComputeFovModifierEvent event){
            Player player = event.getPlayer();
            ItemStack itemStack = player.getUseItem();
            if(player.isUsingItem()){
                for(Item item : BowHandler.getBows()){
                    if(itemStack.is(item)){
                        float f = event.getFovModifier();
                        if(f != event.getNewFovModifier()) f = event.getNewFovModifier();
                        int i = player.getTicksUsingItem();
                        float f1 = (float)i / 20.0F;
                        if(f1 > 1.0F){
                            f1 = 1.0F;
                        }else{
                            f1 *= f1;
                        }

                        f *= 1.0F - f1 * 0.15F;
                        event.setNewFovModifier((float)Mth.lerp(Minecraft.getInstance().options.fovEffectScale().get(), 1.0F, f));
                    }
                }
            }

            ScreenshakeHandler.fovTick(event);
        }
    }
}
