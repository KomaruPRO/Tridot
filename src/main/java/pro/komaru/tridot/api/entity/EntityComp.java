package pro.komaru.tridot.api.entity;

import net.minecraft.world.entity.Entity;

public interface EntityComp<T extends Entity> {
    default T entity() {
        return (T) this;
    }
}
