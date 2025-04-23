package pro.komaru.tridot.common.registry.item.skins;

import pro.komaru.tridot.*;
import pro.komaru.tridot.client.model.*;
import pro.komaru.tridot.client.model.armor.*;
import net.minecraft.client.model.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.*;

public class ItemSkinEntry{

    public boolean appliesOn(ItemStack itemStack){
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public ArmorModel armorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel _default){
        return TridotModels.EMPTY_ARMOR;
    }

    @OnlyIn(Dist.CLIENT)
    public String armorTexture(Entity entity, ItemStack stack, EquipmentSlot slot, String type){
        return Tridot.ID + ":textures/models/armor/skin/empty.png";
    }

    @OnlyIn(Dist.CLIENT)
    public String itemModel(ItemStack stack){
        return null;
    }
}
