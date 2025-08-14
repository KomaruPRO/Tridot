package pro.komaru.tridot.core.entity.ecs;

import net.minecraft.world.entity.Entity;
import pro.komaru.tridot.core.struct.Seq;
import pro.komaru.tridot.core.struct.enums.GameSide;

/**
 * Implementation of the EntityCompContainer interface.
 * This class manages components for a specific entity, allowing adding, removing, and retrieving components.
 * It ensures that components are added in a topological order based on their dependencies.
 */
public class EntityCompContainerImpl implements EntityCompContainer {
    private final Seq<EntityComp> components = Seq.empty();
    private Seq<EntityComp> componentsSortedPriority = null;
    private final Entity entity;

    public EntityCompContainerImpl(Entity entity) {
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
            throw new IllegalStateException("Component " + component.name() + " is not applicable for the current game side: " + GameSide.of(entity).toStringColored());

        boolean ignoreDeps = EntityComp.ignoreDependenciesSideOf(component.getClass());

        if(ignoreDeps && component.side() != GameSide.BOTH) {
            throw new IllegalArgumentException("You can not use \u001B[1m\u001B[33m@IgnoreDependenciesSide\u001B[0m on component which side isn't "+GameSide.BOTH.toStringColored()+".");
        }

        Seq<String> mismatchingSideDeps = Seq.with();
        for (Class<? extends EntityComp> dep : component.dependencies()) {
            if(ignoreDeps) continue;
            var depSide = EntityComp.sideOf(dep);
            if (!depSide.appliesStricti(component.side())) mismatchingSideDeps.add(dep.getSimpleName() + "\u001B[0m ["+depSide.toStringColored()+"]");
        }

        Seq<String> missingDeps = Seq.with();
        for (Class<? extends EntityComp> dep : component.dependencies()) {
            if(!EntityComp.sideOf(dep).applies(GameSide.of(entity)) && ignoreDeps) continue;
            boolean hasDep = components.contains(c -> c.getClass().equals(dep));
            if (!hasDep) missingDeps.add(dep.getSimpleName());
        }

        if (!mismatchingSideDeps.isEmpty())
            throw new IllegalArgumentException(
                    "Component " + component.name() +
                            " has dependencies that mismatch it's game side: [" + component.side().toStringColored() + "]\n" + mismatchingSideDeps.map(e -> "\u001B[1m- " + e+"\u001B[0m").toString("\n") +
                            (component.side() == GameSide.BOTH ? """
                                    \n| This component will meet these dependencies only on one of sides, so it should not have side-specific dependencies.
                                    | If you're sure it will not be a problem, use \u001B[1m\u001B[33m@IgnoreDependenciesSide\u001B[0m on your component class.""" : "")
            );
        if (!missingDeps.isEmpty())
            throw new IllegalArgumentException(
                    "Component " + component.name() +
                            " depends on missing components: \n" + missingDeps.map(e -> "\u001B[1m- " + e+"\u001B[0m").toString("\n")
            );

        components.add(component);
        componentsSortedPriority = null;
        component.assignEntity(entity);
        try {
            component.onAdded();
        } catch (Exception e) {
            components.remove(component);
            componentsSortedPriority = null;
            throw new IllegalStateException("Failed to add component " + component.name() + ": " + e.getMessage(), e);
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
            throw new IllegalArgumentException("Component " + component.name() + " is not present in the container.");
        }

        Class<? extends EntityComp> compClass = component.getClass();
        Seq<String> dependents = Seq.empty();
        for (EntityComp other : components) {
            if (other == component) continue;
            if (other.dependencies().contains(compClass)) {
                dependents.add(other.name());
            }
        }

        if (!dependents.isEmpty()) {
            throw new IllegalStateException(
                    "Cannot remove component " + component.name() +
                            " because it is a dependency for:\n" + dependents.map(e -> "\u001B[1m- " + e+"\u001B[0m").toString("\n")
            );
        }

        components.remove(component);
        componentsSortedPriority = null;
        component.onRemoved();
    }

    /** * Removes all components from this container.
     * This method is called when the entity is removed.
     * It will attempt to remove components in a topological order based on their dependencies.
     * This method will call `onRemoved` for each component before removing it.
     * @throws IllegalArgumentException if components cannot be removed due to dependency issues.
     * @throws IllegalStateException if the components cannot be removed safely due to dependency issues.
     */
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
            });
            for (int i = components.size-1; i >= 0; i--) {
                EntityComp comp = components.get(i);
                if (comp != null) removeComponent(comp);
            }
        } catch (IllegalArgumentException e) {
            components.each(EntityComp::onRemoved);
            components.clear();
            componentsSortedPriority = null;
            throw new IllegalArgumentException("Components cannot be removed safely due to dependency issues: " + e.getMessage());
        }
    }

    /** * Returns all components of the specified type.
     * @param compType the class of the component to retrieve.
     * @param <T> the type of the component.
     * @return a sequence of components of the specified type, or an empty sequence if not found.
     * @throws IllegalArgumentException if the compType is null.
     */
    @Override
    public <T> Seq<T> getComponents(Class<T> compType) {
        return components.select(c -> compType.equals(c.getClass()))
                .map(compType::cast);
    }

    @Override
    public Seq<EntityComp> componentsPriorityOrdered() {
        if(componentsSortedPriority == null)
            componentsSortedPriority = components.copy().sort((a, b) -> Integer.compare(b.priority(), a.priority()));
        return componentsSortedPriority;
    }



    /** * Returns the entity this container is assigned to.
     * @return the entity this container is assigned to.
     */
    public Entity entity() {
        return entity;
    }
    /** * Returns the components in this container.
     * @return a copy of the internal components list.
     */
    public Seq<EntityComp> components() {
        return components.copy();
    }

}
