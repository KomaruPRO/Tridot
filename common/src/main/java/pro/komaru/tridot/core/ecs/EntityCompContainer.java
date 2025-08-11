package pro.komaru.tridot.core.ecs;

import net.minecraft.world.entity.Entity;
import pro.komaru.tridot.core.struct.Seq;
import pro.komaru.tridot.core.struct.enums.GameSide;

public class EntityCompContainer implements EntityCompAccessor {
    private Seq<EntityComp> components = Seq.empty();
    private Entity entity;

    public EntityCompContainer(Entity entity) {
        if (entity == null) throw new IllegalArgumentException("Entity cannot be null.");
        this.entity = entity;
    }

    /** * Adds multiple components to this container.
     * The components will be added in a topological order based on their dependencies.
     * @param components the components to add.
     * @throws IllegalArgumentException if the components are null or if any component depends on components that are not present in the container.
     */
    @Override
    public void addComponents(Seq<EntityComp> components) {
        if (components == null) throw new IllegalArgumentException("Components cannot be null.");
        try {
            components.topoSort(comp -> {
                Seq<EntityComp> deps = Seq.empty();
                for (Class<? extends EntityComp> depClass : comp.dependencies()) {
                    EntityComp found = components.find(c -> depClass.isAssignableFrom(c.getClass()));
                    if (found != null) deps.add(found);
                }
                return deps;
            });
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Components cannot be added due to dependency issues: " + e.getMessage());
        }
        for (EntityComp component : components) {
            if(component == null) continue;
            if(!component.side().applies(GameSide.of(entity))) continue;
            addComponent(component);
        }
    }

    /** * Adds a component to this container.
     * @param component the component to add.
     * @throws IllegalArgumentException if the component is null or if it depends on components that are not present in the container.
     */
    @Override
    public void addComponent(EntityComp component) {
        if (component == null)
            throw new IllegalArgumentException("Component cannot be null.");
        if(!component.side().applies(GameSide.of(entity)))
            throw new IllegalStateException("Component " + component.getClass().getSimpleName() + " is not applicable for the current game side: " + GameSide.of(entity));

        Seq<String> missingDeps = Seq.with();
        for (Class<? extends EntityComp> dep : component.dependencies()) {
            boolean hasDep = components.contains(c -> c.getClass().equals(dep));
            if (!hasDep) {
                missingDeps.add(dep.getSimpleName());
            }
        }

        if (!missingDeps.isEmpty())
            throw new IllegalArgumentException(
                    "Component " + component.getClass().getSimpleName() +
                            " depends on missing components: " + String.join(", ", missingDeps)
            );

        components.add(component);
        component.assignEntity(entity);
        try {
            component.onAdded();
        } catch (Exception e) {
            components.remove(component);
            throw new IllegalStateException("Failed to add component " + component.getClass().getSimpleName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Removes a component from this container.
     * @param component the component to remove.
     * @throws IllegalArgumentException if the component is null or not present in the container.
     * @throws IllegalStateException if the component is a dependency for other components.
     */
    @Override
    public void removeComponent(EntityComp component) {
        if (component == null) {
            throw new IllegalArgumentException("Component cannot be null.");
        }
        if (!components.contains(c -> c.equals(component))) {
            throw new IllegalArgumentException("Component " + component.getClass().getSimpleName() + " is not present in the container.");
        }

        Class<? extends EntityComp> compClass = component.getClass();
        Seq<String> dependents = Seq.empty();
        for (EntityComp other : components) {
            if (other == component) continue;
            if (other.dependencies().contains(compClass)) {
                dependents.add(other.getClass().getSimpleName());
            }
        }

        if (!dependents.isEmpty()) {
            throw new IllegalStateException(
                    "Cannot remove component " + compClass.getSimpleName() +
                            " because it is a dependency for: " + String.join(", ", dependents)
            );
        }

        components.remove(component);
        component.onRemoved();
    }

    @Override
    public void removeAllComponents() {
        try {
            components.topoSort(comp -> {
                Seq<EntityComp> deps = Seq.empty();
                for (Class<? extends EntityComp> depClass : comp.dependencies()) {
                    EntityComp found = components.find(c -> depClass.isAssignableFrom(c.getClass()));
                    if (found != null) deps.add(found);
                }
                return deps;
            }).reverse();
            components.each(this::removeComponent);
        } catch (IllegalArgumentException e) {
            components.each(EntityComp::onRemoved);
            components.clear();
            throw new IllegalArgumentException("Components cannot be removed safely due to dependency issues: " + e.getMessage());
        }
    }

    /** * Returns the entity this container is assigned to.
     * @return the entity this container is assigned to.
     */
    public Entity entity() {
        return entity;
    }
    /** * Returns the components in this container.
     * @return a sequence of components in this container.
     */
    public Seq<EntityComp> components() {
        return components;
    }
}
