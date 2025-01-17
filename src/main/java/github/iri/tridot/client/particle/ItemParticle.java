package github.iri.tridot.client.particle;

import github.iri.tridot.client.particle.options.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraftforge.client.model.data.*;

public class ItemParticle extends GenericParticle{

    public ItemParticle(ClientLevel level, ItemParticleOptions options, double x, double y, double z, double vx, double vy, double vz){
        super(level, options, null, x, y, z, vx, vy, vz);
        var model = Minecraft.getInstance().getItemRenderer().getModel(options.stack, level, null, 0);
        this.setSprite(model.getOverrides().resolve(model, options.stack, level, null, 0).getParticleIcon(ModelData.EMPTY));
    }
}
