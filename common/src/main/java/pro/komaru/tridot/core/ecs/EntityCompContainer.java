package pro.komaru.tridot.core.ecs;

import net.minecraft.world.entity.Entity;
import pro.komaru.tridot.core.struct.Seq;

/**
 * Represents a container for entity components.
 * This interface provides methods to add, remove, and retrieve components associated with an entity.
 * It also ensures that components are added and removed in a topological order based on their dependencies.
 */
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

    /** * Removes all components from this container.
     * This method is called when the entity is removed.
     * It will attempt to remove components in a topological order based on their dependencies.
     * This method will call `onRemoved` for each component before removing it.
     * @throws IllegalArgumentException if components cannot be removed due to dependency issues.
     * @throws IllegalStateException if the components cannot be removed safely due to dependency issues.
     */
    void removeAllComponents();

    /** * Returns the component of the specified type.
     * @param compType the class of the component to retrieve.
     * @param <T> the type of the component.
     * @return the component of the specified type, or null if not found.
     * @throws IllegalArgumentException if the compType is null.
     */
    default <T> T getComponent(Class<T> compType) {
        return getComponents(compType).firstOpt();
    };

    /** * Returns true if this container has a component of the specified type.
     * @param compType the class of the component to check.
     * @param <T> the type of the component.
     * @return true if a component of the specified type exists, false otherwise.
     * @throws IllegalArgumentException if the compType is null.
     */
    default <T> boolean hasComponent(Class<T> compType) {
        return getComponent(compType) != null;
    }

    /** * Returns all components of the specified type.
     * @param compType the class of the component to retrieve.
     * @param <T> the type of the component.
     * @return a sequence of components of the specified type, or an empty sequence if not found.
     * @throws IllegalArgumentException if the compType is null.
     */
    <T> Seq<T> getComponents(Class<T> compType);

    /** * Returns the entity this container is assigned to.
     * @return the entity this container is assigned to.
     */
    Entity entity();
    /** * Returns the components in this container.
     * @return a copy of the internal components list.
     */
    Seq<EntityComp> components();
    /** * Returns the components in this container, ordered by their priority.
     * Components with higher priority will be processed first.
     * @return a copy of internal components ordered by their priority.
     */
    Seq<EntityComp> componentsPriorityOrdered();
}
