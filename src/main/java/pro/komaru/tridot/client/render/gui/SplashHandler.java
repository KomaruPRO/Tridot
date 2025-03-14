package pro.komaru.tridot.client.render.gui;

import net.minecraft.network.chat.Component;
import pro.komaru.tridot.api.Utils;
import pro.komaru.tridot.util.struct.data.Seq;
import pro.komaru.tridot.util.struct.func.Boolf;
import pro.komaru.tridot.util.struct.func.Prov;

import java.util.*;

public class SplashHandler {

    public static class DotSplash {
        public Prov<String> text;
        public Boolf<DotSplash> filter;

        public DotSplash(Prov<String> text, Boolf<DotSplash> filter) {
            this.text = text;
            this.filter = filter;
        }
    }

    public static Seq<DotSplash> all = Seq.with();

    public static void add(Prov<String> text, String language, int weight) {
        for (int i = 0; i < weight; i++) {
            all.add(new DotSplash(text,s -> language == null || Utils.mc().options.languageCode.equals(language)));
        }
    }
    public static void add(Prov<String> text, String language) {
        add(text,language,1);
    }
    public static void add(Prov<String> text) {
        add(text,null,1);
    }
    public static void add(String text) {
        add(text,null,1);
    }
    public static void add(String text, String language) {
        add(() -> text,language,1);
    }
    public static void add(String text, String language,int weight) {
        add(() -> text,language,weight);
    }
    public static void add(Component comp) {
        add(comp::getString,null,1);
    }
    public static void add(Component comp, int weight) {
        add(comp::getString,null,weight);
    }

    public static List<String> getSplashes() {
        return SplashHandler.all.select(f -> f.filter.get(f)).map(e -> e.text.get()).list();
    }
}
