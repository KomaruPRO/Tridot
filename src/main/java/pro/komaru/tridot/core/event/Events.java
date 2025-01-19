package pro.komaru.tridot.core.event;

import com.mojang.blaze3d.systems.*;
import net.minecraft.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.eventbus.api.*;
import pro.komaru.tridot.*;
import pro.komaru.tridot.client.graphics.Clr;
import pro.komaru.tridot.client.graphics.render.entity.bossbar.Bossbar;
import pro.komaru.tridot.core.config.*;
import pro.komaru.tridot.core.mixin.client.BossHealthOverlayAccessor;
import pro.komaru.tridot.core.proxy.*;
import pro.komaru.tridot.registry.*;
import pro.komaru.tridot.utilities.Util;

import java.awt.*;
import java.util.*;
import java.util.stream.*;

public class Events{

    @SubscribeEvent
    public void disableBlock(ShieldBlockEvent event){
        if(event.getDamageSource().getDirectEntity() instanceof Player player){
            LivingEntity mob = event.getEntity();
            ItemStack weapon = player.getMainHandItem();
            if(!weapon.isEmpty() && weapon.is(TagsRegistry.CAN_DISABLE_SHIELD) && mob instanceof Player attacked){
                attacked.disableShield(true);
            }
        }
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent e){
        if(Util.isDevelopment){
            ItemStack itemStack = e.getItemStack();
            Stream<ResourceLocation> itemTagStream = itemStack.getTags().map(TagKey::location);
            if(Minecraft.getInstance().options.advancedItemTooltips){
                if(Screen.hasControlDown()){
                    if(!itemStack.getTags().toList().isEmpty()){
                        e.getToolTip().add(net.minecraft.network.chat.Component.empty());
                        e.getToolTip().add(net.minecraft.network.chat.Component.literal("ItemTags: " + itemTagStream.toList()).withStyle(ChatFormatting.DARK_GRAY));
                    }

                    if(itemStack.getItem() instanceof BlockItem blockItem){
                        BlockState blockState = blockItem.getBlock().defaultBlockState();
                        Stream<ResourceLocation> blockTagStream = blockState.getTags().map(TagKey::location);
                        if(!blockState.getTags().map(TagKey::location).toList().isEmpty()){
                            if(itemStack.getTags().toList().isEmpty()){
                                e.getToolTip().add(net.minecraft.network.chat.Component.empty());
                            }

                            e.getToolTip().add(net.minecraft.network.chat.Component.literal("BlockTags: " + blockTagStream.toList()).withStyle(ChatFormatting.DARK_GRAY));
                        }
                    }
                }else if(!itemStack.getTags().toList().isEmpty() || itemStack.getItem() instanceof BlockItem blockItem && !blockItem.getBlock().defaultBlockState().getTags().toList().isEmpty()){
                    e.getToolTip().add(net.minecraft.network.chat.Component.empty());
                    e.getToolTip().add(Component.literal("Press [Control] to get tags info").withStyle(ChatFormatting.GRAY));
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @OnlyIn(Dist.CLIENT)
    public void onBossInfoRender(CustomizeGuiOverlayEvent.BossEventProgress ev){
        Minecraft mc = Minecraft.getInstance();
        if(ev.isCanceled() || mc.level == null || !ClientConfig.CUSTOM_BOSSBARS.get()) return;
        Map<UUID, LerpingBossEvent> events = ((BossHealthOverlayAccessor)mc.gui.getBossOverlay()).getEvents();
        if(events.isEmpty()) return;
        GuiGraphics pGuiGraphics = ev.getGuiGraphics();
        int screenWidth = pGuiGraphics.guiWidth();
        int offset = 0;
        for(LerpingBossEvent event : events.values()){
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

                if(bossbar != null){
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
