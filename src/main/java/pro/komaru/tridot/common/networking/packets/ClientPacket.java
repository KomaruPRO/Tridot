package pro.komaru.tridot.common.networking.packets;

import net.minecraft.network.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.fml.*;
import net.minecraftforge.network.*;

import java.util.*;
import java.util.function.*;

public abstract class ClientPacket{
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
