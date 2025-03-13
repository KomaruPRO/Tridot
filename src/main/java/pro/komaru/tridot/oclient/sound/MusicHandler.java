package pro.komaru.tridot.oclient.sound;

import java.util.*;

//todo fluffy
public class MusicHandler{
    public static List<MusicModifier> modifiers = new ArrayList<>();

    public static void register(MusicModifier modifier){
        modifiers.add(modifier);
    }

    public static List<MusicModifier> getModifiers(){
        return modifiers;
    }
}
