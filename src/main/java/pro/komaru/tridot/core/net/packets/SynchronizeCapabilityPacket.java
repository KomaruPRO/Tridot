package pro.komaru.tridot.core.net.packets;

import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraftforge.common.capabilities.*;
import pro.komaru.tridot.core.cap.*;
import pro.komaru.tridot.core.net.*;
import pro.komaru.tridot.core.struct.Structs;
import pro.komaru.tridot.core.struct.capability.CapImpl;
import pro.komaru.tridot.utilities.*;

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
