package pro.komaru.tridot.core.entity.ecs;

import net.minecraft.world.entity.Entity;
import pro.komaru.tridot.core.struct.Seq;
import pro.komaru.tridot.core.struct.func.Func;

public interface EntityCompsInitializer extends Func<Entity, Seq<EntityComp>> {
    /**
     * Initializes the component sequence for the given entity.
     * This method is called when the entity is created or loaded.
     *
     * @param entity the entity to initialize the component sequence for
     * @return the initialized component sequence
     */
    @Override
    Seq<EntityComp> get(Entity entity);
}
