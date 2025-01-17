package github.iri.tridot.registry.event;

import com.mojang.blaze3d.systems.*;
import github.iri.tridot.*;
import github.iri.tridot.client.render.entity.bossbar.*;
import github.iri.tridot.core.config.*;
import github.iri.tridot.core.proxy.*;
import github.iri.tridot.mixin.client.*;
import github.iri.tridot.util.client.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.resources.*;
import net.minecraft.world.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.*;

import java.awt.*;
import java.util.*;

public class Events{

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @OnlyIn(Dist.CLIENT)
    public void onBossInfoRender(CustomizeGuiOverlayEvent.BossEventProgress ev){
        Minecraft mc = Minecraft.getInstance();
        if(ev.isCanceled() || mc.level == null || !ClientConfig.CUSTOM_BOSSBARS.get()) return;
        Map<UUID, LerpingBossEvent> events = ((BossHealthOverlayAccessor) mc.gui.getBossOverlay()).getEvents();
        if (events.isEmpty()) return;
        GuiGraphics pGuiGraphics = ev.getGuiGraphics();
        int screenWidth = pGuiGraphics.guiWidth();
        int offset = 0;
        for (LerpingBossEvent event : events.values()){
            if(ClientProxy.bossbars.containsKey(ev.getBossEvent().getId())){
                String id = ClientProxy.bossbars.get(event.getId());
                Bossbar bossbar = Bossbar.bossbars.getOrDefault(id, null);
                if(bossbar == null){
                    ev.setIncrement(18);
                    drawVanillaBar(pGuiGraphics, screenWidth / 2 - 91, offset, event);
                    int nameX = screenWidth / 2 - mc.font.width(event.getName()) / 2;
                    int nameY = offset + 16 - 9;
                    pGuiGraphics.drawString(mc.font, event.getName(), nameX, nameY, 16777215);
                }

                if (bossbar != null){
                    if(ClientConfig.BOSSBAR_TITLE.get()){
                        ev.setIncrement(32);
                        int yOffset = offset + 6;
                        int xOffset = screenWidth / 2 - 91;
                        Minecraft.getInstance().getProfiler().push("BossBar");
                        pGuiGraphics.blit(bossbar.getTexture(), xOffset, yOffset, 0, 0, 183, 24, 256, 64);
                        int i = (int)(event.getProgress() * 177.0F);
                        if(i > 0){
                            if(event.getOverlay() == BossEvent.BossBarOverlay.PROGRESS){
                                RenderSystem.enableBlend();
                                if(Objects.equals(bossbar.getTexture(), new ResourceLocation(TridotLib.ID, "textures/gui/bossbars/base.png"))){
                                    Color color = bossbar.rainbow ? Clr.rainbowColor(mc.level.getGameTime() / 1.5f) : bossbar.getColor();
                                    pGuiGraphics.setColor((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255, 1);
                                }

                                pGuiGraphics.blit(bossbar.getTexture(), xOffset + 3, yOffset + 14, 3, 30, i, 4, 256, 64);
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
                        pGuiGraphics.blit(bossbar.getTexture(), xOffset, yOffset, 0, 0, 183, 24, 256, 64);
                        int i = (int)(event.getProgress() * 177.0F);
                        if(i > 0){
                            if(event.getOverlay() == BossEvent.BossBarOverlay.PROGRESS){
                                if(Objects.equals(bossbar.getTexture(), new ResourceLocation(TridotLib.ID, "textures/gui/bossbars/base.png"))){
                                    Color color = bossbar.rainbow ? Clr.rainbowColor(mc.level.getGameTime() / 1.5f) : bossbar.getColor();
                                    pGuiGraphics.setColor((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255, 1);
                                }

                                RenderSystem.enableBlend();
                                pGuiGraphics.blit(bossbar.getTexture(), xOffset + 3, yOffset + 14, 3, 30, i, 4, 256, 64);
                                RenderSystem.disableBlend();
                                pGuiGraphics.setColor(1, 1, 1, 1);
                            }
                        }
                    }
                }

                offset += ev.getIncrement();
                if(offset >= pGuiGraphics.guiHeight() / 4) break;
            }
        }

        ev.setCanceled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void drawVanillaBar(GuiGraphics pGuiGraphics, int pX, int offset, BossEvent pBossEvent){
        drawVanillaBar(pGuiGraphics, pX, offset + 16, pBossEvent, 182, 0);
        int i = (int)(pBossEvent.getProgress() * 183.0F);
        if(i > 0){
            drawVanillaBar(pGuiGraphics, pX, offset + 16, pBossEvent, i, 5);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void drawVanillaBar(GuiGraphics pGuiGraphics, int pX, int pY, BossEvent pBossEvent, int pWidth, int p_281636_){
        pGuiGraphics.blit(TridotLibClient.VANILLA_LOC, pX, pY, 0, pBossEvent.getColor().ordinal() * 5 * 2 + p_281636_, pWidth, 5);
        if(pBossEvent.getOverlay() != BossEvent.BossBarOverlay.PROGRESS){
            RenderSystem.enableBlend();
            pGuiGraphics.blit(TridotLibClient.VANILLA_LOC, pX, pY, 0, 80 + (pBossEvent.getOverlay().ordinal() - 1) * 5 * 2 + p_281636_, pWidth, 5);
            RenderSystem.disableBlend();
        }
    }
}
