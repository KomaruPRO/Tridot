package pro.komaru.tridot.oregistry.block.fire;

import java.util.*;

public class FireItemHandler{
    public static List<FireItemModifier> modifiers = new ArrayList<>();

    public static void register(FireItemModifier modifier){
        modifiers.add(modifier);
    }

    public static List<FireItemModifier> getModifiers(){
        return modifiers;
    }
}
