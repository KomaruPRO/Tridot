package pro.komaru.tridot.core.entity.entc;

import net.minecraft.world.entity.Entity;
import pro.komaru.tridot.core.struct.Seq;
import pro.komaru.tridot.core.struct.func.Func;

public interface EntcCompsInitializer extends Func<Entity, Seq<EntcComp>> {
    /**
     * Initializes the component sequence for the given entity.
     * This method is called when the entity is created or loaded.
     *
     * @param entity the entity to initialize the component sequence for
     * @return the initialized component sequence
     */
    @Override
    Seq<EntcComp> get(Entity entity);
}
