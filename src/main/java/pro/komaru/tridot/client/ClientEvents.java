package pro.komaru.tridot.client;

import net.minecraft.client.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.*;
import net.minecraftforge.eventbus.api.*;
import pro.komaru.tridot.api.render.text.DotText;
import pro.komaru.tridot.client.gfx.postprocess.*;
import pro.komaru.tridot.client.model.render.item.bow.*;
import pro.komaru.tridot.client.render.screenshake.*;
import pro.komaru.tridot.util.Col;

import static pro.komaru.tridot.api.render.text.DotText.*;

public class ClientEvents {

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event){
        Minecraft minecraft = Minecraft.getInstance();
        ClientTick.clientTickEnd(event);
        if(event.phase == TickEvent.Phase.END){
            if(minecraft.isPaused()) return;
            Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
            ScreenshakeHandler.clientTick(camera);
            PostProcessHandler.tick();
        }
    }

    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event){
        ClientTick.renderTick(event);
    }

    @SubscribeEvent
    public void render(RenderGuiEvent event) {
        DotText.create("Hello ")
                .add(
                        DotText.create("world!")
                                .effects()
                                .effects(
                                        rainbow(1f,true),
                                        outline(Col.black,true),
                                        wave(1f))
                )
                .renderStyle(r ->
                        r
                                .shadow(true)
                                .centered(false,false)
                                .maxWidth(60f + (float) Math.abs(Math.sin(ClientTick.getTotal()/50f))*60f)
                                .scl((float) (1f+Math.abs(Math.sin(ClientTick.getTotal()/20f)))))
                .render(event.getGuiGraphics(),100,100);
        ;
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
                    float f1 = computeFov(player);

                    f *= 1.0F - f1 * 0.15F;
                    event.setNewFovModifier((float)Mth.lerp(Minecraft.getInstance().options.fovEffectScale().get(), 1.0F, f));
                }
            }
        }

        ScreenshakeHandler.fovTick(event);
    }

    private float computeFov(Player player) {
        int i = player.getTicksUsingItem();
        float f1 = (float)i / 20.0F;
        if(f1 > 1.0F){
            f1 = 1.0F;
        }else {
            f1 *= f1;
        }

        return f1;
    }
}
