package pro.komaru.tridot.utilities.stash.net;

import pro.komaru.tridot.core.net.*;
import pro.komaru.tridot.utilities.stash.*;
import net.minecraft.network.*;
import pro.komaru.tridot.core.net.Packet;
import pro.komaru.tridot.utilities.stash.SyncStash;

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
