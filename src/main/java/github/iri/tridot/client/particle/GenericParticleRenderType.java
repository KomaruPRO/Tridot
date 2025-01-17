package github.iri.tridot.client.particle;

import com.mojang.blaze3d.systems.*;
import com.mojang.blaze3d.vertex.*;
import github.iri.tridot.client.render.*;
import github.iri.tridot.integration.client.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.*;

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