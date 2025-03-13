package pro.komaru.tridot.ocore.entity;

import net.minecraft.network.syncher.*;
import net.minecraft.world.entity.*;
import pro.komaru.tridot.ocore.struct.func.Cons;
import pro.komaru.tridot.ocore.struct.func.Cons2;
import pro.komaru.tridot.ocore.struct.func.Func;

import java.lang.reflect.*;

public class TridotEntityVar<T> {
    public Cons<SynchedEntityData> define;
    public Cons2<SynchedEntityData,T> set;
    public Func<SynchedEntityData,T> get;

    public EntityDataAccessor<T> accessor;

    public Class<T> type;

    public String name;

    public TridotEntityVar(String name, Class<? extends LivingEntity> entity, T def) {
        type = (Class<T>) def.getClass();
        accessor = SynchedEntityData.defineId(entity, getSerializer());
        define = data -> data.define(accessor,def);
        set = (data,v) -> data.set(accessor,v);
        get = (data) -> data.get(accessor);
        this.name = name;
    }

    public EntityDataSerializer<T> getSerializer() {
        for (Field field : EntityDataSerializers.class.getFields()) {
            if (field.getGenericType() instanceof ParameterizedType parameterizedType) {
                Type actualType = parameterizedType.getActualTypeArguments()[0]; // первый generic аргумент
                if (actualType.equals(type)) { // сравнение с твоим типом
                    try {
                        return (EntityDataSerializer<T>) field.get(null); // достаём значение из static поля
                    } catch (IllegalAccessException e) {
                        e.printStackTrace(); // или хотя бы логнуть чот
                    }
                }
            }
        }
        return null;
    }
}
