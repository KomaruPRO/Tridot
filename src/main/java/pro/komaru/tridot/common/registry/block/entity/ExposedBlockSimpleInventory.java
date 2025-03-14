package pro.komaru.tridot.common.registry.block.entity;

import com.google.common.base.*;
import net.minecraft.core.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

public abstract class ExposedBlockSimpleInventory extends BlockSimpleInventory implements WorldlyContainer{
    private final Supplier<int[]> slots = Suppliers.memoize(() -> IntStream.range(0, getContainerSize()).toArray());

    protected ExposedBlockSimpleInventory(BlockEntityType<?> type, BlockPos pos, BlockState state){
        super(type, pos, state);
    }

    @Override
    public boolean isEmpty(){
        return getItemHandler().isEmpty();
    }

    @Override
    public int getContainerSize(){
        return inventorySize();
    }

    @Override
    public ItemStack getItem(int index){
        return getItemHandler().getItem(index);
    }

    @Override
    public ItemStack removeItem(int index, int count){
        return getItemHandler().removeItem(index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index){
        return getItemHandler().removeItemNoUpdate(index);
    }

    @Override
    public void setItem(int index, ItemStack stack){
        getItemHandler().setItem(index, stack);
    }

    @Override
    public boolean stillValid(Player player){
        return getItemHandler().stillValid(player);
    }

    @Override
    public int getMaxStackSize(){
        return getItemHandler().getMaxStackSize();
    }

    @Override
    public void startOpen(Player player){
        getItemHandler().startOpen(player);
    }

    @Override
    public void stopOpen(Player player){
        getItemHandler().stopOpen(player);
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack){
        return getItemHandler().canPlaceItem(index, stack);
    }

    @Override
    public int countItem(Item item){
        return getItemHandler().countItem(item);
    }

    @Override
    public boolean hasAnyOf(Set<Item> set){
        return getItemHandler().hasAnyOf(set);
    }

    @NotNull
    @Override
    public int[] getSlotsForFace(@NotNull Direction side){
        return slots.get();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack stack, @Nullable Direction direction){
        if(canPlaceItem(index, stack)){
            ItemStack existing = getItem(index);
            return existing.getCount() < getMaxStackSize();
        }

        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @NotNull ItemStack stack, @Nullable Direction direction){
        return true;
    }

    @Override
    public void clearContent(){
        getItemHandler().clearContent();
    }
}