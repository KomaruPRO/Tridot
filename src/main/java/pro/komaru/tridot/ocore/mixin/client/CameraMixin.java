package pro.komaru.tridot.ocore.mixin.client;

import net.minecraft.client.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import pro.komaru.tridot.oclient.graphics.gui.screenshake.*;

@Mixin(Camera.class)
public class CameraMixin{

    @Inject(method = "setup", at = @At("RETURN"))
    private void tridot$screenshake(BlockGetter area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci){
        ScreenshakeHandler.cameraTick((Camera)(Object)this);
    }
}