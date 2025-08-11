package pro.komaru.tridot.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pro.komaru.tridot.core.ecs.*;
import pro.komaru.tridot.core.struct.Seq;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityCompAccessor {
    @Unique
    private final EntityCompContainer tridot$componentContainer = new EntityCompContainer((Entity) (Object) this);

    @Override public Entity entity() {return tridot$componentContainer.entity();}
    @Override public Seq<EntityComp> components() {return tridot$componentContainer.components();}

    @Override public void addComponents(Seq<EntityComp> components) {tridot$componentContainer.addComponents(components);}
    @Override public void addComponent(EntityComp component) {tridot$componentContainer.addComponent(component);}
    @Override public void removeComponent(EntityComp component) {tridot$componentContainer.removeComponent(component);}

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

    @Inject(method = "tick", at = @At("HEAD"))
    private void tridot$onTick(CallbackInfo ci) {
        components().each(EntityComp::onTick);
    }
}
