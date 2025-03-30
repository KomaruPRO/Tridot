package pro.komaru.tridot.common;

import com.mojang.blaze3d.systems.*;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.resources.language.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.eventbus.api.*;
import pro.komaru.tridot.*;
import pro.komaru.tridot.api.render.bossbars.*;
import pro.komaru.tridot.client.sound.MusicHandler;
import pro.komaru.tridot.client.sound.MusicModifier;
import pro.komaru.tridot.common.config.ClientConfig;
import pro.komaru.tridot.common.config.CommonConfig;
import pro.komaru.tridot.common.networking.proxy.ClientProxy;
import pro.komaru.tridot.common.registry.TagsRegistry;
import pro.komaru.tridot.common.registry.item.*;
import pro.komaru.tridot.mixin.client.BossHealthOverlayAccessor;
import pro.komaru.tridot.api.networking.PacketHandler;
import pro.komaru.tridot.common.networking.packets.DungeonSoundPacket;
import pro.komaru.tridot.api.Utils;

import java.awt.*;
import java.util.*;
import java.util.stream.*;

public class Events{
    public static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        MinecraftServer server = event.getServer();
        if (server.getTickCount() % 100 != 0) return;
        for (Player player : server.getPlayerList().getPlayers()) {
            for(MusicModifier modifier : MusicHandler.getModifiers()) {
                if(modifier instanceof MusicModifier.DungeonMusic dungeonMusic) {
                    if (dungeonMusic.isPlayerInStructure(player, (ServerLevel) player.level()) && TridotLibClient.DUNGEON_MUSIC_INSTANCE == null) PacketHandler.sendTo(player, new DungeonSoundPacket(dungeonMusic.music, player.getX(), player.getY() + (player.getBbHeight() / 2), player.getZ()));
                }
            }
        }
    }

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
        if(Utils.isDevelopment){
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

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event){
        if(!event.getSource().is(DamageTypeTags.BYPASSES_ARMOR)){
            float incomingDamage = event.getAmount();
            float totalMultiplier;
            if(CommonConfig.PERCENT_ARMOR.get() && event.getEntity() instanceof Player player){
                float armor = (float)player.getAttributeValue(AttributeRegistry.PERCENT_ARMOR.get()) / 100;
                totalMultiplier = Math.max(Math.min(1 - (armor), 1), 0);
                float reducedDamage = incomingDamage * totalMultiplier;
                event.setAmount(reducedDamage);
            }
        }
    }

    public boolean customBossBarActive;
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
            String id = ClientProxy.bossbars.get(event.getId());
            AbstractBossbar abstractBossbar = AbstractBossbar.bossbars.getOrDefault(id, null);
            if(abstractBossbar == null && customBossBarActive) {
                ev.setIncrement(18);
                drawVanillaBar(pGuiGraphics, screenWidth / 2 - 91, offset, event);
                int nameX = screenWidth / 2 - mc.font.width(event.getName()) / 2;
                int nameY = offset + 16 - 9;
                pGuiGraphics.drawString(mc.font, event.getName(), nameX, nameY, 16777215);
            }

            if(abstractBossbar != null){
                customBossBarActive = true;
                abstractBossbar.render(ev, event, offset, screenWidth, pGuiGraphics, abstractBossbar, mc);
            }

            offset += ev.getIncrement();
            if(offset >= pGuiGraphics.guiHeight() / 4) break;
        }

        if (customBossBarActive) {
            ev.setCanceled(true);
        }
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
