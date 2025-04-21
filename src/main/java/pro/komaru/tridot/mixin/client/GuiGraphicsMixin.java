package pro.komaru.tridot.mixin.client;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import pro.komaru.tridot.api.interfaces.*;
import pro.komaru.tridot.client.render.*;
import pro.komaru.tridot.client.render.gui.particle.*;
import pro.komaru.tridot.common.config.ClientConfig;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin{
    @Shadow
    @Final
    private PoseStack pose;

    @Inject(at = @At(value = "TAIL"), method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V")
    private void tridot$renderItem(LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset, CallbackInfo ci){
        if(ClientConfig.ITEM_GUI_PARTICLE.get()){
            if(stack.getItem() instanceof IGuiRenderItem guiParticleItem){
                GuiGraphics self = (GuiGraphics)((Object)this);
                guiParticleItem.onGuiRender(self, entity, level, stack, x, y, seed, guiOffset);
            }
        }

        for(RenderBuilder builder : TridotRenderTypes.customItemRenderBuilderGui){
            builder.endBatch();
        }

        TridotRenderTypes.customItemRenderBuilderGui.clear();
    }

    @Inject(at = @At(value = "HEAD"), method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V")
    private void tridot$renderGuiItem(LivingEntity entity, Level level, ItemStack stack, int x, int y, int p_283260_, int p_281995_, CallbackInfo ci) {
        ScreenParticleHandler.renderItemStackEarly(pose, stack, x, y);
    }

    @Inject(at = @At(value = "TAIL"), method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V")
    private void tridot$renderGuiItemLate(LivingEntity pEntity, Level pLevel, ItemStack pStack, int pX, int pY, int pSeed, int pGuiOffset, CallbackInfo ci) {
        ScreenParticleHandler.renderItemStackLate();
    }
}
