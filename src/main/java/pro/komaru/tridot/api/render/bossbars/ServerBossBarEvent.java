package pro.komaru.tridot.api.render.bossbars;

import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import pro.komaru.tridot.*;
import pro.komaru.tridot.api.networking.PacketHandler;
import pro.komaru.tridot.common.networking.packets.*;

import javax.annotation.*;
import java.util.*;

public class ServerBossBarEvent extends ServerBossEvent{
    private String id;
    private SoundEvent bossMusic;

    public ServerBossBarEvent(Component component, String id, SoundEvent bossMusic){
        super(component, BossBarColor.PURPLE, BossBarOverlay.PROGRESS);
        this.id = id;
        this.bossMusic = bossMusic;
    }

    public ServerBossBarEvent(Component component, String id){
        super(component, BossBarColor.PURPLE, BossBarOverlay.PROGRESS);
        this.id = id;
        this.bossMusic = SoundEvents.EMPTY;
    }

    public void setId(String id){
        if(!Objects.equals(id, this.id)){
            this.id = id;
            PacketHandler.sendToAll(new UpdateBossbarPacket(this.getId(), id, bossMusic));
        }
    }

    public void addPlayer(ServerPlayer serverPlayer){
        PacketHandler.sendNonLocal(new UpdateBossbarPacket(this.getId(), id, bossMusic), serverPlayer);
        super.addPlayer(serverPlayer);
    }

    public void removePlayer(ServerPlayer serverPlayer){
        PacketHandler.sendNonLocal(new RemoveBossbarPacket(this.getId()), serverPlayer);
        super.removePlayer(serverPlayer);
    }
}