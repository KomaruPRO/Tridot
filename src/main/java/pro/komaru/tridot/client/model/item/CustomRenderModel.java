package pro.komaru.tridot.client.model.item;

import net.minecraft.client.resources.model.*;

//todo fluffy
public class CustomRenderModel extends CustomModel{

    public CustomRenderModel(BakedModel baseModel, CustomItemOverrides itemOverrides){
        super(baseModel, itemOverrides);
    }

    @Override
    public boolean isCustomRenderer(){
        return true;
    }
}