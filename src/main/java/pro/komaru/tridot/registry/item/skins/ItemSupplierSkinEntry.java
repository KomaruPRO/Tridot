package pro.komaru.tridot.registry.item.skins;

import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.*;

import java.util.function.*;

public class ItemSupplierSkinEntry extends ItemSkinEntry{
    public final Supplier<Item> item;
    public String skin;

    public ItemSupplierSkinEntry(Supplier<Item> item, String skin){
        this.item = item;
        this.skin = skin;
    }

    public boolean canApplyOnItem(ItemStack itemStack){
        return itemStack.is(item.get());
    }

    @OnlyIn(Dist.CLIENT)
    public String getItemModelName(ItemStack stack){
        return this.skin;
    }
}