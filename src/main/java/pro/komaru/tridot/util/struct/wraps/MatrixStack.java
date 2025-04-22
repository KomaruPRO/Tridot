package pro.komaru.tridot.util.struct.wraps;

import com.mojang.blaze3d.vertex.PoseStack;
import pro.komaru.tridot.util.comps.render.RenderStackc;

public class MatrixStack implements RenderStackc {
    public PoseStack stack;

    public MatrixStack(PoseStack stack) {
        this.stack = stack;
    }

    @Override
    public PoseStack stack() {
        return stack;
    }
}
