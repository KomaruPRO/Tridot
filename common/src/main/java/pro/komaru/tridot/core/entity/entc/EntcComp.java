package pro.komaru.tridot.core.entity.entc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import pro.komaru.tridot.core.struct.CallInfo;
import pro.komaru.tridot.core.struct.CallInfoReturnable;
import pro.komaru.tridot.core.struct.Seq;
import pro.komaru.tridot.core.struct.enums.GameSide;

import java.util.Objects;

/**
 * Base class for components that can be assigned to entities.
 * Components are used to add functionality to entities in a modular way.
 * Each component can have its own priority, game side, dependencies, and lifecycle methods.
 */
public abstract class EntcComp implements EntcDispatcher {
    private Entity entity;
    private EntcRegistry.EntcCompEntry entry;

    /** Returns the priority of this component.
     * Components with higher priority process events first.
     * Please use appropriate priority values to ensure correct behavior and further expansion.
     * @return the priority of this component, default is 0.
     */
    public int priority() {
        return 0;
    }




    /** Called when this component is added to an entity and the entity was assigned. */
    public void onAdded() {}
    /** Called when this component is removed from an entity. */
    public void onRemoved() {}
    /** Called every tick for this component. */
    public void onTick(CallInfo callInfo) {}
    /** Called when the entity is hurt.
     * @param source the source of the damage.
     * @param amount the amount of damage taken.
     * @param call a CallInfoReturnable object that can be used to cancel the event or return a value.
     * @see Entity#hurt(DamageSource, float)
     */
    public void onHurt(DamageSource source, float amount, CallInfoReturnable<Boolean> call) {}
    /** Called when the entity dies.
     * @param damageSource the source of the damage that caused the death.
     * @see LivingEntity#die(DamageSource)
     */
    public void onDeath(DamageSource damageSource) {}
    /** Called every tick when the entity is dead.
     * This method is called after the entity has been marked as dead, but before it is removed from the world.
     * It can be used to perform cleanup or final actions before the entity is removed.
     * @see LivingEntity#tickDeath()
     */
    public void onDeathTick() {}
    /** Called when the entity is loaded from NBT.
     * This method can be used to read custom data from the NBT tag.
     * @param nbt the NBT tag to read data from.
     */
    public void onDataLoad(CompoundTag nbt) {}
    /** Called when the entity is saved to NBT.
     * This method can be used to write custom data to the NBT tag.
     * @param nbt the NBT tag to write data to.
     */
    public void onDataWrite(CompoundTag nbt) {}
    /** Called every tick for effects processing.
     * This method is called after all components have been processed for the tick.
     * It can be used to apply effects or perform actions that depend on other components.
     */
    public void onEffectsTick() {}
    /** Called when the entity has collided with other entities or blocks.
     * @param entities the entities that this entity has collided with.
     * @param direction the direction of the movement.
     * @param collidedDirection the direction of the movement, stopped by collision.
     */
    public void onCollision(Seq<Entity> entities, Vec3 direction, Vec3 collidedDirection) {}
    /** Called when entity picks up an item.
     * @param itemEntity the ItemEntity that was picked up.
     * @param stack the ItemStack that was picked up.
     */
    public void onItemPickup(ItemEntity itemEntity, ItemStack stack) {}



    /** Returns the dependencies of this component.
     * Components that are listed here will be processed before this component.
     * @return a sequence of component classes that this component depends on, default is an empty sequence.
     */
    public final Seq<EntcRegistry.EntcCompEntry> dependencies() {
        return getEntry().getProperties().getDependencies().map(EntcRegistry::get).select(Objects::nonNull);
    }
    /** Returns the game side on which this component should be applied.
     * This is used to determine whether the component should be added on the client, server, or both.
     * @return the game side for this component, default is GameSide.BOTH.
     */
    public final GameSide side() {
        return getEntry().getProperties().getSide();
    }

    /** Returns the entity this component is assigned to.
     * @throws IllegalStateException if the entity is not set.
     */
    public Entity getEntity() {
        if(entity == null) throw new IllegalStateException("Entity is not set for this component.");
        return entity;
    }
    /** Assigns this component to an entity.
     * @param entity the entity to assign this component to.
     * @throws IllegalStateException if the entity is already set for this component.
     * @throws IllegalArgumentException if the entity is null.
     */
    public void assignEntity(Entity entity) {
        if(this.entity != null) throw new IllegalStateException("Entity is already set for this component.");
        if(entity == null) throw new IllegalArgumentException("Entity cannot be null.");

        this.entity = entity;
    }
    /** Returns the entry of this component
     * @throws IllegalStateException if the entry is not set.
     */
    public EntcRegistry.EntcCompEntry getEntry() {
        if(entry == null) throw new IllegalStateException("Entry is not set for this component.");
        return entry;
    }
    /** Assigns this component's entry.
     * @param entry the entry of this component
     * @throws IllegalStateException if the entry is already set for this component.
     * @throws IllegalArgumentException if the entry is null.
     */
    public void assignEntity(EntcRegistry.EntcCompEntry entry) {
        if(this.entry != null) throw new IllegalStateException("Entry is already set for this component.");
        if(entry == null) throw new IllegalArgumentException("Entry cannot be null.");

        this.entry = entry;
    }

    @Override
    public String toString() {
        return name();
    }

    public String name() {
        return getEntry().getId().toString();
    }

    @Override
    public EntcCompContainer componentContainer() {
        return ((EntcDispatcher) entity).componentContainer();
    }
}