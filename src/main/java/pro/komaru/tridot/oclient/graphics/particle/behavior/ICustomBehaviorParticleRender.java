package pro.komaru.tridot.oclient.graphics.particle.behavior;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;
import pro.komaru.tridot.oclient.graphics.particle.GenericParticle;

public interface ICustomBehaviorParticleRender{
    void render(GenericParticle particle, PoseStack poseStack, MultiBufferSource buffer, float partialTicks);
}
