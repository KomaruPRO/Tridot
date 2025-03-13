package pro.komaru.tridot.oclient.model.item;

import pro.komaru.tridot.oregistry.item.skins.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;

import javax.annotation.*;

//todo fluffy
public class ItemSkinItemOverrides extends CustomItemOverrides{

    @Override
    public BakedModel resolve(BakedModel originalModel, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed){
        ItemSkin skin = ItemSkin.getSkinFromItem(stack);
        if(skin != null){
            String skinStr = skin.getItemModelName(stack);
            if(skinStr != null) return ItemSkinModels.getModelSkins(skinStr);
        }
        return originalModel;
    }
}