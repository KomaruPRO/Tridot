package pro.komaru.tridot.common;

import com.mojang.blaze3d.systems.*;
import net.minecraft.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.*;
import net.minecraft.tags.*;
import net.minecraft.world.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.*;
import net.minecraftforge.event.entity.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.registries.*;
import pro.komaru.tridot.*;
import pro.komaru.tridot.api.render.bossbars.*;
import pro.komaru.tridot.client.sound.MusicHandler;
import pro.komaru.tridot.client.sound.MusicModifier;
import pro.komaru.tridot.common.config.ClientConfig;
import pro.komaru.tridot.common.config.CommonConfig;
import pro.komaru.tridot.common.networking.proxy.ClientProxy;
import pro.komaru.tridot.common.registry.TagsRegistry;
import pro.komaru.tridot.common.registry.item.*;
import pro.komaru.tridot.common.registry.item.armor.*;
import pro.komaru.tridot.common.registry.item.types.*;
import pro.komaru.tridot.mixin.client.BossHealthOverlayAccessor;
import pro.komaru.tridot.api.networking.PacketHandler;
import pro.komaru.tridot.common.networking.packets.DungeonSoundPacket;
import pro.komaru.tridot.api.Utils;
import pro.komaru.tridot.util.*;

import java.util.*;
import java.util.stream.*;

public class Events{
    public static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onJoinServer(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if(!event.isCanceled() && entity instanceof ServerPlayer player){
            evaluateArmorEffects(player);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if(!event.isCanceled()) {
            evaluateArmorEffects(event.getEntity());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if(!event.isCanceled()) {
            evaluateArmorEffects(event.getEntity());
        }
    }

    @SubscribeEvent
    public void onEquipmentChanged(LivingEquipmentChangeEvent event) {
        LivingEntity entity = event.getEntity();
        EquipmentSlot slot = event.getSlot();
        if(slot.isArmor() && entity instanceof Player player) {
            evaluateArmorEffects(player);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent e) {
        Player player = e.player;
        if(!e.isCanceled()) {
            if(!player.level().isClientSide()){
                evaluateArmorEffects(player);
            }
        }
    }

    @SubscribeEvent
    public void onShieldBlock(ShieldBlockEvent ev) {
        LivingEntity entity = ev.getEntity();
        ItemStack stack = entity.getUseItem();
        if(stack.getItem() instanceof ConfiguredShield shieldItem) {
            float armor = shieldItem.blockedPercent / 100.0F;
            armor = shieldItem.onPostBlock(stack, armor);
            float totalMultiplier = Math.max(Math.min(1 - (armor), 1), 0);
            float reducedDamage = ev.getOriginalBlockedDamage() * totalMultiplier;
            shieldItem.onShieldBlock(ev.getDamageSource(), ev.getOriginalBlockedDamage(), stack, entity);
            ev.setBlockedDamage(reducedDamage);
        }
    }

    public void evaluateArmorEffects(Player player) {
        Set<MobEffect> currentlyApplied = getTrackedEffects(player);
        Set<MobEffect> newApplied = new HashSet<>();
        for (var entry : AbstractArmorRegistry.EFFECTS.entrySet()) {
            ArmorMaterial material = entry.getKey();
            if (SuitArmorItem.hasCorrectArmorOn(material, player)) {
                for (var effectData : entry.getValue()) {
                    if (effectData.condition().test(player)) {
                        MobEffect effect = effectData.instance().get().getEffect();
                        newApplied.add(effect);
                        if (!player.hasEffect(effect)) {
                            MobEffectInstance instance = effectData.instance().get();
                            player.addEffect(instance);
                        }
                    }
                }
            }
        }

        for (MobEffect oldEffect : currentlyApplied) {
            if (!newApplied.contains(oldEffect) && player.hasEffect(oldEffect)) {
                player.removeEffect(oldEffect);
            }
        }

        saveTrackedEffects(player, newApplied);
    }

    private static final String ARMOR_EFFECTS_TAG = "ArmorEffects";
    public Set<MobEffect> getTrackedEffects(Player player) {
        CompoundTag tag = player.getPersistentData().getCompound(ARMOR_EFFECTS_TAG);
        Set<MobEffect> effects = new HashSet<>();
        for (String key : tag.getAllKeys()) {
            MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(key));
            if (effect != null) effects.add(effect);
        }

        return effects;
    }

    public void saveTrackedEffects(Player player, Set<MobEffect> effects) {
        CompoundTag tag = new CompoundTag();
        for (MobEffect effect : effects) {
            tag.putBoolean(ForgeRegistries.MOB_EFFECTS.getKey(effect).toString(), true);
        }

        player.getPersistentData().put(ARMOR_EFFECTS_TAG, tag);
    }

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
    public void onAttack(AttackEntityEvent event) {
        Entity target = event.getTarget();
        Player attacker = event.getEntity();
        if (!(target instanceof LivingEntity living)) return;
        for (var entry : AbstractArmorRegistry.HIT_EFFECTS.entrySet()) {
            ArmorMaterial material = entry.getKey();
            if (!SuitArmorItem.hasCorrectArmorOn(material, attacker)) return;
            for (var effectData : entry.getValue()) {
                float chance = effectData.chance();
                if (!Tmp.rnd.chance(chance) || !effectData.condition().test(attacker)) continue;
                living.addEffect(effectData.instance().get());
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
        ItemStack itemStack = e.getItemStack();
        Utils.Items.addSkinTooltip(itemStack, e.getToolTip());
        if(Utils.isDevelopment){
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
            if(CommonConfig.PERCENT_ARMOR.get()){
                if(event.getEntity().getAttribute(AttributeRegistry.PERCENT_ARMOR.get()) == null) return;
                float armor = (float)event.getEntity().getAttributeValue(AttributeRegistry.PERCENT_ARMOR.get()) / 100;
                totalMultiplier = Math.max(Math.min(1 - (armor), 1), 0);
                float reducedDamage = incomingDamage * totalMultiplier;
                event.setAmount(reducedDamage);
            }
        }
    }

//    public boolean customBossBarActive;
//    @SubscribeEvent(priority = EventPriority.HIGHEST)
//    @OnlyIn(Dist.CLIENT)
//    public void onBossInfoRender(CustomizeGuiOverlayEvent.BossEventProgress ev){
//        Minecraft mc = Minecraft.getInstance();
//        if(ev.isCanceled() || mc.level == null || !ClientConfig.CUSTOM_BOSSBARS.get()) return;
//        Map<UUID, LerpingBossEvent> events = ((BossHealthOverlayAccessor)mc.gui.getBossOverlay()).getEvents();
//        if(events.isEmpty()) return;
//        GuiGraphics pGuiGraphics = ev.getGuiGraphics();
//        int screenWidth = pGuiGraphics.guiWidth();
//        int offset = 0;
//        for(LerpingBossEvent event : events.values()){
//            String id = ClientProxy.bossbars.get(event.getId());
//            AbstractBossbar abstractBossbar = AbstractBossbar.bossbars.getOrDefault(id, null);
//            if(abstractBossbar == null && customBossBarActive) {
//                ev.setIncrement(18);
//                drawVanillaBar(pGuiGraphics, screenWidth / 2 - 91, offset, event);
//                int nameX = screenWidth / 2 - mc.font.width(event.getName()) / 2;
//                int nameY = offset + 16 - 9;
//                pGuiGraphics.drawString(mc.font, event.getName(), nameX, nameY, 16777215);
//            }
//
//            if(abstractBossbar != null){
//                customBossBarActive = true;
//                abstractBossbar.render(event, ev, offset, pGuiGraphics, abstractBossbar, mc);
//            }
//
//            offset += ev.getIncrement();
//            if(offset >= pGuiGraphics.guiHeight() / 4) break;
//        }
//
//        if (customBossBarActive) {
//            ev.setCanceled(true);
//        }
//    }

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
