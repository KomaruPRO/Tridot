package pro.komaru.tridot.client.gui.components;

import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.resources.*;
import net.minecraftforge.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class TridotLogoRenderer extends LogoRenderer{
    public ResourceLocation logo;

    public TridotLogoRenderer(ResourceLocation logo, boolean keepLogoThroughFade){
        super(keepLogoThroughFade);
        this.logo = logo;
    }

    @Override
    public void renderLogo(GuiGraphics guiGraphics, int screenWidth, float transparency){
        this.renderLogo(guiGraphics, screenWidth, transparency, 30);
    }

    @Override
    public void renderLogo(GuiGraphics guiGraphics, int screenWidth, float transparency, int height){
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.keepLogoThroughFade ? 1.0F : transparency);
        int i = screenWidth / 2 - 128;
        guiGraphics.blit(logo, i, height, 0.0F, 0.0F, 256, 64, 256, 64);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
