package pro.komaru.tridot.client.gfx.particle.options;

import net.minecraft.core.particles.*;
import net.minecraft.world.level.block.state.*;

public class BlockParticleOptions extends GenericParticleOptions{
    public final BlockState blockState;

    public BlockParticleOptions(ParticleType<?> type, BlockState blockState){
        super(type);
        this.blockState = blockState;
    }
}
