package pro.komaru.tridot.client.render.gui;

import com.mojang.blaze3d.platform.*;
import com.mojang.blaze3d.systems.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.*;
import pro.komaru.tridot.common.config.*;
import pro.komaru.tridot.util.struct.data.*;

@OnlyIn(Dist.CLIENT)
public class OverlayInstance{
    private ResourceLocation location;
    private boolean isRendered;
    private int showTick;
    private int showTime;

    public float fadeIn = 20;
    public float fadeOut = 20;

    public OverlayInstance() {
        if (ClientConfig.ABILITY_OVERLAY.get()) {
            this.isRendered = true;
        }
    }

    public OverlayInstance(ResourceLocation texture, int showTime) {
        if (ClientConfig.ABILITY_OVERLAY.get()) {
            this.isRendered = true;
            this.setShowTime(showTime);
            this.setTexture(texture);
        }
    }

    public void tick(TickEvent.ClientTickEvent event) {
        int totalDuration = (int) (fadeIn + showTime + fadeOut);
        if (isRendered) {
            if (showTick < totalDuration) {
                showTick++;
            } else {
                showTick = 0;
                isRendered = false;
            }
        }
    }
    public void onDraw(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        GuiGraphics gui = event.getGuiGraphics();
        if (isRendered) {
            gui.pose().pushPose();
            gui.pose().translate(0, 0, -200);
            int width = mc.getWindow().getGuiScaledWidth();
            int height = mc.getWindow().getGuiScaledHeight();
            float alpha = getAlpha(event);
            float f = 0.1F;

            renderOverlay(gui, f, alpha, width, height);
            gui.pose().popPose();
        }
    }

    private void renderOverlay(GuiGraphics gui, float f, float alpha, int width, int height){
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        gui.setColor(f * alpha, f * alpha, f * alpha, alpha);
        gui.blit(location, 0, 0, 0, 0, 1920, 1080, width, height);
        gui.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();

        RenderSystem.applyModelViewMatrix();
    }

    private float getAlpha(RenderGuiOverlayEvent.Post event) {
        float ticks = showTick + event.getPartialTick();
        float alpha;

        float totalDuration = fadeIn + showTime + fadeOut;
        if (ticks < fadeIn) {
            alpha = Mth.clamp(ticks / fadeIn, 0.0F, 1.0F);
        }else if (ticks < fadeIn + showTime) {
            alpha = 1.0F;
        }else if (ticks < totalDuration) {
            float fadeOutProgress = (ticks - (fadeIn + showTime)) / fadeOut;
            alpha = 1.0F - Mth.clamp(fadeOutProgress, 0.0F, 1.0F);
        }else {
            alpha = 0.0F;
        }

        return alpha;
    }

    public OverlayInstance setTexture(ResourceLocation location) {
        this.location = location;
        return this;
    }

    public OverlayInstance setShowTime(int time) {
        this.showTime = time;
        return this;
    }

    public OverlayInstance setFadeIn(float time) {
        this.fadeIn = time;
        return this;
    }

    public OverlayInstance setFadeOut(float time) {
        this.fadeOut = time;
        return this;
    }
}
