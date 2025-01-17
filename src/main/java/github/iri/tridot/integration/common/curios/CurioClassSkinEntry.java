package github.iri.tridot.integration.common.curios;

import github.iri.tridot.common.item.itemskin.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.*;

public class CurioClassSkinEntry extends ItemClassSkinEntry{

    public CurioClassSkinEntry(Class item, String skin){
        super(item, skin);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type){
        return skin;
    }
}
