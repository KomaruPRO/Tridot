package pro.komaru.tridot.api.render.bossbars;

import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import pro.komaru.tridot.core.net.*;
import pro.komaru.tridot.core.net.packets.*;

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
        if(id != this.id){
            this.id = id;
            PacketHandler.sendToAll(new UpdateBossbarPacket(this.getId(), id, bossMusic));
        }
    }

    public void addPlayer(ServerPlayer serverPlayer){
        PacketHandler.sendNonLocal(new UpdateBossbarPacket(this.getId(), id, bossMusic), serverPlayer);
        super.addPlayer(serverPlayer);
    }

    public void removePlayer(ServerPlayer serverPlayer){
        PacketHandler.sendNonLocal(new UpdateBossbarPacket(this.getId(), "empty", bossMusic), serverPlayer);
        super.removePlayer(serverPlayer);
    }
}