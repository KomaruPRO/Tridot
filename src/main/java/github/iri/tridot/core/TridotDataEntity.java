package github.iri.tridot.core;

import github.iri.tridot.core.entity.*;
import github.iri.tridot.utilities.func.*;
import github.iri.tridot.utilities.struct.*;
import net.minecraft.nbt.*;
import net.minecraft.network.syncher.*;

public interface TridotDataEntity {
    Seq<TridotEntityVar<?>> data();
    SynchedEntityData synchedData();

    default TridotEntityVar<?> getVar(String name) {
        return data().find(e -> e.name.equals(name));
    }

    default <T> T get(String name) {
        return (T) getVar(name).get.get(synchedData());
    }
    default <T> void set(String name, T val) {
        ((TridotEntityVar<T>) getVar(name)).set.get(synchedData(),val);
    }

    default void writeData(CompoundTag tag) {
        data().each(e -> {
            Object obj = e.get.get(synchedData());
            if (e.type == Integer.class) {
                tag.putInt(e.name, (Integer) obj);
            } else if (e.type == Float.class) {
                tag.putFloat(e.name, (Float) obj);
            } else if (e.type == String.class) {
                tag.putString(e.name, (String) obj);
            } else if (e.type == Boolean.class) {
                tag.putBoolean(e.name, (Boolean) obj);
            }
        });
    }
    default void readData(CompoundTag tag) {
        data().each(e -> {
            Object value = null;
            Cons2<SynchedEntityData,Object> set = (Cons2<SynchedEntityData, Object>) e.set;
            if (e.type == Integer.class) {
                value = tag.getInt(e.name);
            } else if (e.type == Float.class) {
                value = tag.getFloat(e.name);
            } else if (e.type == String.class) {
                value = tag.getString(e.name);
            } else if (e.type == Boolean.class) {
                value = tag.getBoolean(e.name);
            }

            if (value != null) {
                set.get(synchedData(), value);  // This is safe since `value` is now typed correctly.
            }
        });
    }
    default void defineData() {
        data().each(e -> e.define.get(synchedData()));
    }
}
