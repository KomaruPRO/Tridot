package pro.komaru.tridot.client.model.item;

import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

//todo fluffy
public class CustomModel implements BakedModel{
    public final BakedModel baseModel;
    public final CustomItemOverrides itemOverrides;

    public CustomModel(BakedModel baseModel, CustomItemOverrides itemOverrides){
        this.baseModel = baseModel;
        this.itemOverrides = itemOverrides;
    }

    @Override
    public ItemOverrides getOverrides(){
        return itemOverrides;
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction direction, RandomSource random){
        return baseModel.getQuads(state, direction, random);
    }

    @Override
    public boolean useAmbientOcclusion(){
        return baseModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d(){
        return baseModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight(){
        return baseModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer(){
        return baseModel.isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon(){
        return baseModel.getParticleIcon();
    }

    @Override
    public ItemTransforms getTransforms(){
        return baseModel.getTransforms();
    }
}