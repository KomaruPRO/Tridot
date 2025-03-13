package pro.komaru.tridot.ocore.event;

import net.minecraft.server.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import net.minecraftforge.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;
import pro.komaru.tridot.*;
import pro.komaru.tridot.oclient.sound.*;
import pro.komaru.tridot.oclient.sound.MusicModifier.*;
import pro.komaru.tridot.ocore.net.*;
import pro.komaru.tridot.ocore.net.packets.*;

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
}