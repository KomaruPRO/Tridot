package pro.komaru.tridot.common.registry.item.armor;

import net.minecraft.client.*;
import net.minecraft.client.model.*;
import net.minecraft.network.chat.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.extensions.common.*;
import net.minecraftforge.common.extensions.*;
import pro.komaru.tridot.api.*;
import pro.komaru.tridot.client.model.armor.*;
import pro.komaru.tridot.common.registry.item.skins.*;

import java.util.*;

public class SkinableArmorItem extends PercentageArmorItem implements IForgeItem{
    public SkinableArmorItem(ArmorMaterial pMaterial, Type pType, Properties pProperties){
        super(pMaterial, pType, pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type){
        ItemSkin skin = ItemSkin.itemSkin(stack);
        if(skin == null) return super.getArmorTexture(stack, entity, slot, type);
        return skin.getArmorTexture(stack, entity, slot, type);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void initializeClient(java.util.function.Consumer<IClientItemExtensions> consumer){
        consumer.accept(new IClientItemExtensions(){
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel original){
                HumanoidModel<?> model = getModel(entity, itemStack, armorSlot, original);
                if(model != null) return model;

                return original;
            }
        });
    }

    public HumanoidModel<?> getModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> original) {
        ItemSkin skin = ItemSkin.itemSkin(itemStack);
        if(skin != null){
            HumanoidModel<?> model =  skin.getHumanoidModel(entity, itemStack, armorSlot, original);
            if(model instanceof ArmorModel) {
                return getArmorModel(entity, itemStack, armorSlot, original);
            }

            return model;
        }

        return null;
    }

    public ArmorModel getArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> original){
        float partialTicks = Minecraft.getInstance().getFrameTime();
        float f = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
        float f1 = Mth.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRot);
        float netHeadYaw = f1 - f;
        float netHeadPitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());

        ArmorModel model;
        ItemSkin skin = ItemSkin.itemSkin(itemStack);
        if(skin != null){
            model = skin.getArmorModel(entity, itemStack, armorSlot, original);
            model.slot = type.getSlot();
            model.copyFromDefault(original);
            model.setupAnim(entity, entity.walkAnimation.position(partialTicks), entity.walkAnimation.speed(partialTicks), entity.tickCount + partialTicks, netHeadYaw, netHeadPitch);
            return model;
        }
        return null;
    }
}
