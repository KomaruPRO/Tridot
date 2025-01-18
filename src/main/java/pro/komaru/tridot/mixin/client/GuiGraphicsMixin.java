package pro.komaru.tridot.mixin.client;

import pro.komaru.tridot.client.*;
import pro.komaru.tridot.client.render.*;
import pro.komaru.tridot.core.config.*;
import pro.komaru.tridot.core.interfaces.*;
import net.minecraft.client.gui.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import pro.komaru.tridot.client.TridotRenderTypes;
import pro.komaru.tridot.client.render.RenderBuilder;
import pro.komaru.tridot.core.config.ClientConfig;
import pro.komaru.tridot.core.interfaces.IGuiParticleItem;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin{

    @Inject(at = @At(value = "TAIL"), method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V")
    private void tridot$renderItem(LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset, CallbackInfo ci){
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
