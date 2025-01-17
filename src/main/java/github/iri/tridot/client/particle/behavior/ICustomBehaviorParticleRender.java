package github.iri.tridot.client.particle.behavior;

import com.mojang.blaze3d.vertex.*;
import github.iri.tridot.client.particle.*;
import net.minecraft.client.renderer.*;

public interface ICustomBehaviorParticleRender{
    void render(GenericParticle particle, PoseStack poseStack, MultiBufferSource buffer, float partialTicks);
}
