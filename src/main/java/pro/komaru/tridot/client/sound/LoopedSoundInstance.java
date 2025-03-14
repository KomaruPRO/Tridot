package pro.komaru.tridot.client.sound;

import net.minecraft.client.player.*;
import net.minecraft.client.resources.sounds.*;
import net.minecraft.sounds.*;
import net.minecraftforge.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class LoopedSoundInstance extends AbstractTickableSoundInstance{
    public final LocalPlayer player;
    public LoopedSoundInstance(SoundEvent sound, LocalPlayer pPlayer) {
        super(sound, SoundSource.AMBIENT, SoundInstance.createUnseededRandom());
        this.player = pPlayer;
        this.looping = true;
        this.delay = 0;
        this.volume = 1.00F;
    }

    public LoopedSoundInstance(SoundEvent sound, SoundSource source, LocalPlayer pPlayer){
        super(sound, source, SoundInstance.createUnseededRandom());
        this.player = pPlayer;
        this.looping = true;
        this.delay = 0;
        this.volume = 1.00F;
    }

    public void tick() {
        if (this.player != null && !this.player.isRemoved()) {
            this.x = (float) this.player.getX();
            this.y = (float) this.player.getY();
            this.z = (float) this.player.getZ();
        } else {
            this.stop();
        }
    }
}