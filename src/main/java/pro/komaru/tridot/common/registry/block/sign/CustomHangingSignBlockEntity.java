package pro.komaru.tridot.common.registry.block.sign;

import pro.komaru.tridot.common.registry.block.TridotBlockEntities;
import net.minecraft.core.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

public class CustomHangingSignBlockEntity extends HangingSignBlockEntity{

    public CustomHangingSignBlockEntity(BlockPos pos, BlockState state){
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType(){
        return TridotBlockEntities.HANGING_SIGN.get();
    }
}
