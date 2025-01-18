package pro.komaru.tridot.client.particle.behavior;

import com.mojang.blaze3d.vertex.*;
import pro.komaru.tridot.client.particle.*;
import net.minecraft.client.renderer.*;
import pro.komaru.tridot.client.particle.GenericParticle;

public interface ICustomBehaviorParticleRender{
    void render(GenericParticle particle, PoseStack poseStack, MultiBufferSource buffer, float partialTicks);
}
