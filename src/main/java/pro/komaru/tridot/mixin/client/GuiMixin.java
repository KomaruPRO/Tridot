package pro.komaru.tridot.mixin.client;

import net.minecraft.client.gui.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import pro.komaru.tridot.client.render.gui.particle.*;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(at = @At("HEAD"), method = "renderHotbar")
    private void tridot$renderHotbarStart(float pPartialTick, GuiGraphics pGuiGraphics, CallbackInfo ci) {
        ScreenParticleHandler.renderingHotbar = true;
    }

    @Inject(at = @At("RETURN"), method = "renderHotbar")
    private void tridot$renderHotbarEnd(float pPartialTick, GuiGraphics pGuiGraphics, CallbackInfo ci) {
        ScreenParticleHandler.renderingHotbar = false;
    }
}