package pro.komaru.tridot.core.ecs;

import net.minecraft.world.entity.Entity;
import pro.komaru.tridot.core.struct.Seq;
import pro.komaru.tridot.core.struct.enums.GameSide;

public abstract class EntityComp {
    private Entity entity;

    /** * Returns the priority of this component.
     * Components with higher priority are processed first.
     * Please use appropriate priority values to ensure correct behavior and further expansion.
     * @return the priority of this component, default is 0.
     */
    public int priority() {
        return 0;
    }

    /** * Returns the game side this component is for.
     * Components that are not relevant for the current game side will not be processed.
     * @return the game side this component is for.
     */
    public abstract GameSide side();

    /** * Called when this component is added to an entity and the entity was assigned. */
    public void onAdded() {}
    /** * Called when this component is removed from an entity. */
    public void onRemoved() {}

    /** * Called every tick for this component. */
    public void onTick() {}


    /** * Returns the dependencies of this component.
     * Components that are listed here will be processed before this component.
     * @return a sequence of component classes that this component depends on, default is an empty sequence.
     */
    public Seq<Class<? extends EntityComp>> dependencies() {
        return Seq.empty();
    }

    /** * Returns the entity this component is assigned to.
     * @throws IllegalStateException if the entity is not set.
     */
    public Entity getEntity() {
        if(entity == null) throw new IllegalStateException("Entity is not set for this component.");
        return entity;
    }
    /** * Assigns this component to an entity.
     * @param entity the entity to assign this component to.
     * @throws IllegalStateException if the entity is already set for this component.
     * @throws IllegalArgumentException if the entity is null.
     */
    public void assignEntity(Entity entity) {
        if(this.entity != null) throw new IllegalStateException("Entity is already set for this component.");
        if(entity == null) throw new IllegalArgumentException("Entity cannot be null.");

        this.entity = entity;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}