package pro.komaru.tridot.utilities;

import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.*;

public class IntegrationUtil{
    public static Item getItem(String modId, String id){
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(modId, id));
        return item != null ? item : Items.DIRT;
    }
}
