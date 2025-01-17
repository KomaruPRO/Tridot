package github.iri.tridot.mixin.client;

import github.iri.tridot.client.render.entity.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.player.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin{

    @Inject(at = @At("RETURN"), method = "<init>")
    private void fluffy_fur$PlayerRenderer(EntityRendererProvider.Context context, boolean useSlimModel, CallbackInfo ci){
        PlayerRenderer self = (PlayerRenderer)((Object)this);
        self.addLayer(new ExtraLayer<>(self));
    }
}
