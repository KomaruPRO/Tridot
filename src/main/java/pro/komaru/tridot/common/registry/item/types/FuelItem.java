package pro.komaru.tridot.common.registry.item.types;

import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;

import javax.annotation.*;

public class FuelItem extends Item{
    public int fuel;

    public FuelItem(Properties properties, int fuel){
        super(properties);
        this.fuel = fuel;
    }

    @Override
    public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType){
        return fuel;
    }
}
