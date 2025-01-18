package pro.komaru.tridot.client.splash;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.LanguageSelectScreen;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import pro.komaru.tridot.utilities.struct.Pair;

import java.util.*;

public class SplashHandler{
    public static List<String> splashes = new ArrayList<>();
    public static List<Component> componentSplashes = new ArrayList<>();
    public static List<SplashLanguaged> languagedSplashes = new ArrayList<>();
    public static void addSplash(Component splash){
        componentSplashes.add(splash);
    }
    public static void addSplash(String splash){
        splashes.add(splash);
    }
    public static void addSplash(String lang, String splash){
        languagedSplashes.add(new SplashLanguaged(lang,splash));
    }

    public static List<String> getSplashes(){
        List<String> all = new ArrayList<>(splashes);
        for (Component c : componentSplashes)
            all.add(c.getString());
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,() -> () -> addLanguaged(all));
        return all;
    }
    @OnlyIn(Dist.CLIENT)
    public static void addLanguaged(List<String> all) {
        for (SplashLanguaged languagedSplash : languagedSplashes) {
            if(languagedSplash.first().equals(Minecraft.getInstance().options.languageCode))
                all.add(languagedSplash.second());
        }
    }

    public static class SplashLanguaged extends Pair<String,String> {
        public SplashLanguaged(String lang, String splash) {
            super(lang,splash);
        }
    }
}
