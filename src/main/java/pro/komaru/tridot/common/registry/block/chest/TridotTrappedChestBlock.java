package pro.komaru.tridot.common.registry.block.chest;

import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.stats.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import pro.komaru.tridot.common.registry.block.TridotBlockEntities;
import pro.komaru.tridot.common.registry.block.entity.TridotTrappedChestBlockEntity;

import javax.annotation.*;

public class TridotTrappedChestBlock extends ChestBlock{
    public boolean autoReg;
    public TridotTrappedChestBlock(Properties pProperties) {
        super(pProperties, TridotBlockEntities.TRAPPED_CHEST_BLOCK_ENTITY::get);
        this.autoReg = true;
    }

    protected Stat<ResourceLocation> getOpenChestStat() {
        return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST);
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     *
     * @deprecated call via {@link BlockStateBase#isSignalSource}
     * whenever possible. Implementing/overriding is fine.
     */
    @Deprecated
    public boolean isSignalSource(BlockState pState) {
        return true;
    }

    /**
     * @deprecated call via {@link BlockStateBase#getSignal}
     * whenever possible. Implementing/overriding is fine.
     */
    @Deprecated
    public int getSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        return Mth.clamp(ChestBlockEntity.getOpenCount(pBlockAccess, pPos), 0, 15);
    }

    /**
     * @deprecated call via {@link BlockStateBase#getDirectSignal}
     * whenever possible. Implementing/overriding is fine.
     */
    @Deprecated
    public int getDirectSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        return pSide == Direction.UP ? pBlockState.getSignal(pBlockAccess, pPos, pSide) : 0;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TridotTrappedChestBlockEntity(pPos, pState);
    }
}
