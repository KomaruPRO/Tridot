package pro.komaru.tridot.oregistry.item.skins;

import pro.komaru.tridot.oclient.*;
import pro.komaru.tridot.oclient.model.armor.*;
import net.minecraft.client.model.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.*;

import java.util.*;

//todo fluffy
public class ArmorClassSkinEntry extends ItemClassSkinEntry{
    public Map<EquipmentSlot, String> skins = new HashMap<>();

    public ArmorClassSkinEntry(Class item, String skin){
        super(item, skin);
    }

    public ArmorClassSkinEntry addArmorSkin(EquipmentSlot armorSlot, String skin){
        skins.put(armorSlot, skin);
        return this;
    }

    @Override
    public boolean canApplyOnItem(ItemStack itemStack){
        if(item.isInstance(itemStack.getItem())){
            if(itemStack.getItem() instanceof ArmorItem armor){
                return skins.containsKey(armor.getEquipmentSlot());
            }
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public String getItemModelName(ItemStack stack){
        if(stack.getItem() instanceof ArmorItem armor){
            return skins.get(armor.getEquipmentSlot());
        }
        return null;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ArmorModel getArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel _default){
        return TridotModels.EMPTY_ARMOR;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type){
        return skin;
    }
}
