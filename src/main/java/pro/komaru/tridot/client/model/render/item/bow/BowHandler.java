package pro.komaru.tridot.client.model.render.item.bow;

import net.minecraft.world.item.*;

import java.util.*;

public class BowHandler{
    public static List<Item> bows = new ArrayList<>();

    public static void addBow(Item item){
        bows.add(item);
    }

    public static List<Item> getBows(){
        return bows;
    }
}
