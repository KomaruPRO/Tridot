package pro.komaru.tridot.client.gui.screen;

import net.minecraft.world.entity.player.*;
import net.minecraftforge.items.*;

public class InputSlot extends SlotItemHandler{

    public InputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition){
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean allowModification(Player player){
        return true;
    }
}
