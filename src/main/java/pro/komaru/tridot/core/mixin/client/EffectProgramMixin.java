package pro.komaru.tridot.core.mixin.client;

import com.mojang.blaze3d.preprocessor.*;
import com.mojang.blaze3d.shaders.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import pro.komaru.tridot.client.graphics.shader.postprocess.*;

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