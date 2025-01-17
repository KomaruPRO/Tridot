package github.iri.tridot.mixin.client;

import github.iri.tridot.client.render.*;
import github.iri.tridot.common.interfaces.*;
import github.iri.tridot.config.*;
import github.iri.tridot.registry.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin{

    @Inject(at = @At(value = "TAIL"), method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V")
    private void fluffy_fur$renderItem(LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset, CallbackInfo ci){
        if(ClientConfig.ITEM_GUI_PARTICLE.get()){
            if(stack.getItem() instanceof IGuiParticleItem guiParticleItem){
                GuiGraphics self = (GuiGraphics)((Object)this);
                guiParticleItem.renderParticle(self.pose(), entity, level, stack, x, y, seed, guiOffset);
            }
        }

        for(RenderBuilder builder : TridotRenderTypes.customItemRenderBuilderGui){
            builder.endBatch();
        }
        TridotRenderTypes.customItemRenderBuilderGui.clear();
    }
}
