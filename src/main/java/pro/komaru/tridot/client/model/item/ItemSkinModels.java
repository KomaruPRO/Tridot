package pro.komaru.tridot.client.model.item;

import net.minecraft.client.resources.model.*;
import net.minecraft.resources.*;

import java.util.*;

public class ItemSkinModels{
    public static Map<String, BakedModel> modelsSkins = new HashMap<>();
    public static ArrayList<String> skins = new ArrayList<>();

    public static void addModelSkins(String id, BakedModel model){
        modelsSkins.put(id, model);
    }

    public static void add(ResourceLocation loc){
        skins.add(loc.toString());
    }

    public static void addBowSkin(ResourceLocation loc){
        String id = loc.toString();
        skins.add(id);
        skins.add(id + "_pulling_0");
        skins.add(id + "_pulling_1");
        skins.add(id + "_pulling_2");
    }

    public static Map<String, BakedModel> getModelsSkins(){
        return modelsSkins;
    }

    public static ArrayList<String> getSkins(){
        return skins;
    }

    public static BakedModel getModelSkins(String id){
        return modelsSkins.get(id);
    }

    public static String getSkin(int id){
        return skins.get(id);
    }
}