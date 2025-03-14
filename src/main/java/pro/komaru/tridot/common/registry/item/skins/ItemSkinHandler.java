package pro.komaru.tridot.common.registry.item.skins;

import pro.komaru.tridot.util.struct.data.Seq;

import java.util.*;

public class ItemSkinHandler{
    public static Map<String, ItemSkin> skins = new HashMap<>();
    public static Seq<ItemSkin> skinList = Seq.with();

    public static void add(String id, ItemSkin skin){
        skins.put(id, skin);
        skinList.add(skin);
    }

    public static ItemSkin get(int id){
        return skins.get(id);
    }

    public static ItemSkin get(String id){
        return skins.get(id);
    }

    public static void register(ItemSkin skin){
        skins.put(skin.id.toString(), skin);
        skinList.add(skin);
    }

    public static int size(){
        return skins.size();
    }

    public static Seq<ItemSkin> getSkins(){
        return skinList;
    }
}
