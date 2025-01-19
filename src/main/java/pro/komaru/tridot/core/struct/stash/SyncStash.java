package pro.komaru.tridot.core.struct.stash;

import pro.komaru.tridot.core.struct.Structs;
import pro.komaru.tridot.core.struct.stash.net.SyncStashObjectPacket;
import pro.komaru.tridot.core.struct.data.Seq;
import net.minecraft.server.level.*;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.*;
import pro.komaru.tridot.utilities.Utils;

public class SyncStash {
    private static int lastId = 0;

    private static final Seq<SyncStashObject<?>> stash = Seq.with();
    private static final Seq<Integer> lastChanged = Seq.with();

    public static <T> int add(T obj) {
        SyncStashObject<T> stashObject = new SyncStashObject<>(obj);
        stash.setSize(Math.max(stash.size,stashObject.getId()+1));
        stash.set(stashObject.getId(),stashObject);
        lastChanged.add(stashObject.getId());
        return stashObject.getId();
    }
    public static <T> int set(int id, byte[] obj) {
        SyncStashObject<T> stashObject = new SyncStashObject<>(id,obj);
        stash.setSize(Math.max(stash.size,stashObject.getId()+1));
        stash.set(stashObject.getId(),stashObject);
        return stashObject.getId();
    }
    public static <T> int setAt(int id, T obj) {
        boolean same = stash.getOrNull(id) == obj;
        SyncStashObject<T> stashObject = new SyncStashObject<>(obj);
        stashObject.setId(id);
        stash.setSize(Math.max(stash.size,stashObject.getId()+1));
        stash.set(stashObject.getId(),stashObject);
        if(!same) lastChanged.add(stashObject.getId());
        return stashObject.getId();
    }
    public static <T> T get(int obj) {
        return Structs.safeGet(getStash().getOrNull(obj), a -> (T) a.get());
    }
    public static <T> T getAndDelete(int obj) {
        T stashObject = get(obj);
        lastChanged.add(getStash().get(obj).getId());
        stash.remove(obj);
        if(lastId == obj) lastId--;
        return stashObject;
    }

    public static void synchronizeLast(SimpleChannel CHANNEL) {
        for (Integer i : lastChanged) {
            SyncStashObject<?> toSync = stash.get(i);
            int id = toSync.getId();
            byte[] bytes = toSync.toBytes();
            for (ServerPlayer player : Utils.players())
                CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SyncStashObjectPacket(id,bytes));
        }
    }

    public static int nextId() {
        return lastId++;
    };
    public static Seq<SyncStashObject<?>> getStash() {
        return stash;
    }
}
