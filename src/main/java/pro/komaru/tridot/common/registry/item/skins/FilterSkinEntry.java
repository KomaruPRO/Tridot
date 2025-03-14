package pro.komaru.tridot.common.registry.item.skins;

import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.*;
import pro.komaru.tridot.util.struct.func.Boolf;

public class FilterSkinEntry extends ItemSkinEntry{
    public String model;
    public Boolf<ItemStack> filter;

    public FilterSkinEntry(String model, Boolf<ItemStack> filter){
        this.filter = filter;
        this.model = model;
    }

    public boolean appliesOn(ItemStack stack){
        return filter.get(stack);
    }
    @OnlyIn(Dist.CLIENT)
    public String itemModel(ItemStack stack){
        return this.model;
    }
}