package pro.komaru.tridot.core.event;

import pro.komaru.tridot.*;
import pro.komaru.tridot.client.gui.components.*;
import pro.komaru.tridot.TridotLib;
import pro.komaru.tridot.TridotLibClient;
import pro.komaru.tridot.client.gui.components.TridotMenuButton;
import pro.komaru.tridot.client.gui.components.TridotMenuButton.*;
import pro.komaru.tridot.client.sound.*;
import pro.komaru.tridot.client.sound.MusicHandler;
import pro.komaru.tridot.client.sound.MusicModifier;
import pro.komaru.tridot.client.sound.MusicModifier.*;
import pro.komaru.tridot.core.config.*;
import pro.komaru.tridot.core.net.*;
import pro.komaru.tridot.core.net.packets.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.server.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;
import org.apache.commons.lang3.mutable.*;
import pro.komaru.tridot.core.config.ClientConfig;
import pro.komaru.tridot.core.net.PacketHandler;
import pro.komaru.tridot.core.net.packets.DungeonSoundPacket;

@Mod.EventBusSubscriber(modid = TridotLib.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents{

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        MinecraftServer server = event.getServer();
        if (server.getTickCount() % 100 != 0) return;
        for (Player player : server.getPlayerList().getPlayers()) {
            for(MusicModifier modifier : MusicHandler.getModifiers()) {
                if(modifier instanceof Dungeon dungeonMusic) {
                    if (dungeonMusic.isPlayerInStructure(player, (ServerLevel) player.level()) && TridotLibClient.DUNGEON_MUSIC_INSTANCE == null) PacketHandler.sendTo(player, new DungeonSoundPacket(dungeonMusic.music, player.getX(), player.getY() + (player.getBbHeight() / 2), player.getZ()));
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onGuiInit(ScreenEvent.Init event){
        if(ClientConfig.MENU_BUTTON.get()){
            Screen gui = event.getScreen();
            MenuRows menu = null;
            int rowIdx = 0, offsetX = 0, offsetFreeX = 0, offsetFreeY = 0;
            if(gui instanceof TitleScreen){
                menu = MenuRows.MAIN_MENU;
                rowIdx = ClientConfig.MENU_BUTTON_ROW.get();
                offsetX = ClientConfig.MENU_BUTTON_ROW_X_OFFSET.get();
                offsetFreeX = ClientConfig.MENU_BUTTON_X_OFFSET.get();
                offsetFreeY = ClientConfig.MENU_BUTTON_Y_OFFSET.get();
            }

            if(rowIdx != 0 && menu != null){
                boolean onLeft = offsetX < 0;
                String target = (onLeft ? menu.leftButtons : menu.rightButtons).get(rowIdx - 1);

                int offsetX_ = offsetX;
                int offsetFreeX_ = offsetFreeX;
                int offsetFreeY_ = offsetFreeY;
                MutableObject<GuiEventListener> toAdd = new MutableObject<>(null);
                event.getListenersList()
                .stream()
                .filter(w -> w instanceof AbstractWidget)
                .map(w -> (AbstractWidget)w)
                .filter(w -> w.getMessage()
                .getString()
                .equals(target))
                .findFirst()
                .ifPresent(w -> toAdd
                .setValue(new TridotMenuButton(w.getX() + offsetX_ + (onLeft ? -20 : w.getWidth()) + offsetFreeX_, w.getY() + offsetFreeY_)));
                if(toAdd.getValue() != null)
                    event.addListener(toAdd.getValue());
            }
        }
    }
}