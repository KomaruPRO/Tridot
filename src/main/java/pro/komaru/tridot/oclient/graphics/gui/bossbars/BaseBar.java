package pro.komaru.tridot.oclient.graphics.gui.bossbars;

import com.mojang.blaze3d.systems.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.resources.*;
import net.minecraft.world.*;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent.*;
import pro.komaru.tridot.*;
import pro.komaru.tridot.ocore.config.*;
import pro.komaru.tridot.util.Col;

import java.util.*;

public class BaseBar extends AbstractBossbar{
    public BaseBar(ResourceLocation tex){
        super(tex);
    }

    public BaseBar(java.awt.Color color){
        super(color);
    }

    public BaseBar(boolean rainbow){
        super(rainbow);
    }

    public void render(BossEventProgress ev, LerpingBossEvent event, int offset, int screenWidth, GuiGraphics pGuiGraphics, AbstractBossbar abstractBossbar, Minecraft mc){
        if(ClientConfig.BOSSBAR_TITLE.get()){
            ev.setIncrement(32);
            int yOffset = offset + 6;
            int xOffset = screenWidth / 2 - 91;
            Minecraft.getInstance().getProfiler().push("BossBar");
            pGuiGraphics.blit(abstractBossbar.getTexture(), xOffset, yOffset, 0, 0, 183, 24, 256, 64);
            int i = (int)(event.getProgress() * 177.0F);
            if(i > 0){
                if(event.getOverlay() == BossEvent.BossBarOverlay.PROGRESS){
                    RenderSystem.enableBlend();
                    if(Objects.equals(abstractBossbar.getTexture(), new ResourceLocation(TridotLib.ID, "textures/gui/bossbars/base.png"))){
                        java.awt.Color color = abstractBossbar.rainbow ? Col.rainbowColor(mc.level.getGameTime() / 1.5f) : abstractBossbar.getColor();
                        pGuiGraphics.setColor((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255, 1);
                    }

                    pGuiGraphics.blit(abstractBossbar.getTexture(), xOffset + 3, yOffset + 14, 3, 30, i, 4, 256, 64);
                    RenderSystem.disableBlend();
                    pGuiGraphics.setColor(1, 1, 1, 1);
                }
            }

            int nameX = screenWidth / 2 - mc.font.width(event.getName()) / 2;
            int nameY = offset + 30;
            pGuiGraphics.drawString(mc.font, event.getName(), nameX, nameY, 16777215);
        }else{
            ev.setIncrement(26);
            int yOffset = offset + 6;
            int xOffset = screenWidth / 2 - 91;
            Minecraft.getInstance().getProfiler().push("BossBar");
            pGuiGraphics.blit(abstractBossbar.getTexture(), xOffset, yOffset, 0, 0, 183, 24, 256, 64);
            int i = (int)(event.getProgress() * 177.0F);
            if(i > 0){
                if(event.getOverlay() == BossEvent.BossBarOverlay.PROGRESS){
                    if(Objects.equals(abstractBossbar.getTexture(), new ResourceLocation(TridotLib.ID, "textures/gui/bossbars/base.png"))){
                        java.awt.Color color = abstractBossbar.rainbow ? Col.rainbowColor(mc.level.getGameTime() / 1.5f) : abstractBossbar.getColor();
                        pGuiGraphics.setColor((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255, 1);
                    }

                    RenderSystem.enableBlend();
                    pGuiGraphics.blit(abstractBossbar.getTexture(), xOffset + 3, yOffset + 14, 3, 30, i, 4, 256, 64);
                    RenderSystem.disableBlend();
                    pGuiGraphics.setColor(1, 1, 1, 1);
                }
            }
        }
    }
}
