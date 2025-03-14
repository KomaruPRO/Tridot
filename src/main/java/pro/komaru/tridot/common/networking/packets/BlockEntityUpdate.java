package pro.komaru.tridot.common.networking.packets;

import net.minecraft.core.*;
import net.minecraft.network.protocol.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;

public class BlockEntityUpdate{

    public static void packet(BlockEntity blockEntity){
        if(blockEntity != null && blockEntity.getLevel() instanceof ServerLevel){
            Packet<?> packet = blockEntity.getUpdatePacket();
            if(packet != null){
                BlockPos pos = blockEntity.getBlockPos();
                ((ServerChunkCache)blockEntity.getLevel().getChunkSource()).chunkMap
                .getPlayers(new ChunkPos(pos), false)
                .forEach(e -> e.connection.send(packet));
            }
        }
    }
}
