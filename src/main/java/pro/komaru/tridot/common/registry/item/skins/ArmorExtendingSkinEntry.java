package pro.komaru.tridot.common.registry.item.skins;

import pro.komaru.tridot.client.model.*;
import pro.komaru.tridot.client.model.armor.*;
import net.minecraft.client.model.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.*;

import java.util.*;

public class ArmorExtendingSkinEntry extends ItemExtendingSkinEntry {
    public Map<EquipmentSlot, String> skins = new HashMap<>();

    public ArmorExtendingSkinEntry(Class<? extends Item> item, String skin){
        super(item, skin);
    }

    public ArmorExtendingSkinEntry skinFor(EquipmentSlot armorSlot, String skin){
        skins.put(armorSlot, skin);
        return this;
    }

    @Override
    public boolean appliesOn(ItemStack stack){
        return super.appliesOn(stack)
                && stack.getItem() instanceof ArmorItem armor
                && skins.containsKey(armor.getEquipmentSlot());
    }

    @OnlyIn(Dist.CLIENT)
    public String itemModel(ItemStack stack){
        if(!appliesOn(stack)) return null;
        ArmorItem armor = (ArmorItem) stack.getItem();
        return skins.get(armor.getEquipmentSlot());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ArmorModel armorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel _default){
        return TridotModels.EMPTY_ARMOR;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String armorTexture(Entity entity, ItemStack stack, EquipmentSlot slot, String type){
        return model;
    }
}
