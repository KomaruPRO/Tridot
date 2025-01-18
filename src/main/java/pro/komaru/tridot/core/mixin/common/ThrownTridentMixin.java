package pro.komaru.tridot.core.mixin.common;

import net.minecraft.network.syncher.*;
import net.minecraft.world.entity.projectile.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin{

    @Shadow
    private boolean dealtDamage;

    @Final
    @Shadow
    private static EntityDataAccessor<Byte> ID_LOYALTY;

    @Inject(at = @At("HEAD"), method = "tick")
    public void tridot$tick(CallbackInfo ci){
        ThrownTrident self = (ThrownTrident)((Object)this);
        int i = self.getEntityData().get(ID_LOYALTY);
        if(i > 0 && self.position().y() < self.level().dimensionType().minY() - 32){
            dealtDamage = true;
        }
    }
}
