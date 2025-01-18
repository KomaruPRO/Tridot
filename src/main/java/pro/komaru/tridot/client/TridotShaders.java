package pro.komaru.tridot.client;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.event.lifecycle.*;
import pro.komaru.tridot.TridotLib;
import pro.komaru.tridot.client.graphics.shader.postprocess.GlowPostProcess;
import pro.komaru.tridot.client.graphics.shader.postprocess.PostProcessHandler;

import java.io.*;

public class TridotShaders{
    public static ShaderInstance ADDITIVE_TEXTURE, ADDITIVE, TRANSLUCENT_TEXTURE, TRANSLUCENT;

    @Mod.EventBusSubscriber(modid = TridotLib.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientRegistryEvents{
        @SubscribeEvent
        public static void registerShaders(FMLClientSetupEvent event){
            PostProcessHandler.addInstance(GlowPostProcess.INSTANCE);
        }

        @SubscribeEvent
        public static void shaderRegistry(RegisterShadersEvent event) throws IOException{
            event.registerShader(new ShaderInstance(event.getResourceProvider(), new ResourceLocation(TridotLib.ID, "additive_texture"), DefaultVertexFormat.POSITION_TEX_COLOR), shader -> ADDITIVE_TEXTURE = shader);
            event.registerShader(new ShaderInstance(event.getResourceProvider(), new ResourceLocation(TridotLib.ID, "additive"), DefaultVertexFormat.POSITION_COLOR), shader -> ADDITIVE = shader);
            event.registerShader(new ShaderInstance(event.getResourceProvider(), new ResourceLocation(TridotLib.ID, "translucent_texture"), DefaultVertexFormat.PARTICLE), shader -> TRANSLUCENT_TEXTURE = shader);
            event.registerShader(new ShaderInstance(event.getResourceProvider(), new ResourceLocation(TridotLib.ID, "translucent"), DefaultVertexFormat.PARTICLE), shader -> TRANSLUCENT = shader);
        }
    }

    public static ShaderInstance getAdditiveTexture(){
        return ADDITIVE_TEXTURE;
    }

    public static ShaderInstance getAdditive(){
        return ADDITIVE;
    }

    public static ShaderInstance getTranslucentTexture(){
        return TRANSLUCENT_TEXTURE;
    }

    public static ShaderInstance getTranslucent(){
        return TRANSLUCENT;
    }
}