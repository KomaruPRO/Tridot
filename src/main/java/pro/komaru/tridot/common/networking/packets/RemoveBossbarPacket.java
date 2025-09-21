package pro.komaru.tridot.common.networking.packets;

import net.minecraft.network.*;
import net.minecraft.sounds.*;
import net.minecraftforge.network.NetworkEvent.*;
import pro.komaru.tridot.*;

import java.util.*;
import java.util.function.*;

public class RemoveBossbarPacket{
    private final UUID id;

    public RemoveBossbarPacket(UUID id){
        this.id = id;
    }

    public static RemoveBossbarPacket decode(FriendlyByteBuf buf){
        return new RemoveBossbarPacket(buf.readUUID());
    }

    public static void encode(RemoveBossbarPacket msg, FriendlyByteBuf buf){
        buf.writeUUID(msg.id);
    }

    public static void handle(RemoveBossbarPacket msg, Supplier<Context> context){
        context.get().setPacketHandled(true);
        Tridot.PROXY.removeBossBarRender(msg.id);
    }
}