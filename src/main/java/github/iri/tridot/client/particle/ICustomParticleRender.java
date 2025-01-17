package github.iri.tridot.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;

public interface ICustomParticleRender{
    void render(PoseStack poseStack, MultiBufferSource buffer, float partialTicks);
}
