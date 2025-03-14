package pro.komaru.tridot.client.gfx.particle;

import com.mojang.blaze3d.systems.*;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.*;
import pro.komaru.tridot.client.render.LevelRenderHandler;
import pro.komaru.tridot.core.integration.client.ShadersIntegration;

public class GenericParticleRenderType implements ParticleRenderType{
    public static final GenericParticleRenderType INSTANCE = new GenericParticleRenderType();

    @Override
    public void begin(BufferBuilder bufferBuilder, TextureManager textureManager){
        if(ShadersIntegration.shouldApply()) LevelRenderHandler.MATRIX4F = RenderSystem.getModelViewMatrix();
    }

    @Override
    public void end(Tesselator tesselator){

    }
}