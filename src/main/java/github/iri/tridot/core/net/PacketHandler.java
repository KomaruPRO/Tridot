package github.iri.tridot.core.net;

import github.iri.tridot.*;
import github.iri.tridot.core.net.packets.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.*;

public class PacketHandler extends AbstractPacketHandler{
    public static final String PROTOCOL = "10";
    public static final SimpleChannel HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(TridotLib.ID, "network"), () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

    public static void init(){
        int id = 0;
        HANDLER.registerMessage(id++, DashParticlePacket.class, DashParticlePacket::encode, DashParticlePacket::decode, DashParticlePacket::handle);
        HANDLER.registerMessage(id++, CooldownSoundPacket.class, CooldownSoundPacket::encode, CooldownSoundPacket::decode, CooldownSoundPacket::handle);
        HANDLER.registerMessage(id++, DungeonSoundPacket.class, DungeonSoundPacket::encode, DungeonSoundPacket::decode, DungeonSoundPacket::handle);
        HANDLER.registerMessage(id++, UpdateBossbarPacket.class, UpdateBossbarPacket::encode, UpdateBossbarPacket::decode, UpdateBossbarPacket::handle);
    }

    public static SimpleChannel getHandler(){
        return HANDLER;
    }

    public static void sendTo(ServerPlayer playerMP, Object toSend){
        sendTo(getHandler(), playerMP, toSend);
    }

    public static void sendNonLocal(ServerPlayer playerMP, Object toSend){
        sendNonLocal(getHandler(), playerMP, toSend);
    }

    public static void sendToTracking(Level level, BlockPos pos, Object msg){
        sendToTracking(getHandler(), level, pos, msg);
    }

    public static void sendTo(Player entity, Object msg){
        sendTo(getHandler(), entity, msg);
    }

    public static void sendToServer(Object msg){
        sendToServer(getHandler(), msg);
    }
}