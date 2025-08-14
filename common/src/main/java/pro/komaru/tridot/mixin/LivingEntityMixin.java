package pro.komaru.tridot.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pro.komaru.tridot.core.entity.ecs.EntityComp;
import pro.komaru.tridot.core.entity.ecs.EntityCompAccessor;
import pro.komaru.tridot.core.entity.ecs.EntityCompContainer;
import pro.komaru.tridot.core.struct.CallInfo;
import pro.komaru.tridot.core.struct.CallInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tridot$onTick(CallbackInfo ci) {
        EntityCompContainer compAccessor = (EntityCompContainer) this;
        CallInfo callInfo = new CallInfo();
        for (EntityComp comp : compAccessor.componentsPriorityOrdered()) {
            comp.onTick(callInfo);
            if(callInfo.isCancelled()) {
                ci.cancel();
                return;
            }
        }
    }
    @Inject(method = "die", at = @At("HEAD"))
    private void tridot$onDie(DamageSource damageSource, CallbackInfo ci) {
        EntityCompContainer compAccessor = (EntityCompContainer) this;
        for (EntityComp entityComp : compAccessor.componentsPriorityOrdered()) {
            entityComp.onDeath(damageSource);
        }
    }
    @Inject(method = "tickDeath", at = @At("HEAD"))
    private void tridot$onTickDeath(CallbackInfo ci) {
        EntityCompContainer compAccessor = (EntityCompContainer) this;
        for (EntityComp entityComp : compAccessor.componentsPriorityOrdered()) {
            entityComp.onDeathTick();
        }
    }
    @Inject(method = "tickEffects", at = @At("HEAD"))
    private void tridot$onTickEffects(CallbackInfo ci) {
        EntityCompContainer compAccessor = (EntityCompContainer) this;
        for (EntityComp entityComp : compAccessor.componentsPriorityOrdered()) {
            entityComp.onEffectsTick();
        }
    }
    @Inject(method = "onItemPickup", at = @At("HEAD"))
    private void tridot$onItemPickup(ItemEntity item, CallbackInfo ci) {
        EntityCompContainer compAccessor = (EntityCompContainer) this;
        for (EntityComp entityComp : compAccessor.componentsPriorityOrdered()) {
            entityComp.onItemPickup(item,item.getItem());
        }
    }
    @Inject(method = "hurt", at = @At("RETURN"), cancellable = true)
    private void tridot$onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        CallInfoReturnable<Boolean> callInfo = new CallInfoReturnable<>();
        EntityCompContainer compAccessor = (EntityCompContainer) this;
        for (EntityComp comp : compAccessor.componentsPriorityOrdered()) {
            comp.onHurt(source,amount,callInfo);
            if (callInfo.isCancelled() && callInfo.getReturnedValue() != null) {
                cir.setReturnValue(callInfo.getReturnedValue());
                return;
            }
            if(callInfo.isCancelled()) return;
        }
    }
}
