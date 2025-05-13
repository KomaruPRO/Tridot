package pro.komaru.tridot.api.entity.ai.brain;

import net.minecraft.world.entity.Entity;
import pro.komaru.tridot.api.entity.EntityComp;

public interface HasAI extends EntityComp<Entity> {
    EntityAI ai();

    default void tickAI() {

    }
}
