package github.iri.tridot.mixin.client;

import github.iri.tridot.client.playerskin.*;
import net.minecraft.client.player.*;
import net.minecraft.resources.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin{

    @Inject(at = @At("RETURN"), method = "getSkinTextureLocation", cancellable = true)
    private void fluffy_fur$getSkinTextureLocation(CallbackInfoReturnable<ResourceLocation> cir){
        AbstractClientPlayer self = (AbstractClientPlayer)((Object)this);
        PlayerSkin skin = PlayerSkinHandler.getSkin(self);

        if(skin != null){
            ResourceLocation skinTexture = skin.getSkin(self);
            if(skinTexture != null){
                cir.setReturnValue(skinTexture);
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "getCloakTextureLocation", cancellable = true)
    private void fluffy_fur$getCloakTextureLocation(CallbackInfoReturnable<ResourceLocation> cir){
        AbstractClientPlayer self = (AbstractClientPlayer)((Object)this);
        PlayerSkinCape cape = PlayerSkinHandler.getSkinCape(self);

        if(cape != null){
            ResourceLocation capeTexture = cape.getSkin(self);
            if(capeTexture != null){
                cir.setReturnValue(capeTexture);
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "getModelName", cancellable = true)
    private void fluffy_fur$getModelName(CallbackInfoReturnable<String> cir){
        AbstractClientPlayer self = (AbstractClientPlayer)((Object)this);
        PlayerSkin skin = PlayerSkinHandler.getSkin(self);

        if(skin != null){
            if(skin.getSlim(self)){
                cir.setReturnValue("slim");
            }else{
                cir.setReturnValue("default");
            }
        }
    }
}
