package github.iri.tridot.client.splash;

import java.util.*;

public class SplashHandler{
    public static List<String> splashes = new ArrayList<>();

    public static void addSplash(String splash){
        splashes.add(splash);
    }

    public static List<String> getSplashes(){
        return splashes;
    }
}
