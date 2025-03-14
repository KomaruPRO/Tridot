package pro.komaru.tridot.common.registry.item.skins;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.*;

public class ItemExtendingSkinEntry extends ItemSkinEntry{
    public final Class<? extends Item> item;
    public String model;

    public ItemExtendingSkinEntry(Class<? extends Item> item, String model){
        this.item = item;
        this.model = model;
    }

    @Override
    public boolean appliesOn(ItemStack itemStack){
        return item.isInstance(itemStack.getItem());
    }

    @OnlyIn(Dist.CLIENT)
    public String itemModel(ItemStack stack){
        return model;
    }
    @Override
    @OnlyIn(Dist.CLIENT)
    public String armorTexture(Entity entity, ItemStack stack, EquipmentSlot slot, String type) {
        return model;
    }
}
