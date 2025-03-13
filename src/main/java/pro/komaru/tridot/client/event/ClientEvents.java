package pro.komaru.tridot.client.event;

import net.minecraft.client.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.network.chat.Component;
import net.minecraft.util.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.*;
import net.minecraftforge.eventbus.api.*;
import pro.komaru.tridot.*;
import pro.komaru.tridot.client.graphics.Clr;
import pro.komaru.tridot.client.graphics.gui.screen.*;
import pro.komaru.tridot.client.graphics.gui.screenshake.*;
import pro.komaru.tridot.client.graphics.render.item.bow.*;
import pro.komaru.tridot.client.graphics.shader.postprocess.*;
import pro.komaru.tridot.client.text.DotStyle;
import pro.komaru.tridot.client.text.TextFx;
import pro.komaru.tridot.core.config.*;

public class ClientEvents{

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event){
        Minecraft minecraft = Minecraft.getInstance();
        ClientTickHandler.clientTickEnd(event);
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
    public void render(RenderGuiEvent.Post event) {
        var gui = event.getGuiGraphics();
        var mc = Minecraft.getInstance();
        //todo temp thingy
        gui.drawString(mc.font,
                Component.literal("Сан-наби...")
                        .setStyle(DotStyle.of()
                                .color(Clr.fromHex("FF857C"))
                                .effects(
                                        TextFx.advance(1.5f),
                                        TextFx.wave(0.1f,0.2f),
                                        TextFx.shake(0.2f)
                                )
                        ),
                10,10,0xFFFFFFFF);


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
