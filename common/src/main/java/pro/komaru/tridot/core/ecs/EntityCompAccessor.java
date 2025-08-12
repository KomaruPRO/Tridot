package pro.komaru.tridot.core.ecs;

import pro.komaru.tridot.core.struct.Seq;

/**
 * An interface for accessing and managing components in an entity component container.
 * This interface provides methods to add, remove, and retrieve components from the container.
 * It also ensures that components are managed in a way that respects their dependencies.
 */
public interface EntityCompAccessor {
    default EntityCompContainer componentContainer() {
        return (EntityCompContainer) this;
    }

    /** * Adds a component to this container.
     * @param component the component to add.
     * @throws IllegalArgumentException if the component is null or if it depends on components that are not present in the container.
     */
    default void addComponent(EntityComp component) {
        componentContainer().addComponent(component);
    };

    /**
     * Removes a component from this container.
     * @param component the component to remove.
     * @throws IllegalArgumentException if the component is null or not present in the container.
     * @throws IllegalStateException if the component is a dependency for other components.
     */
    default void removeComponent(EntityComp component) {
        componentContainer().removeComponent(component);
    };

    /** * Removes all components from this container.
     * This method is called when the entity is removed.
     * It will attempt to remove components in a topological order based on their dependencies.
     * This method will call `onRemoved` for each component before removing it.
     * @throws IllegalArgumentException if components cannot be removed due to dependency issues.
     * @throws IllegalStateException if the components cannot be removed safely due to dependency issues.
     */
    default void removeAllComponents() {
        componentContainer().removeAllComponents();
    };
    /** * Returns the components in this container.
     * @return a copy of the internal components list.
     */
    default Seq<EntityComp> getComponents() {
        return componentContainer().components();
    }

    /** * Returns the component of the specified type.
     * @param compType the class of the component to retrieve.
     * @param <T> the type of the component.
     * @return the component of the specified type, or null if not found.
     * @throws IllegalArgumentException if the compType is null.
     */
    default <T> T getComponent(Class<T> compType) {
        return componentContainer().getComponent(compType);
    }

    /** * Returns all components of the specified type.
     * @param compType the class of the component to retrieve.
     * @param <T> the type of the component.
     * @return a sequence of components of the specified type, or an empty sequence if not found.
     * @throws IllegalArgumentException if the compType is null.
     */
    default <T> Seq<T> getComponents(Class<T> compType) {
        return componentContainer().getComponents(compType);
    }

    /** * Returns true if this container has a component of the specified type.
     * @param compType the class of the component to check.
     * @param <T> the type of the component.
     * @return true if a component of the specified type exists, false otherwise.
     * @throws IllegalArgumentException if the compType is null.
     */
    default <T> boolean hasComponent(Class<T> compType) {
        return componentContainer().hasComponent(compType);
    }
}
