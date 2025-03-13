package pro.komaru.tridot.ocore.net.packets;

import net.minecraft.nbt.*;
import net.minecraft.network.*;

public abstract class ServerNBTPacket extends ServerPacket{
    protected CompoundTag nbt;

    public ServerNBTPacket(CompoundTag data){
        this.nbt = data;
    }

    public void encode(FriendlyByteBuf buf){
        buf.writeNbt(nbt);
    }

    public static <T extends ServerNBTPacket> T decode(PacketProvider<T> provider, FriendlyByteBuf buf){
        CompoundTag nbt = buf.readNbt();
        return provider.getPacket(nbt);
    }

    public interface PacketProvider<T extends ServerNBTPacket>{
        T getPacket(CompoundTag nbt);
    }
}
