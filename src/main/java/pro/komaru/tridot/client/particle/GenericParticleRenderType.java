package pro.komaru.tridot.client.particle;

import com.mojang.blaze3d.systems.*;
import com.mojang.blaze3d.vertex.*;
import pro.komaru.tridot.client.render.*;
import pro.komaru.tridot.integration.client.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.*;
import pro.komaru.tridot.client.render.LevelRenderHandler;
import pro.komaru.tridot.integration.client.ShadersIntegration;

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