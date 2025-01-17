package github.iri.tridot.client.particle;

import github.iri.tridot.client.particle.options.BlockParticleOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.client.model.data.ModelData;

public class BlockParticle extends GenericParticle{

    public BlockParticle(ClientLevel level, BlockParticleOptions options, double x, double y, double z, double vx, double vy, double vz){
        super(level, options, null, x, y, z, vx, vy, vz);
        var model = Minecraft.getInstance().getBlockRenderer().getBlockModel(options.blockState);
        this.setSprite(model.getParticleIcon(ModelData.EMPTY));
    }
}
