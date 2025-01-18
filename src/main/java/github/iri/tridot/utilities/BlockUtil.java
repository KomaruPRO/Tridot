package github.iri.tridot.utilities;

import net.minecraft.core.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;

public class BlockUtil{

    public static boolean growCrop(ItemStack stack, Level level, BlockPos blockPos){
        if(BoneMealItem.growCrop(stack, level, blockPos)){
            return true;
        }else{
            BlockState blockstate = level.getBlockState(blockPos);
            boolean flag = blockstate.isFaceSturdy(level, blockPos, Direction.UP);
            return flag && BoneMealItem.growWaterPlant(stack, level, blockPos.relative(Direction.UP), Direction.UP);
        }
    }

    public static boolean growCrop(Level level, BlockPos blockPos){
        return growCrop(ItemStack.EMPTY, level, blockPos);
    }
}
