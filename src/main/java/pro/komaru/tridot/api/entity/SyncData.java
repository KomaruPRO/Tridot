package pro.komaru.tridot.api.entity;


import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import pro.komaru.tridot.util.struct.data.Seq;

public interface SyncData extends EntityComp<Entity> {
    Seq<DotSyncedEntry<?>> entries();

    default SynchedEntityData synchedData() {
        return entity().getEntityData();
    }
    default <T> DotSyncedEntry<T> getDataEntry(String name) {
        return (DotSyncedEntry<T>) entries().find(e -> e.name.equals(name));
    }

    default <T> T get(String name) {
        return (T) getDataEntry(name).get.get(synchedData());
    }
    default <T> void set(String name, T val) {
        getDataEntry(name).set.get(synchedData(),val);
    }
}
