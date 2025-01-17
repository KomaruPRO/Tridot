package github.iri.tridot.mixin.client;

import com.mojang.blaze3d.vertex.*;
import github.iri.tridot.client.*;
import github.iri.tridot.client.render.*;
import github.iri.tridot.client.shader.postprocess.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin{

    @Shadow
    public abstract void renderLevel(float pPartialTicks, long pFinishTimeNano, PoseStack pPoseStack);

    @Inject(at = @At(value = "RETURN"), method = "renderItemInHand")
    private void tridot$renderItemInHand(PoseStack pPoseStack, Camera pActiveRenderInfo, float pPartialTicks, CallbackInfo ci){
        for(RenderBuilder builder : TridotRenderTypes.customItemRenderBuilderFirst){
            builder.endBatch();
        }
        TridotRenderTypes.customItemRenderBuilderFirst.clear();
    }

    @Inject(method = "resize", at = @At(value = "HEAD"))
    public void tridot$injectionResizeListener(int width, int height, CallbackInfo ci){
        LevelRenderHandler.resize(width, height);
        PostProcessHandler.resize(width, height);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;bindWrite(Z)V"), method = "render")
    public void tridot$renderScreenPostProcess(float partialTicks, long nanoTime, boolean renderLevel, CallbackInfo ci){
        PostProcessHandler.onScreenRender((GameRenderer)(Object)this, partialTicks, nanoTime, renderLevel);
    }

    @Inject(at = @At(value = "RETURN"), method = "render")
    public void tridot$renderWindowPostProcess(float partialTicks, long nanoTime, boolean renderLevel, CallbackInfo ci){
        PostProcessHandler.onWindowRender((GameRenderer)(Object)this, partialTicks, nanoTime, renderLevel);
    }
}
