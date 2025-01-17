package github.iri.tridot.registry.item.types;

import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.*;

import javax.annotation.*;

public class FuelBlockItem extends BlockItem{
    public int fuel;

    public FuelBlockItem(Block blockIn, Properties properties, int fuel){
        super(blockIn, properties);
        this.fuel = fuel;
    }

    @Override
    public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType){
        return fuel;
    }
}
