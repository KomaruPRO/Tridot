package github.iri.tridot.mixin.client;

import com.mojang.blaze3d.preprocessor.*;
import com.mojang.blaze3d.shaders.*;
import github.iri.tridot.client.shader.postprocess.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(EffectProgram.class)
public class EffectProgramMixin{
    @ModifyArg(
    method = "compileShader",
    at = @At(
    value = "INVOKE",
    target = "Lcom/mojang/blaze3d/shaders/EffectProgram;compileShaderInternal(Lcom/mojang/blaze3d/shaders/Program$Type;Ljava/lang/String;Ljava/io/InputStream;Ljava/lang/String;Lcom/mojang/blaze3d/preprocessor/GlslPreprocessor;)I"
    ),
    index = 4
    )
    private static GlslPreprocessor tridot$useCustomPreprocessor(GlslPreprocessor org){
        return TridotGlslPreprocessor.PREPROCESSOR;
    }
}