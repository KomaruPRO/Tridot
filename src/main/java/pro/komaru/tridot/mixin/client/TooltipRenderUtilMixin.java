package pro.komaru.tridot.mixin.client;

import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.inventory.tooltip.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import pro.komaru.tridot.api.*;

@Mixin(TooltipRenderUtil.class)
public class TooltipRenderUtilMixin{

    @Inject(method = "renderTooltipBackground(Lnet/minecraft/client/gui/GuiGraphics;IIIIIIIII)V", at = @At("HEAD"), remap = false)
    private static void getTooltipSize(GuiGraphics pGuiGraphics, int pX, int pY, int pWidth, int pHeight, int pZ, int backgroundTop, int backgroundBottom, int borderTop, int borderBottom, CallbackInfo ci) {
        TooltipTracker.setSize(pWidth, pHeight);
        TooltipTracker.setPos(pX, pY);
    }
}