package pro.komaru.tridot.client.gfx.particle.behavior;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;
import pro.komaru.tridot.client.gfx.particle.GenericParticle;

public interface ICustomBehaviorParticleRender{
    void render(GenericParticle particle, PoseStack poseStack, MultiBufferSource buffer, float partialTicks);
}
