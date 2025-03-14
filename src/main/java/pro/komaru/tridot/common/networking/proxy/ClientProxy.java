package pro.komaru.tridot.common.networking.proxy;

import net.minecraft.client.*;
import net.minecraft.client.sounds.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraftforge.api.distmarker.*;
import pro.komaru.tridot.*;
import pro.komaru.tridot.client.sound.*;

import java.util.*;

public class ClientProxy implements ISidedProxy{
    public static Map<UUID, String> bossbars = new HashMap<>();

    @Override
    public Player getPlayer(){
        return Minecraft.getInstance().player;
    }

    @Override
    public Level getLevel(){
        return Minecraft.getInstance().level;
    }

    @OnlyIn(Dist.CLIENT)
    public static void playSound(SoundEvent event){
        SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        if(TridotLibClient.BOSS_MUSIC != null && soundManager.isActive(TridotLibClient.BOSS_MUSIC)){
            return;
        }

        TridotLibClient.BOSS_MUSIC = new LoopedSoundInstance(event, SoundSource.MUSIC, Minecraft.getInstance().player);
        soundManager.play(TridotLibClient.BOSS_MUSIC);
        if(!soundManager.isActive(TridotLibClient.BOSS_MUSIC)){
            TridotLibClient.BOSS_MUSIC = null;
        }
    }

    public void removeBossBarRender(UUID bossBar){
        bossbars.remove(bossBar);
        if(TridotLibClient.BOSS_MUSIC != null){
            Minecraft.getInstance().getSoundManager().stop(TridotLibClient.BOSS_MUSIC);
        }
    }

    public void setBossBarRender(UUID bossBar, String id, SoundEvent bossMusic){
        bossbars.put(bossBar, id);
        playSound(bossMusic);
    }
}