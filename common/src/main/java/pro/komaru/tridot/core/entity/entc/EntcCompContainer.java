package pro.komaru.tridot.core.entity.entc;

import net.minecraft.world.entity.Entity;
import pro.komaru.tridot.core.struct.Seq;
import pro.komaru.tridot.core.struct.enums.GameSide;

public class EntcCompContainer {
    private final Seq<EntcComp> components = Seq.empty();
    private Seq<EntcComp> componentsSortedPriority = null;
    private final Entity entity;

    public EntcCompContainer(Entity entity) {
        if (entity == null) throw new IllegalArgumentException("Entity cannot be null.");
        this.entity = entity;
    }

    /** * Adds multiple components to this container.
     * The components will be added in a topological order based on their dependencies.
     * @param components the components to add.
     * @throws IllegalArgumentException if the components are null or if any component depends on components that are not present in the container.
     */
    public void addComponents(Seq<EntcComp> components) {
        if (components == null) throw new IllegalArgumentException("Components cannot be null.");
        try {
            components.topoSort(comp -> {
                Seq<EntcComp> deps = Seq.empty();
                for (EntcRegistry.EntcCompEntry depEntry : comp.dependencies()) {
                    EntcComp found = components.find(c -> c.getEntry().equals(depEntry));
                    if (found != null) deps.add(found);
                }
                return deps;
            });
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Components cannot be added due to dependency issues: " + e.getMessage());
        }
        for (EntcComp component : components) {
            if(component == null) continue;
            if(!component.side().applies(GameSide.of(entity))) continue;
            addComponent(component);
        }
    }

    /** * Adds a component to this container.
     * @param component the component to add.
     * @throws IllegalArgumentException if the component is null or if it depends on components that are not present in the container.
     */
    public void addComponent(EntcComp component) {
        if (component == null)
            throw new IllegalArgumentException("Component cannot be null.");
        if(!component.side().applies(GameSide.of(entity)))
            throw new IllegalStateException("Component " + component.name() + " is not applicable for the current game side: " + GameSide.of(entity).toStringColored());

        boolean ignoreDeps = component.getEntry().getProperties().isIgnoreDepsSide();

        if(ignoreDeps && component.side() != GameSide.BOTH) {
            throw new IllegalArgumentException("You can not use \u001B[1m\u001B[33m@IgnoreDependenciesSide\u001B[0m on component which side isn't "+GameSide.BOTH.toStringColored()+".");
        }

        Seq<String> mismatchingSideDeps = Seq.with();
        for (EntcRegistry.EntcCompEntry dep : component.dependencies()) {
            if(ignoreDeps) continue;
            var depSide = dep.getProperties().getSide();
            if (!depSide.appliesStricti(component.side())) mismatchingSideDeps.add(dep.getId().toString() + "\u001B[0m ["+depSide.toStringColored()+"]");
        }

        Seq<String> missingDeps = Seq.with();
        for (EntcRegistry.EntcCompEntry dep : component.dependencies()) {
            if(!dep.getProperties().getSide().applies(GameSide.of(entity)) && ignoreDeps) continue;
            boolean hasDep = components.contains(c -> c.getEntry().equals(dep));
            if (!hasDep) missingDeps.add(dep.getId().toString());
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
    public void removeComponent(EntcComp component) {
        if (component == null) {
            throw new IllegalArgumentException("Component cannot be null.");
        }
        if (!components.contains(c -> c.equals(component))) {
            throw new IllegalArgumentException("Component " + component.name() + " is not present in the container.");
        }

        EntcRegistry.EntcCompEntry compEntry = component.getEntry();
        Seq<String> dependents = Seq.empty();
        for (EntcComp other : components) {
            if (other == component) continue;
            if (other.dependencies().contains(compEntry)) {
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
    public void removeAllComponents() {
        try {
            components.topoSort(comp -> {
                Seq<EntcComp> deps = Seq.empty();
                for (EntcRegistry.EntcCompEntry entry : comp.dependencies()) {
                    EntcComp found = components.find(c -> c.getEntry().equals(entry));
                    if (found != null) deps.add(found);
                }
                return deps;
            });
            for (int i = components.size-1; i >= 0; i--) {
                EntcComp comp = components.get(i);
                if (comp != null) removeComponent(comp);
            }
        } catch (IllegalArgumentException e) {
            components.each(EntcComp::onRemoved);
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
    public <T> Seq<T> getComponents(Class<T> compType) {
        return components.select(c -> compType.equals(c.getClass()))
                .map(compType::cast);
    }
    /** * Returns the component of the specified type.
     * @param compType the class of the component to retrieve.
     * @param <T> the type of the component.
     * @return the component of the specified type, or null if not found.
     * @throws IllegalArgumentException if the compType is null.
     */
    public <T> T getComponent(Class<T> compType) {
        return getComponents(compType).firstOpt();
    };

    /** * Returns true if this container has a component of the specified type.
     * @param compType the class of the component to check.
     * @param <T> the type of the component.
     * @return true if a component of the specified type exists, false otherwise.
     * @throws IllegalArgumentException if the compType is null.
     */
    public <T> boolean hasComponent(Class<T> compType) {
        return getComponent(compType) != null;
    }

    /** * Returns the copy of container's components sorted by their priority
     * Components with higher priority will be processed first.
     * @return a copy of internal components ordered by their priority.
     */
    public Seq<EntcComp> componentsPriorityOrdered() {
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
    public Seq<EntcComp> components() {
        return components.copy();
    }

}
