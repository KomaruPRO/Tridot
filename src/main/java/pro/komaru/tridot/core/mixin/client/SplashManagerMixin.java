package pro.komaru.tridot.core.mixin.client;

import net.minecraft.client.resources.*;
import net.minecraft.server.packs.resources.*;
import net.minecraft.util.profiling.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import pro.komaru.tridot.client.graphics.gui.splash.*;

import java.util.*;

@Mixin(SplashManager.class)
public abstract class SplashManagerMixin{

    @Shadow
    @Final
    private List<String> splashes;

    @Inject(at = @At("RETURN"), method = "apply*")
    public void tridot$apply(List<String> object, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci){
        this.splashes.addAll(SplashHandler.getSplashes());
    }
}
