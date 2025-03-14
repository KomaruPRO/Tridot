package pro.komaru.tridot.client.gfx.particle.behavior;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import pro.komaru.tridot.client.gfx.TridotParticles;
import pro.komaru.tridot.client.gfx.particle.GenericParticle;
import pro.komaru.tridot.client.gfx.particle.data.SpinParticleData;
import pro.komaru.tridot.client.render.RenderBuilder;
import pro.komaru.tridot.util.phys.Vec3;

public class CubeParticleBehavior extends ParticleBehavior implements ICustomBehaviorParticleRender{

    public CubeParticleBehavior(SpinParticleData xSpinData, SpinParticleData ySpinData, SpinParticleData zSpinData, float xOffset, float yOffset, float zOffset, boolean firstSide, boolean secondSide, boolean camera, boolean xRotCam, boolean yRotCam){
        super(xSpinData, ySpinData, zSpinData, xOffset, yOffset, zOffset, firstSide, secondSide, camera, xRotCam, yRotCam);
    }

    public CubeParticleBehavior copy(){
        return new CubeParticleBehavior(xSpinData, ySpinData, zSpinData, xOffset, yOffset, zOffset, firstSide, secondSide, camera, xRotCam, yRotCam);
    }

    public static CubeParticleBehaviorBuilder create(){
        return new CubeParticleBehaviorBuilder(0, 0, 0);
    }

    public static CubeParticleBehaviorBuilder create(float xOffset, float yOffset, float zOffset){
        return new CubeParticleBehaviorBuilder((float)Math.toRadians(xOffset), (float)Math.toRadians(yOffset), (float)Math.toRadians(zOffset));
    }

    @Override
    public void render(GenericParticle particle, VertexConsumer vertexConsumer, Camera renderInfo, float partialTicks){
        if(particle.shouldRenderTraits) updateRenderTraits(particle, partialTicks);
        TridotParticles.addBehaviorParticleList(particle, this);
    }

    @Override
    public void render(GenericParticle particle, PoseStack poseStack, MultiBufferSource buffer, float partialTicks){
        poseStack.pushPose();
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 vec3 = Vec3.from(camera.getPosition());
        Vec3 pos = getPosition(particle, Minecraft.getInstance().gameRenderer.getMainCamera(), partialTicks);
        poseStack.translate(pos.x() + vec3.x(), pos.y() + vec3.y(), pos.z() + vec3.z());
        poseStack.mulPose(getRotate(particle, Minecraft.getInstance().gameRenderer.getMainCamera(), partialTicks));

        RenderBuilder.create().setRenderType(particle.renderType).setSided(firstSide, secondSide)
        .setUV(particle.getU0(), particle.getV0(), particle.getU1(), particle.getV1())
        .setColor(particle.getRed(), particle.getGreen(), particle.getBlue())
        .setAlpha(particle.getAlpha())
        .setLight(particle.getLightColor(partialTicks))
        .renderCenteredCube(poseStack, particle.getSize() / 2f);
        poseStack.popPose();
    }
}
