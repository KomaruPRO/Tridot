package pro.komaru.tridot.mixin.client;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.player.*;
import net.minecraft.client.renderer.*;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import pro.komaru.tridot.client.render.animation.ItemAnimation;
import pro.komaru.tridot.client.render.item.bow.BowHandler;
import pro.komaru.tridot.core.interfaces.ICustomAnimationItem;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin{

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"), method = "renderArmWithItem")
    public void tridot$renderArmWithItem(AbstractClientPlayer player, float partialTicks, float pitch, InteractionHand hand, float swingProgress, ItemStack stack, float equippedProgress, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, CallbackInfo ci){
        if(stack.getItem() instanceof ICustomAnimationItem item){
            ItemAnimation animation = item.getAnimation(stack);
            if(animation != null){
                boolean use = true;
                if(animation.isOnlyItemUse()) use = ItemAnimation.isItemUse(player) && player.getUsedItemHand() == hand;
                if(use) item.getAnimation(stack).renderArmWithItem(player, partialTicks, pitch, hand, swingProgress, stack, equippedProgress, poseStack, buffer, combinedLight);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "evaluateWhichHandsToRender", cancellable = true)
    private static void tridot$evaluateWhichHandsToRender(LocalPlayer pPlayer, CallbackInfoReturnable<ItemInHandRenderer.HandRenderSelection> cir){
        ItemStack itemStack = pPlayer.getUseItem();
        InteractionHand hand = pPlayer.getUsedItemHand();
        for(Item item : BowHandler.getBows()){
            if(itemStack.is(item)){
                cir.setReturnValue(ItemInHandRenderer.HandRenderSelection.onlyForHand(hand));
            }
        }
    }
}
