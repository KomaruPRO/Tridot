package pro.komaru.tridot.ointegration.client;

import net.irisshaders.iris.*;
import net.minecraft.client.*;
import net.minecraftforge.fml.*;

public class ShadersIntegration{
    public static boolean LOADED;

    public static class LoadedOnly{
        public static boolean isShadersEnabled(){
            return Iris.getIrisConfig().areShadersEnabled();
        }
    }

    public static void init(){
        LOADED = ModList.get().isLoaded("oculus");
    }

    public static boolean isLoaded(){
        return LOADED;
    }

    public static boolean isShadersEnabled(){
        if(isLoaded()){
            return LoadedOnly.isShadersEnabled();
        }
        return false;
    }

    public static boolean shouldApply(){
        return isShadersEnabled() || Minecraft.useShaderTransparency();
    }
}
