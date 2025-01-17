package github.iri.tridot.mixin.client;

import github.iri.tridot.*;
import net.minecraft.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.animal.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(FoxRenderer.class)
public abstract class FoxRendererMixin{
    @Unique
    private static final ResourceLocation MAXBOGOMOL_LOCATION = new ResourceLocation(TridotLib.ID, "textures/entity/fox/maxbogomol.png");
    @Unique
    private static final ResourceLocation MAXBOGOMOL_SLEEP_LOCATION = new ResourceLocation(TridotLib.ID, "textures/entity/fox/maxbogomol_sleep.png");
    @Unique
    private static final ResourceLocation FOXPLANE_LOCATION = new ResourceLocation(TridotLib.ID, "textures/entity/fox/foxplane.png");
    @Unique
    private static final ResourceLocation FOXPLANE_SLEEP_LOCATION = new ResourceLocation(TridotLib.ID, "textures/entity/fox/foxplane_sleep.png");

    @Inject(at = @At("HEAD"), method = "getTextureLocation*", cancellable = true)
    public void fluffy_fur$getTextureLocation(Fox entity, CallbackInfoReturnable<ResourceLocation> ci){
        String s = ChatFormatting.stripFormatting(entity.getName().getString());
        if("MaxBogomol".equals(s)){
            if(entity.isSleeping()){
                ci.setReturnValue(MAXBOGOMOL_SLEEP_LOCATION);
            }
            ci.setReturnValue(MAXBOGOMOL_LOCATION);
        }
        if("FoxPlane".equals(s)){
            if(entity.isSleeping()){
                ci.setReturnValue(FOXPLANE_SLEEP_LOCATION);
            }
            ci.setReturnValue(FOXPLANE_LOCATION);
        }
    }
}
