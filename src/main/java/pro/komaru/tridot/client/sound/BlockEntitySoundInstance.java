package pro.komaru.tridot.client.sound;

import net.minecraft.client.resources.sounds.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.level.block.entity.*;

public class BlockEntitySoundInstance<T extends BlockEntity> extends AbstractTickableSoundInstance{

    public T blockEntity;
    public static RandomSource RANDOM = RandomSource.create();

    public BlockEntitySoundInstance(T blockEntity, SoundEvent soundEvent, float volume, float pitch){
        super(soundEvent, SoundSource.BLOCKS, RANDOM);
        this.blockEntity = blockEntity;
        this.volume = volume;
        this.pitch = pitch;
        this.delay = 0;
        this.looping = true;
    }

    @Override
    public void tick(){
        if(blockEntity.isRemoved()){
            stop();
        }
    }
}
