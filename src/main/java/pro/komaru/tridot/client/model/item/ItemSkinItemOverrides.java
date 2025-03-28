package pro.komaru.tridot.client.model.item;

import pro.komaru.tridot.common.registry.item.skins.ItemSkin;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;

import javax.annotation.*;

public class ItemSkinItemOverrides extends CustomItemOverrides{

    @Override
    public BakedModel resolve(BakedModel originalModel, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed){
        ItemSkin skin = ItemSkin.itemSkin(stack);
        if(skin != null){
            String skinStr = skin.getItemModelName(stack);
            if(skinStr != null) return ItemSkinModels.getModelSkins(skinStr);
        }
        return originalModel;
    }
}