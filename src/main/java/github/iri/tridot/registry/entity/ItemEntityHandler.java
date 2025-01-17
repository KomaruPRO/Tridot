package github.iri.tridot.registry.entity;

import java.util.ArrayList;
import java.util.List;

public class ItemEntityHandler{
    public static List<ItemEntityModifier> modifiers = new ArrayList<>();

    public static void register(ItemEntityModifier modifier){
        modifiers.add(modifier);
    }

    public static List<ItemEntityModifier> getModifiers(){
        return modifiers;
    }
}
