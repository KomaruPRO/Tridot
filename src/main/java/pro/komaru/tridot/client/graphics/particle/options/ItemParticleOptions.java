package pro.komaru.tridot.client.graphics.particle.options;

import net.minecraft.core.particles.*;
import net.minecraft.world.item.*;

public class ItemParticleOptions extends GenericParticleOptions{
    public final ItemStack stack;

    public ItemParticleOptions(ParticleType<?> type, ItemStack stack){
        super(type);
        this.stack = stack;
    }
}
