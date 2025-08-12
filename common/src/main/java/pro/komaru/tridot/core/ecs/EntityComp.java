package pro.komaru.tridot.core.ecs;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import pro.komaru.tridot.core.anno.CompAnnotations;
import pro.komaru.tridot.core.struct.Seq;
import pro.komaru.tridot.core.struct.enums.GameSide;

/**
 * Base class for components that can be assigned to entities.
 * Components are used to add functionality to entities in a modular way.
 * Each component can have its own priority, game side, dependencies, and lifecycle methods.
 */
public abstract class EntityComp {
    private Entity entity;


    /** * Returns the priority of this component.
     * Components with higher priority process events first.
     * Please use appropriate priority values to ensure correct behavior and further expansion.
     * @return the priority of this component, default is 0.
     */
    public int priority() {
        return 0;
    }

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
    public final Seq<Class<? extends EntityComp>> dependencies() {
        return dependenciesOf(getClass());
    }
    /** * Returns the game side on which this component should be applied.
     * This is used to determine whether the component should be added on the client, server, or both.
     * @return the game side for this component, default is GameSide.BOTH.
     */
    public final GameSide side() {
        return sideOf(getClass());
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
        return name();
    }

    public String name() {
        return getClass().getSimpleName();
    }

    public static Seq<Class<? extends EntityComp>> dependenciesOf(Class<? extends EntityComp> compClass) {
        if(compClass == null) return Seq.empty();
        CompAnnotations.DependsOn dependsOn = compClass.getAnnotation(CompAnnotations.DependsOn.class);
        if(dependsOn == null) return Seq.empty();
        return Seq.with(dependsOn.value());
    }
    public static GameSide sideOf(Class<? extends EntityComp> compClass) {
        if(!compClass.isAnnotationPresent(CompAnnotations.ApplyOn.class)) return GameSide.BOTH;
        CompAnnotations.ApplyOn applyOn = compClass.getAnnotation(CompAnnotations.ApplyOn.class);
        if(applyOn == null) return GameSide.BOTH;
        return applyOn.value();
    }
    public static boolean ignoreDependenciesSideOf(Class<? extends EntityComp> dep) {
        return dep.isAnnotationPresent(CompAnnotations.IgnoreDependenciesSide.class);
    }
}