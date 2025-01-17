package github.iri.tridot.client.model.item;

import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class CustomFinalisedModel implements BakedModel{
    public final BakedModel parentModel;
    public final BakedModel subModel;

    public CustomFinalisedModel(BakedModel parentModel, BakedModel subModel){
        this.parentModel = parentModel;
        this.subModel = subModel;
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction direction, RandomSource random){
        if(direction != null){
            return parentModel.getQuads(state, direction, random);
        }

        List<BakedQuad> combinedQuadsList = new ArrayList<>(parentModel.getQuads(state, direction, random));
        combinedQuadsList.addAll(subModel.getQuads(state, direction, random));
        return combinedQuadsList;
    }

    @Override
    public boolean useAmbientOcclusion(){
        return parentModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d(){
        return parentModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight(){
        return false;
    }

    @Override
    public boolean isCustomRenderer(){
        return parentModel.isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon(){
        return parentModel.getParticleIcon();
    }

    @Override
    public ItemTransforms getTransforms(){
        return parentModel.getTransforms();
    }

    @Override
    public ItemOverrides getOverrides(){
        return new CustomItemOverrides();
    }
}