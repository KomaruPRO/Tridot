package github.iri.tridot.mixin.client;

import github.iri.tridot.*;
import net.minecraft.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.animal.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(RabbitRenderer.class)
public abstract class RabbitRendererMixin{
    @Unique
    private static final ResourceLocation RABBIT_NANACHI_LOCATION = new ResourceLocation(TridotLib.ID, "textures/entity/rabbit/nanachi.png");

    @Inject(at = @At("HEAD"), method = "getTextureLocation*", cancellable = true)
    public void fluffy_fur$getTextureLocation(Rabbit entity, CallbackInfoReturnable<ResourceLocation> ci){
        String s = ChatFormatting.stripFormatting(entity.getName().getString());
        if("Nanachi".equals(s)){
            ci.setReturnValue(RABBIT_NANACHI_LOCATION);
        }
    }
}
