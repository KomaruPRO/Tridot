package pro.komaru.tridot.registry.block.chest;

import net.minecraft.core.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import pro.komaru.tridot.registry.block.*;
import pro.komaru.tridot.registry.block.entity.*;

import javax.annotation.*;

public class TridotChestBlock extends ChestBlock{
    public boolean autoReg;

    public TridotChestBlock(Properties pProperties) {
        super(pProperties, TridotBlockEntities.CHEST_BLOCK_ENTITY::get);
        this.autoReg = true;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TridotChestBlockEntity(pPos, pState);
    }
}
