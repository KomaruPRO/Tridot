package pro.komaru.tridot.mixin.client;

import pro.komaru.tridot.client.animation.*;
import pro.komaru.tridot.core.interfaces.*;
import net.minecraft.client.model.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import pro.komaru.tridot.client.animation.ItemAnimation;
import pro.komaru.tridot.core.interfaces.ICustomAnimationItem;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity>{

    @Inject(at = @At("RETURN"), method = "setupAnim")
    public void tridot$setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci){
        HumanoidModel self = (HumanoidModel)((Object)this);
        if(entity instanceof Player player){
            for(InteractionHand hand : InteractionHand.values()){
                if(player.getItemInHand(hand).getItem() instanceof ICustomAnimationItem item){
                    ItemStack stack = player.getItemInHand(hand);
                    ItemAnimation animation = item.getAnimation(stack);
                    if(animation != null){
                        boolean use = true;
                        if(animation.isOnlyItemUse()) use = ItemAnimation.isItemUse(player) && player.getUsedItemHand() == hand;
                        if(use){
                            if(ItemAnimation.isRightHand(player, hand)){
                                item.getAnimation(stack).setupAnimRight(self, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                            }else{
                                item.getAnimation(stack).setupAnimLeft(self, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                            }
                            item.getAnimation(stack).setupAnim(self, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                        }
                    }
                }
            }
        }
    }
}
