package github.iri.tridot.integration.common.curios;

import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import top.theillusivec4.curios.api.*;
import top.theillusivec4.curios.api.type.capability.*;

public class BaseCurioItem extends Item implements ICurioItem, ICurioItemTexture{

    public BaseCurioItem(Properties properties){
        super(properties);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slot, ItemStack stack){
        return true;
    }

    @Override
    public ResourceLocation getTexture(ItemStack stack, LivingEntity entity){
        return null;
    }
}
