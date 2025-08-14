package pro.komaru.tridot.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import pro.komaru.tridot.core.entity.ecs.*;
import pro.komaru.tridot.core.struct.CallInfo;
import pro.komaru.tridot.core.struct.CallInfoReturnable;
import pro.komaru.tridot.core.struct.Seq;

import java.util.List;
import java.util.function.Predicate;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityCompContainer, EntityCompAccessor {
    @Shadow private Level level;

    @Shadow public abstract boolean canCollideWith(Entity entity);

    @Unique
    private final EntityCompContainerImpl tridot$componentContainer = new EntityCompContainerImpl((Entity) (Object) this);

    @Override public Entity entity() {return tridot$componentContainer.entity();}
    @Override public Seq<EntityComp> components() {return tridot$componentContainer.components();}

    @Override public void addComponents(Seq<EntityComp> components) {tridot$componentContainer.addComponents(components);}
    @Override public void addComponent(EntityComp component) {tridot$componentContainer.addComponent(component);}
    @Override public void removeComponent(EntityComp component) {tridot$componentContainer.removeComponent(component);}

    @Override public void removeAllComponents() {tridot$componentContainer.removeAllComponents();}
    @Override public <T> T getComponent(Class<T> compType) {return tridot$componentContainer.getComponent(compType);}
    @Override public <T> Seq<T> getComponents(Class<T> compType) {return tridot$componentContainer.getComponents(compType);}
    @Override public <T> boolean hasComponent(Class<T> compType) {return tridot$componentContainer.hasComponent(compType);}

    @Override public Seq<EntityComp> componentsPriorityOrdered() {return tridot$componentContainer.componentsPriorityOrdered();}

    @Inject(method = "<init>", at = @At("RETURN"))
    private void tridot$onInit(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        Seq<EntityCompsInitializer> initializers = EntityCompRegistry.getInitializersFor(entity.getClass());
        if (initializers == null || initializers.isEmpty()) return;

        Seq<EntityComp> components = initializers.map(e -> e.get(entity)).flatten();
        if (components != null && !components.isEmpty())
            tridot$componentContainer.addComponents(components);
    }
    @Inject(method = "remove", at = @At("HEAD"))
    private void tridot$onRemove(CallbackInfo ci) {
        tridot$componentContainer.removeAllComponents();
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tridot$onTick(CallbackInfo ci) {
        if((Object) this instanceof LivingEntity) return;
        CallInfo callInfo = new CallInfo();
        for (EntityComp comp : componentsPriorityOrdered()) {
            comp.onTick(callInfo);
            if(callInfo.isCancelled()) {
                ci.cancel();
                return;
            }
        }
    }

    @Inject(method = "hurt", at = @At("RETURN"), cancellable = true)
    private void tridot$onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if((Object) this instanceof LivingEntity) return;
        CallInfoReturnable<Boolean> callInfo = new CallInfoReturnable<>();
        for (EntityComp comp : componentsPriorityOrdered()) {
            comp.onHurt(source,amount,callInfo);
            if (callInfo.isCancelled() && callInfo.getReturnedValue() != null) {
                cir.setReturnValue(callInfo.getReturnedValue());
                return;
            }
            if(callInfo.isCancelled()) return;
        }
    }

    @Inject(method = "saveWithoutId", at = @At("TAIL"))
    private void tridot$afterSaveWithoutId(CompoundTag nbt, CallbackInfoReturnable<CompoundTag> cir) {
        for (EntityComp comp : componentsPriorityOrdered()) {
            comp.onDataWrite(nbt);
        }
    }
    @Inject(method = "load", at = @At("TAIL"))
    private void tridot$onAdditionalDataRead(CompoundTag nbt, CallbackInfo ci) {
        for (EntityComp comp : componentsPriorityOrdered()) {
            comp.onDataLoad(nbt);
        }
    }

    @Inject(method = "collide", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void tridot$onCollide(Vec3 vec, CallbackInfoReturnable<Vec3> cir, AABB aabb, List<VoxelShape> list, Vec3 vec3) {
        if(vec3.distanceToSqr(vec) < 1.0E-7) return;
        Predicate<Entity> collisionPredicate = EntitySelector.NO_SPECTATORS.and(this::canCollideWith);
        List<Entity> entities = level.getEntities((Entity) (Object) this, aabb.expandTowards(vec).inflate(1.0E-7), collisionPredicate);
        for (EntityComp comp : componentsPriorityOrdered()) {
            comp.onCollision(Seq.with(entities),vec,vec3);
        }
    }
}
