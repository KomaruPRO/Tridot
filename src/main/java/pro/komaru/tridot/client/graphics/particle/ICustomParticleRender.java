package pro.komaru.tridot.client.graphics.particle;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;

public interface ICustomParticleRender{
    void render(PoseStack poseStack, MultiBufferSource buffer, float partialTicks);
}
