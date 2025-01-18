package github.iri.tridot.core.net.packets;

import net.minecraft.network.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.fml.*;
import net.minecraftforge.network.*;

import java.util.*;
import java.util.function.*;

public abstract class ClientPacket{
    public static final Random random = new Random();

    public void encode(FriendlyByteBuf buf){
    }

    public final void handle(Supplier<NetworkEvent.Context> context){
        context.get().enqueueWork(() -> {
            if(context.get().getDirection().getReceptionSide().equals(LogicalSide.CLIENT)){
                ClientOnly.clientData(this, context);
            }
        });
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public void execute(Supplier<NetworkEvent.Context> context){
    }

    public static class ClientOnly{
        public static void clientData(ClientPacket packet, Supplier<NetworkEvent.Context> context){
            packet.execute(context);
        }
    }
}
