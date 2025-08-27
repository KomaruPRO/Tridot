package pro.komaru.tridot.common.registry.item.armor;

import net.minecraft.sounds.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.ArmorItem.*;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.*;
import pro.komaru.tridot.common.registry.item.builders.*;
import pro.komaru.tridot.common.registry.item.builders.AbstractArmorBuilder.*;

import java.util.*;

public abstract class AbstractArmorRegistry implements ArmorMaterial, TridotArmorMat{
    public AbstractArmorBuilder<?> builder;
    public static final Map<ArmorMaterial, List<ArmorEffectData>> EFFECTS = new HashMap<>();
    public static final Map<ArmorMaterial, List<HitEffectData>> HIT_EFFECTS = new HashMap<>();

    public AbstractArmorRegistry(AbstractArmorBuilder<?> builder, List<ArmorEffectData> data, List<HitEffectData> hitData){
        this.builder = builder;
        if(data != null) EFFECTS.put(this, data);
        if(hitData != null) HIT_EFFECTS.put(this, hitData);
    }

    @Override
    public AbstractArmorBuilder<?> builder(){
        return builder;
    }

    @Override
    public int getDurabilityForType(Type pType){
        return builder.durability[pType.ordinal()] * builder.durabilityMultiplier;
    }

    public float getPercentDefenseForType(Type pType){
        return switch(pType){
            case HELMET -> builder.headProtectionAmount;
            case CHESTPLATE -> builder.chestplateProtectionAmount;
            case LEGGINGS -> builder.leggingsProtectionAmount;
            case BOOTS -> builder.bootsProtectionAmount;
        };
    }

    @Override
    public int getDefenseForType(Type pType){
        return switch(pType){
            case HELMET -> (int)builder.headProtectionAmount;
            case CHESTPLATE -> (int)builder.chestplateProtectionAmount;
            case LEGGINGS -> (int)builder.leggingsProtectionAmount;
            case BOOTS -> (int)builder.bootsProtectionAmount;
        };
    }

    @Override
    public int getEnchantmentValue(){
        return builder.enchantmentValue;
    }

    @Override
    @NotNull
    public SoundEvent getEquipSound(){
        return builder.equipSound;
    }

    @Override
    @NotNull
    public Ingredient getRepairIngredient(){
        return builder.repairIngredient.get();
    }

    @Override
    @NotNull
    public abstract String getName();

    @Override
    public float getToughness(){
        return builder.toughness;
    }

    @Override
    public float getKnockbackResistance(){
        return builder.knockbackResistance;
    }
}