package pro.komaru.tridot.client.sound;

import pro.komaru.tridot.util.struct.data.Seq;

public class MusicHandler {
    public static Seq<MusicModifier> modifiers = Seq.with();

    public static void register(MusicModifier modifier){
        modifiers.add(modifier);
    }

    public static Seq<MusicModifier> getModifiers(){
        return modifiers;
    }
}
