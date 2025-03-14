package pro.komaru.tridot.common.compatibility.curios;

import pro.komaru.tridot.common.registry.item.skins.ItemClassSkinEntry;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.*;

public class CurioClassSkinEntry extends ItemClassSkinEntry {

    public CurioClassSkinEntry(Class item, String skin){
        super(item, skin);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type){
        return skin;
    }
}
