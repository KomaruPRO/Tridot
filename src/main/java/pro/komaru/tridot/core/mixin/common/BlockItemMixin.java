package pro.komaru.tridot.core.mixin.common;

import net.minecraft.nbt.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import pro.komaru.tridot.core.interfaces.*;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin{

    @Inject(at = @At("RETURN"), method = "getBlockEntityData", cancellable = true)
    private static void tridot$getBlockEntityData(ItemStack stack, CallbackInfoReturnable<CompoundTag> cir){
        if(stack.getItem() instanceof ICustomBlockEntityDataItem customBlockEntityDataItem){
            CompoundTag tileNbt = cir.getReturnValue();
            if(tileNbt == null) tileNbt = new CompoundTag();
            cir.setReturnValue(customBlockEntityDataItem.getCustomBlockEntityData(stack, tileNbt));
        }
    }
}
