package github.iri.tridot.registry.block.sign;

import net.minecraft.core.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;

import javax.annotation.*;

public class CustomCeilingHangingSignBlock extends CeilingHangingSignBlock{
    public CustomCeilingHangingSignBlock(Properties properties, WoodType type){
        super(properties, type);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state){
        return new CustomHangingSignBlockEntity(pos, state);
    }
}