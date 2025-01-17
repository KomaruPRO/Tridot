package github.iri.tridot.common.block.fire;

import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.*;

public class FireBlockModifier{

    public boolean canLightBlock(Level level, BlockPos blockPos, BlockState blockState, Entity entity){
        return CampfireBlock.canLight(blockState) || CandleBlock.canLight(blockState) || CandleCakeBlock.canLight(blockState);
    }

    public void setLightBlock(Level level, BlockPos blockPos, BlockState blockState, Entity entity){
        level.setBlock(blockPos, blockState.setValue(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
        if(entity != null) level.gameEvent(entity, GameEvent.BLOCK_CHANGE, blockPos);
    }
}
