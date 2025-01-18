package github.iri.tridot.core.net.packets;

import net.minecraft.network.*;
import net.minecraftforge.network.*;

import java.util.*;
import java.util.function.*;

public abstract class ServerPacket{
    public static final Random random = new Random();

    public void encode(FriendlyByteBuf buf){
    }

    public final void handle(Supplier<NetworkEvent.Context> context){
        context.get().enqueueWork(() -> execute(context));
        context.get().setPacketHandled(true);
    }

    public void execute(Supplier<NetworkEvent.Context> context){
    }
}
