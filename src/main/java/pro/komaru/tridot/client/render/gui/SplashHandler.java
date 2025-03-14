package pro.komaru.tridot.client.render.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import pro.komaru.tridot.util.struct.data.Pair;
import pro.komaru.tridot.util.struct.data.Seq;
import pro.komaru.tridot.util.struct.func.Prov;

import java.util.*;

//todo fluffy
public class SplashHandler {

    public static class DotSplash {
        public Prov<String> textSupplier;
        public 
    }


    public static Seq<SplashLanguaged> languagedSplashes = Seq.with();
    public static Seq<Prov<String>> dynamicSplashes = Seq.with();

    public static void addSplash(String splash){
        addSplash(() -> splash);
    }
    public static void addSplash(Component splash){
        addSplash(splash::getString);
    }
    public static void addSplash(Prov<String> splash){
        dynamicSplashes.add(splash);
    }
    public static void addSplash(String lang, String splash){
        languagedSplashes.add(new SplashLanguaged(lang,splash));
    }
    public static void addSplash(int weight, String lang, String splash){
        for (int i = 0; i < weight; i++) addSplash(lang,splash);
    }

    public static List<String> getSplashes(){
        List<String> all = dynamicSplashes.map(Prov::get).list();
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
