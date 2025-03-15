package pro.komaru.tridot.client;

import net.minecraft.client.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.Component;
import net.minecraft.util.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.*;
import net.minecraftforge.eventbus.api.*;
import pro.komaru.tridot.api.Utils;
import pro.komaru.tridot.api.render.*;
import pro.komaru.tridot.client.gfx.TridotParticles;
import pro.komaru.tridot.client.gfx.particle.GenericParticle;
import pro.komaru.tridot.client.gfx.particle.ParticleBuilder;
import pro.komaru.tridot.client.gfx.particle.behavior.TrailParticleBehavior;
import pro.komaru.tridot.client.gfx.particle.data.ColorParticleData;
import pro.komaru.tridot.client.gfx.particle.data.GenericParticleData;
import pro.komaru.tridot.client.gfx.postprocess.*;
import pro.komaru.tridot.client.gfx.text.*;
import pro.komaru.tridot.client.model.render.item.bow.*;
import pro.komaru.tridot.client.render.TridotRenderTypes;
import pro.komaru.tridot.client.render.screenshake.*;
import pro.komaru.tridot.util.*;
import pro.komaru.tridot.util.math.Interp;
import pro.komaru.tridot.util.phys.Vec3;
import pro.komaru.tridot.util.struct.func.Cons;

import java.awt.*;
import java.util.function.Consumer;

public class ClientEvents {

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event){
        Minecraft minecraft = Minecraft.getInstance();
        ClientTick.clientTickEnd(event);
        if(event.phase == TickEvent.Phase.END){
            if(minecraft.isPaused()) return;
            Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
            ScreenshakeHandler.clientTick(camera);
            PostProcessHandler.tick(
            );
        }

        var plr = Utils.player();
        if(plr == null) return;
        Vec3 delta = Vec3.from(plr.getDeltaMovement().normalize());
        Vec3 plrPos = Vec3.from(plr);
        Vec3 pos = plrPos.cpy().add(delta.cpy().scale(0.00015f));
        final Vec3[] cachePos = {new Vec3(pos.x, pos.y, pos.z)};
        final Consumer<GenericParticle> target = p -> {
            Vec3 arrowPos = Vec3.from(plr);
            float lenBetweenArrowAndParticle = (arrowPos.sub(cachePos[0])).len();
            Vec3 vector = (arrowPos.sub(cachePos[0]));
            if(lenBetweenArrowAndParticle > 0){
                cachePos[0] = cachePos[0].add(vector);
                p.setPosition(cachePos[0]);
            }
        };

        ParticleBuilder.create(TridotParticles.TRAIL)
                .setRenderType(TridotRenderTypes.ADDITIVE_PARTICLE_TEXTURE)
                .setBehavior(TrailParticleBehavior.create().build())
                .setColorData(ColorParticleData.create(Col.green, Col.white).build())
                .setTransparencyData(GenericParticleData.create(1, 0).setEasing(Interp.bounceOut).build())
                .setScaleData(GenericParticleData.create(0.5f).setEasing(Interp.sineIn).build())
                .addTickActor(target)
                .setGravity(0)
                .setLifetime(30)
                .repeat(plr.level(), pos.x, pos.y, pos.z, 5);

    }

    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event){
        ClientTick.renderTick(event);
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
