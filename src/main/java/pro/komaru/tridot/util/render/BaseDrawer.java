package pro.komaru.tridot.util.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import pro.komaru.tridot.util.comps.render.gui.IGuiDrawer;

public class BaseDrawer implements IGuiDrawer {

    public GuiGraphics graphics;
    public PoseStack posestack;
    public String namespace;

    public BaseDrawer(GuiGraphics g, PoseStack s, String namespace) {
        this.graphics = g;
        this.posestack = s;
        this.namespace = namespace;
    }

    @Override
    public GuiGraphics graphics() {
        return graphics;
    }
    @Override
    public String namespace() {
        return namespace;
    }
    @Override
    public PoseStack stack() {
        return posestack;
    }
}
