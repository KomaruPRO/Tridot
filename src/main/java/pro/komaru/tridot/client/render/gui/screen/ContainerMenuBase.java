package pro.komaru.tridot.client.render.gui.screen;

import pro.komaru.tridot.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraftforge.items.*;
import org.jetbrains.annotations.*;

public class ContainerMenuBase extends AbstractContainerMenu{
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    public Player playerEntity;
    public IItemHandler playerInventory;

    protected ContainerMenuBase(@Nullable MenuType<?> menuType, int containerId){
        super(menuType, containerId);
    }

    public int getInventorySize(){
        return 1;
    }

    public int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx){
        for(int i = 0; i < amount; i++){
            addSlot(new InputSlot(handler, index, x, y));
            x += dx;
            index++;
        }

        return index;
    }

    public int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy){
        for(int j = 0; j < verAmount; j++){
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }

        return index;
    }

    public void layoutPlayerInventorySlots(int leftCol, int topRow){
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index){
        Slot sourceSlot = slots.get(index);
        if(sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if(index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT){
            if(!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
            + getInventorySize(), false)){
                return ItemStack.EMPTY;
            }
        }else if(index < TE_INVENTORY_FIRST_SLOT_INDEX + getInventorySize()){
            if(!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)){
                return ItemStack.EMPTY;
            }
        }else{
            Log.error("Invalid slotIndex: " + index);
            return ItemStack.EMPTY;
        }
        if(sourceStack.getCount() == 0){
            sourceSlot.set(ItemStack.EMPTY);
        }else{
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerEntity, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player){
        return true;
    }
}
