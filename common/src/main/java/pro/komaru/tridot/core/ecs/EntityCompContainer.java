package pro.komaru.tridot.core.ecs;

import net.minecraft.world.entity.Entity;
import pro.komaru.tridot.core.struct.Seq;

public interface EntityCompContainer {
    /** * Adds multiple components to this container.
     * The components will be added in a topological order based on their dependencies.
     * @param components the components to add.
     * @throws IllegalArgumentException if the components are null or if any component depends on components that are not present in the container.
     */
    void addComponents(Seq<EntityComp> components);
    /** * Adds a component to this container.
     * @param component the component to add.
     * @throws IllegalArgumentException if the component is null or if it depends on components that are not present in the container.
     */
    void addComponent(EntityComp component);
    /**
     * Removes a component from this container.
     * @param component the component to remove.
     * @throws IllegalArgumentException if the component is null or not present in the container.
     * @throws IllegalStateException if the component is a dependency for other components.
     */
    void removeComponent(EntityComp component);

    void removeAllComponents();

    /** * Returns the entity this container is assigned to.
     * @return the entity this container is assigned to.
     */
    Entity entity();
    /** * Returns the components in this container.
     * @return a sequence of components in this container.
     */
    Seq<EntityComp> components();
}
