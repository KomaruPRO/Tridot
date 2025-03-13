package pro.komaru.tridot.oclient.graphics.particle;

import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraftforge.client.model.data.*;
import pro.komaru.tridot.oclient.graphics.particle.options.BlockParticleOptions;

public class BlockParticle extends GenericParticle{

    public BlockParticle(ClientLevel level, BlockParticleOptions options, double x, double y, double z, double vx, double vy, double vz){
        super(level, options, null, x, y, z, vx, vy, vz);
        var model = Minecraft.getInstance().getBlockRenderer().getBlockModel(options.blockState);
        this.setSprite(model.getParticleIcon(ModelData.EMPTY));
    }
}
