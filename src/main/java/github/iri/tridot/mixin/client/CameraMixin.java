package github.iri.tridot.mixin.client;

import github.iri.tridot.client.screenshake.*;
import net.minecraft.client.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Camera.class)
public class CameraMixin{

    @Inject(method = "setup", at = @At("RETURN"))
    private void tridot$screenshake(BlockGetter area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci){
        ScreenshakeHandler.cameraTick((Camera)(Object)this);
    }
}