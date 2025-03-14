package pro.komaru.tridot.common.networking.packets;

import net.minecraft.network.*;
import net.minecraftforge.network.*;

import java.util.*;
import java.util.function.*;

public abstract class ServerPacket{
    public void encode(FriendlyByteBuf buf){
    }

    public final void handle(Supplier<NetworkEvent.Context> context){
        context.get().enqueueWork(() -> execute(context));
        context.get().setPacketHandled(true);
    }

    public void execute(Supplier<NetworkEvent.Context> context){
    }
}
