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

    @Unique private float tridot$lastArmorValue = -1.0F;
    @Unique private String tridot$cachedArmorText = "";

    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true, remap = false)
    protected void renderArmor(GuiGraphics guiGraphics, int width, int height, CallbackInfo ci) {
        float currentArmor = (float) tridot$self().getMinecraft().player.getAttributeValue(AttributeRegistry.PERCENT_ARMOR.get());
        if (currentArmor > 0 && CommonConfig.PERCENT_ARMOR.get()) {
            int left = width / 2 - 91;
            int top = height - tridot$self().leftHeight;

            guiGraphics.blit(GUI_ICONS_LOCATION, left, top - 1, 34, 9, 9, 9);
            if (Math.abs(currentArmor - tridot$lastArmorValue) > 0.01F) {
                String formattedValue = String.format("%.1f%%", currentArmor);
                tridot$cachedArmorText = I18n.get("tooltip.tridot.value", formattedValue);
                tridot$lastArmorValue = currentArmor;
            }

            guiGraphics.drawString(tridot$self().getMinecraft().font, tridot$cachedArmorText, left + 10, top, 0xFFFFFF);

            tridot$self().leftHeight += 10;
            ci.cancel();
        }
    }

    @Unique
    ForgeGui tridot$self() {
        return (ForgeGui) ((Object) this);
    }

}
