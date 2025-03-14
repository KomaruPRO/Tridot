package pro.komaru.tridot.common.registry.block.entity;

import net.minecraft.core.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import pro.komaru.tridot.common.registry.block.TridotBlockEntities;

public class TridotChestBlockEntity extends ChestBlockEntity {
    public TridotChestBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(TridotBlockEntities.CHEST_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return TridotBlockEntities.CHEST_BLOCK_ENTITY.get();
    }
}