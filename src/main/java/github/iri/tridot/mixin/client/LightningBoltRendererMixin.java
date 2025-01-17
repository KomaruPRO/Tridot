package github.iri.tridot.mixin.client;

import com.mojang.blaze3d.vertex.*;
import github.iri.tridot.client.effect.*;
import github.iri.tridot.config.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(LightningBoltRenderer.class)
public abstract class LightningBoltRendererMixin{

    @Inject(at = @At("HEAD"), method = "render*", cancellable = true)
    public void fluffy_fur$render(LightningBolt entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci){
        if(ClientConfig.LIGHTNING_BOLT_EFFECT.get()){
            ci.cancel();
            TridotEffects.lightningBoltRender(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
    }
}
