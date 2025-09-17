package pro.komaru.tridot.mixin.client;

import com.mojang.blaze3d.systems.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.resources.language.*;
import net.minecraftforge.client.gui.overlay.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import pro.komaru.tridot.common.config.*;
import pro.komaru.tridot.common.registry.item.*;

import java.awt.*;

import static pro.komaru.tridot.common.Events.GUI_ICONS_LOCATION;

@Mixin(ForgeGui.class)
public class ForgeGuiMixin{

    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true, remap = false)
    protected void renderArmor(GuiGraphics guiGraphics, int width, int height, CallbackInfo ci) {
        tridot$self().getMinecraft().getProfiler().push("armor");
        RenderSystem.enableBlend();
        int left = width / 2 - 91;
        int top = height - tridot$self().leftHeight;
        float armor = (float)tridot$self().getMinecraft().player.getAttributeValue(AttributeRegistry.PERCENT_ARMOR.get());
        if(armor > 0 && CommonConfig.PERCENT_ARMOR.get()){
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableDepthTest();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);

            RenderSystem.disableBlend();
            String component = I18n.get("tooltip.tridot.value", String.format("%.1f%%", armor));
            guiGraphics.blit(GUI_ICONS_LOCATION, left, top - 1, 34, 9, 9, 9);
            guiGraphics.drawString(tridot$self().getMinecraft().font, component, left + 10, top, Color.WHITE.getRGB());
            tridot$self().leftHeight += 10;
            RenderSystem.disableBlend();
            ci.cancel();
        }

        tridot$self().getMinecraft().getProfiler().pop();
    }

    @Unique
    ForgeGui tridot$self() {
        return (ForgeGui) ((Object) this);
    }

}
