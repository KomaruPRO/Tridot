package pro.komaru.tridot.oclient;

import com.mojang.blaze3d.platform.*;
import com.mojang.blaze3d.systems.*;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.event.lifecycle.*;
import pro.komaru.tridot.TridotLib;
import pro.komaru.tridot.oclient.graphics.render.LevelRenderHandler;
import pro.komaru.tridot.oclient.graphics.render.RenderBuilder;
import pro.komaru.tridot.oclient.graphics.render.TridotRenderType;

import java.util.*;

public class TridotRenderTypes{

    public static List<RenderType> renderTypes = new ArrayList<>();
    public static List<RenderType> additiveParticleRenderTypes = new ArrayList<>();
    public static List<RenderType> additiveRenderTypes = new ArrayList<>();
    public static List<RenderType> translucentParticleRenderTypes = new ArrayList<>();
    public static List<RenderType> translucentRenderTypes = new ArrayList<>();

    public static List<RenderBuilder> customItemRenderBuilderGui = new ArrayList<>();
    public static List<RenderBuilder> customItemRenderBuilderFirst = new ArrayList<>();

    public static final RenderStateShard.TransparencyStateShard ADDITIVE_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("additive_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    public static final RenderStateShard.TransparencyStateShard NORMAL_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("normal_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    public static final RenderStateShard.LightmapStateShard LIGHTMAP = new RenderStateShard.LightmapStateShard(true);
    public static final RenderStateShard.LightmapStateShard NO_LIGHTMAP = new RenderStateShard.LightmapStateShard(false);
    public static final RenderStateShard.OverlayStateShard OVERLAY = new RenderStateShard.OverlayStateShard(true);
    public static final RenderStateShard.OverlayStateShard NO_OVERLAY = new RenderStateShard.OverlayStateShard(false);
    public static final RenderStateShard.CullStateShard CULL = new RenderStateShard.CullStateShard(true);
    public static final RenderStateShard.CullStateShard NO_CULL = new RenderStateShard.CullStateShard(false);
    public static final RenderStateShard.WriteMaskStateShard COLOR_WRITE = new RenderStateShard.WriteMaskStateShard(true, false);
    public static final RenderStateShard.WriteMaskStateShard DEPTH_WRITE = new RenderStateShard.WriteMaskStateShard(false, true);
    public static final RenderStateShard.WriteMaskStateShard COLOR_DEPTH_WRITE = new RenderStateShard.WriteMaskStateShard(true, true);
    public static final RenderStateShard.TextureStateShard BLOCK_SHEET = new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false);
    public static final RenderStateShard.TextureStateShard BLOCK_SHEET_MIPPED = new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, true);
    public static final RenderStateShard.TextureStateShard PARTICLE_SHEET = new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_PARTICLES, false, false);
    public static final RenderStateShard.TextureStateShard PARTICLE_SHEET_MIPPED = new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_PARTICLES, false, true);

    public static final RenderStateShard.ShaderStateShard ADDITIVE_TEXTURE_SHADER = new RenderStateShard.ShaderStateShard(TridotShaders::getAdditiveTexture);
    public static final RenderStateShard.ShaderStateShard ADDITIVE_SHADER = new RenderStateShard.ShaderStateShard(TridotShaders::getAdditive);
    public static final RenderStateShard.ShaderStateShard TRANSLUCENT_TEXTURE_SHADER = new RenderStateShard.ShaderStateShard(TridotShaders::getTranslucentTexture);
    public static final RenderStateShard.ShaderStateShard TRANSLUCENT_SHADER = new RenderStateShard.ShaderStateShard(TridotShaders::getTranslucent);

    public static RenderType ADDITIVE_PARTICLE = TridotRenderType.createRenderType(TridotLib.ID + ":additive_particle",
    DefaultVertexFormat.PARTICLE, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
    .setWriteMaskState(COLOR_WRITE).setLightmapState(NO_LIGHTMAP).setTransparencyState(ADDITIVE_TRANSPARENCY)
    .setTextureState(PARTICLE_SHEET).setShaderState(ADDITIVE_TEXTURE_SHADER).createCompositeState(true));

    public static RenderType ADDITIVE_BLOCK_PARTICLE = TridotRenderType.createRenderType(TridotLib.ID + ":additive_block_particle",
    DefaultVertexFormat.PARTICLE, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
    .setWriteMaskState(COLOR_WRITE).setLightmapState(NO_LIGHTMAP).setTransparencyState(ADDITIVE_TRANSPARENCY)
    .setTextureState(BLOCK_SHEET).setShaderState(ADDITIVE_TEXTURE_SHADER).createCompositeState(true));

    public static RenderType ADDITIVE_PARTICLE_TEXTURE = TridotRenderType.createRenderType(TridotLib.ID + ":additive_particle_texture",
    DefaultVertexFormat.PARTICLE, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
    .setWriteMaskState(COLOR_WRITE).setLightmapState(NO_LIGHTMAP).setTransparencyState(ADDITIVE_TRANSPARENCY)
    .setTextureState(PARTICLE_SHEET).setShaderState(ADDITIVE_TEXTURE_SHADER).createCompositeState(true));

    public static RenderType ADDITIVE_TEXTURE = TridotRenderType.createRenderType(TridotLib.ID + ":additive_texture",
    DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
    .setWriteMaskState(COLOR_WRITE).setLightmapState(NO_LIGHTMAP).setTransparencyState(NORMAL_TRANSPARENCY)
    .setTextureState(BLOCK_SHEET).setShaderState(ADDITIVE_TEXTURE_SHADER).createCompositeState(true));

    public static RenderType ADDITIVE = TridotRenderType.createRenderType(TridotLib.ID + ":additive",
    DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
    .setWriteMaskState(COLOR_WRITE).setLightmapState(NO_LIGHTMAP).setTransparencyState(ADDITIVE_TRANSPARENCY)
    .setShaderState(ADDITIVE_SHADER).createCompositeState(true));

    public static RenderType TRANSLUCENT_PARTICLE = TridotRenderType.createRenderType(TridotLib.ID + ":translucent_particle",
    DefaultVertexFormat.PARTICLE, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
    .setWriteMaskState(COLOR_WRITE).setLightmapState(LIGHTMAP).setTransparencyState(NORMAL_TRANSPARENCY)
    .setTextureState(PARTICLE_SHEET).setShaderState(TRANSLUCENT_TEXTURE_SHADER).createCompositeState(true));

    public static RenderType TRANSLUCENT_BLOCK_PARTICLE = TridotRenderType.createRenderType(TridotLib.ID + ":translucent_block_particle",
    DefaultVertexFormat.PARTICLE, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
    .setWriteMaskState(COLOR_WRITE).setLightmapState(LIGHTMAP).setTransparencyState(NORMAL_TRANSPARENCY)
    .setTextureState(BLOCK_SHEET).setShaderState(TRANSLUCENT_TEXTURE_SHADER).createCompositeState(true));

    public static RenderType TRANSLUCENT_PARTICLE_TEXTURE = TridotRenderType.createRenderType(TridotLib.ID + ":translucent_particle_texture",
    DefaultVertexFormat.PARTICLE, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
    .setWriteMaskState(COLOR_WRITE).setLightmapState(LIGHTMAP).setTransparencyState(NORMAL_TRANSPARENCY)
    .setTextureState(PARTICLE_SHEET).setShaderState(TRANSLUCENT_TEXTURE_SHADER).createCompositeState(true));

    public static RenderType TRANSLUCENT_TEXTURE = TridotRenderType.createRenderType(TridotLib.ID + ":translucent_texture",
    DefaultVertexFormat.PARTICLE, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
    .setWriteMaskState(COLOR_WRITE).setLightmapState(LIGHTMAP).setTransparencyState(NORMAL_TRANSPARENCY)
    .setTextureState(BLOCK_SHEET).setShaderState(TRANSLUCENT_TEXTURE_SHADER).createCompositeState(true));

    public static RenderType TRANSLUCENT = TridotRenderType.createRenderType(TridotLib.ID + ":translucent",
    DefaultVertexFormat.PARTICLE, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
    .setWriteMaskState(COLOR_WRITE).setLightmapState(LIGHTMAP).setTransparencyState(NORMAL_TRANSPARENCY)
    .setShaderState(TRANSLUCENT_SHADER).createCompositeState(true));

    @Mod.EventBusSubscriber(modid = TridotLib.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientRegistryEvents{
        @SubscribeEvent
        public static void registerRenderTypes(FMLClientSetupEvent event){
            addAdditiveParticleRenderType(ADDITIVE_PARTICLE);
            addAdditiveParticleRenderType(ADDITIVE_BLOCK_PARTICLE);
            addAdditiveRenderType(ADDITIVE_PARTICLE_TEXTURE);
            addAdditiveRenderType(ADDITIVE_TEXTURE);
            addAdditiveRenderType(ADDITIVE);
            addTranslucentParticleRenderType(TRANSLUCENT_PARTICLE);
            addTranslucentParticleRenderType(TRANSLUCENT_BLOCK_PARTICLE);
            addTranslucentRenderType(TRANSLUCENT_PARTICLE_TEXTURE);
            addTranslucentRenderType(TRANSLUCENT_TEXTURE);
            addTranslucentRenderType(TRANSLUCENT);
        }
    }

    public static MultiBufferSource.BufferSource getDelayedRender(){
        return LevelRenderHandler.getDelayedRender();
    }

    public static void addRenderType(RenderType renderType){
        renderTypes.add(renderType);
    }

    public static void addAdditiveParticleRenderType(RenderType renderType){
        additiveParticleRenderTypes.add(renderType);
        addRenderType(renderType);
    }

    public static void addAdditiveRenderType(RenderType renderType){
        additiveRenderTypes.add(renderType);
        addRenderType(renderType);
    }

    public static void addTranslucentParticleRenderType(RenderType renderType){
        translucentParticleRenderTypes.add(renderType);
        addRenderType(renderType);
    }

    public static void addTranslucentRenderType(RenderType renderType){
        translucentRenderTypes.add(renderType);
        addRenderType(renderType);
    }

    public static void addCustomItemRenderBuilderGui(RenderBuilder renderBuilder){
        customItemRenderBuilderGui.add(renderBuilder);
    }

    public static void addCustomItemRenderBuilderFirst(RenderBuilder renderBuilder){
        customItemRenderBuilderFirst.add(renderBuilder);
    }
}
