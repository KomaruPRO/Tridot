package pro.komaru.tridot.mixin.client;

import com.mojang.blaze3d.vertex.*;
import pro.komaru.tridot.client.animation.*;
import pro.komaru.tridot.core.interfaces.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import pro.komaru.tridot.client.animation.ItemAnimation;
import pro.komaru.tridot.core.interfaces.ICustomAnimationItem;

@Mixin(ItemInHandLayer.class)
public abstract class ItemInHandLayerMixin{

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"), method = "renderArmWithItem")
    public void tridot$renderArmWithItem(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext displayContext, HumanoidArm arm, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci){
        if(itemStack.getItem() instanceof ICustomAnimationItem item){
            ItemAnimation animation = item.getAnimation(itemStack);
            if(animation != null){
                boolean use = true;
                if(animation.isOnlyItemUse()) use = ItemAnimation.isItemUse(livingEntity) && ItemAnimation.isSameHand(livingEntity.getMainArm(), arm, livingEntity.getUsedItemHand());
                if(use) item.getAnimation(itemStack).renderArmWithItem(livingEntity, itemStack, displayContext, arm, poseStack, buffer, packedLight);
            }
        }
    }
}
