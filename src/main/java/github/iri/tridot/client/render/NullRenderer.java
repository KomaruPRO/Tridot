package github.iri.tridot.client.render;

import net.minecraft.client.renderer.culling.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraftforge.api.distmarker.*;

import javax.annotation.*;

@OnlyIn(Dist.CLIENT)
public class NullRenderer<T extends Entity> extends EntityRenderer<T> {
    public NullRenderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    @Override
    public boolean shouldRender(T entity, @Nonnull Frustum clipping, double x, double y, double z) {
        return true;
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull T entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}