package pro.komaru.tridot.client.particle;

import pro.komaru.tridot.client.particle.options.*;
import pro.komaru.tridot.utilities.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.world.level.material.*;
import net.minecraftforge.client.extensions.common.*;
import net.minecraftforge.fluids.*;
import pro.komaru.tridot.client.particle.options.FluidParticleOptions;
import pro.komaru.tridot.utilities.client.RenderUtil;

public class FluidParticle extends GenericParticle{

    public FluidParticle(ClientLevel level, FluidParticleOptions options, double x, double y, double z, double vx, double vy, double vz){
        super(level, options, null, x, y, z, vx, vy, vz);
        if(!options.fluidStack.isEmpty()){
            FluidType type = options.fluidStack.getFluid().getFluidType();
            IClientFluidTypeExtensions clientType = IClientFluidTypeExtensions.of(type);
            TextureAtlasSprite sprite = RenderUtil.getSprite(clientType.getStillTexture(options.fluidStack));
            if(options.flowing) sprite = RenderUtil.getSprite(clientType.getFlowingTexture(options.fluidStack));
            this.setSprite(sprite);
        }else{
            IClientFluidTypeExtensions clientType = IClientFluidTypeExtensions.of(Fluids.WATER);
            TextureAtlasSprite sprite = RenderUtil.getSprite(clientType.getStillTexture());
            this.setSprite(sprite);
        }
    }
}
