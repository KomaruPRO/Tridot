package pro.komaru.tridot.util.struct.stash.net;

import pro.komaru.tridot.util.struct.stash.*;
import net.minecraft.network.*;
import pro.komaru.tridot.api.networking.Packet;

public class SyncStashObjectPacket implements Packet {
    int id;
    byte[] bytes;
    public SyncStashObjectPacket(int id, byte[] bytes) {
        this.id = id;
        this.bytes = bytes;
    }
    public SyncStashObjectPacket(FriendlyByteBuf buf) {
        this.id = buf.readInt();
        this.bytes = buf.readByteArray();
    }
    @Override
    public void save(FriendlyByteBuf buf) {
        buf.writeInt(id);
        buf.writeByteArray(bytes);
    }

    @Override
    public void doOnClient() {
        SyncStash.set(id,bytes);
    }
}
