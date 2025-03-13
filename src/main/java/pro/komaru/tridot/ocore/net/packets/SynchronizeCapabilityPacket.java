package pro.komaru.tridot.ocore.net.packets;

import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraftforge.common.capabilities.*;
import pro.komaru.tridot.ocore.cap.*;
import pro.komaru.tridot.ocore.net.*;
import pro.komaru.tridot.ocore.struct.Structs;
import pro.komaru.tridot.ocore.struct.capability.CapImpl;
import pro.komaru.tridot.outilities.*;

public class SynchronizeCapabilityPacket implements Packet {
    CompoundTag nbt;
    int id;
    public SynchronizeCapabilityPacket(CapImpl impl, int id) {
        this(impl.serializeNBT(),id);
    }
    public SynchronizeCapabilityPacket(CompoundTag tag,int id) {
        nbt = tag;
        this.id = id;
    }
    public SynchronizeCapabilityPacket(FriendlyByteBuf buf) {
        id = buf.readInt();
        nbt = buf.readNbt();
    }
    @Override
    public void save(FriendlyByteBuf buf) {
        buf.writeInt(id);
        buf.writeNbt(nbt);
    }

    @Override
    public void doOnClient() {
        Structs.safeRun(Utils.player(), p -> {
            CapEntry<?> entry = CapManager.caps.get(id);
            Capability<?> inst = entry.instance.get();
            p.getCapability(inst).ifPresent(a -> {
                if(a instanceof CapImpl i) i.deserializeNBT(nbt);
            });
        });
    }
}
