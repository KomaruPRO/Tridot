package pro.komaru.tridot.registry.block.entity;

import pro.komaru.tridot.core.net.packets.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;
import pro.komaru.tridot.core.net.packets.BlockEntityUpdate;

import java.util.*;

public abstract class BlockEntityBase extends BlockEntity{

    public Random random = new Random();

    public BlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState blockState){
        super(type, pos, blockState);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::getUpdateTag);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet){
        super.onDataPacket(net, packet);
        handleUpdateTag(packet.getTag());
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag(){
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void setChanged(){
        super.setChanged();
        if(level != null && !level.isClientSide){
            BlockEntityUpdate.packet(this);
        }
    }
}
