package pro.komaru.tridot.common.registry.item.types;

import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;

public class TestItem extends Item{
    public TestItem(Properties pProperties){
        super(pProperties);
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        return InteractionResultHolder.consume(itemstack);
    }

    public int getUseDuration(@NotNull ItemStack stack) {
        return 72000;
    }
}
