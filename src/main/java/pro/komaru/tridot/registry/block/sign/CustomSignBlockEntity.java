package pro.komaru.tridot.registry.block.sign;

import pro.komaru.tridot.registry.block.*;
import net.minecraft.core.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

public class CustomSignBlockEntity extends SignBlockEntity{

    public CustomSignBlockEntity(BlockPos pos, BlockState state){
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType(){
        return TridotBlockEntities.SIGN.get();
    }
}
