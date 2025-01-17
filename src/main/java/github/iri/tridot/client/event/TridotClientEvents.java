package github.iri.tridot.client.event;

import github.iri.tridot.*;
import github.iri.tridot.client.bow.*;
import github.iri.tridot.client.gui.screen.*;
import github.iri.tridot.client.playerskin.*;
import github.iri.tridot.client.screenshake.*;
import github.iri.tridot.client.shader.postprocess.*;
import github.iri.tridot.core.config.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.*;
import net.minecraftforge.eventbus.api.*;

public class TridotClientEvents{

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
            if(minecraft.isPaused()){
                return;
            }
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
