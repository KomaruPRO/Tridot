package pro.komaru.tridot.common.registry.item.builders;

import com.google.common.collect.*;
import net.minecraft.sounds.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;

import java.util.*;
import java.util.function.*;

//example: public static final ArmorRegistry COBALT = new ArmorRegistry.Builder("cobalt").protection(18).mul(46).enchantValue(18).knockbackRes(0.05f).ingredient(() -> Ingredient.of(ItemsRegistry.cobaltIngot.get())).build();
public abstract class AbstractArmorBuilder<T extends ArmorMaterial>{
    public String name;
    public int durabilityMultiplier;
    public int headPercent = 20;
    public int chestPercent = 35;
    public int leggingsPercent = 25;
    public int bootsPercent = 20;
    public int headProtectionAmount;
    public int chestplateProtectionAmount;
    public int leggingsProtectionAmount;
    public int bootsProtectionAmount;
    public int enchantmentValue;
    public int[] durability = {11, 16, 16, 13};
    public float toughness;
    public float knockbackResistance;

    public SoundEvent equipSound = SoundEvents.ARMOR_EQUIP_IRON;
    public Supplier<Ingredient> repairIngredient;
    public List<ArmorEffectData> data;
    public List<HitEffectData> hitData;
    public Multimap<Supplier<Attribute>, AttributeModifier> attributeMap;

    public AbstractArmorBuilder(String name){
        this.name = name;
    }

    /**
     * Represents an effect that can be applied by wearing a specific armor set.
     * @param instance Supplier for the MobEffectInstance to apply.
     * @param condition Predicate determining when the effect should apply.
     */
    public record ArmorEffectData(Supplier<MobEffectInstance> instance, Predicate<Player> condition) {
        public static final Predicate<Player> ALWAYS_TRUE = player -> true;
        public static final int INFINITE_DURATION = -1;
        public static ArmorEffectData createData(Supplier<MobEffect> effectSupplier) {
            return new ArmorEffectData(() -> new MobEffectInstance(effectSupplier.get(), INFINITE_DURATION), ALWAYS_TRUE);
        }

        public static ArmorEffectData createData(Supplier<MobEffectInstance> instance, Predicate<Player> condition) {
            return new ArmorEffectData(instance, condition);
        }
    }

    public record HitEffectData(Supplier<MobEffectInstance> instance, Predicate<Player> condition, float chance) {
        public static final Predicate<Player> ALWAYS_TRUE = player -> true;
        public static final int INFINITE_DURATION = -1;
        public static HitEffectData createData(Supplier<MobEffect> effectSupplier) {
            return new HitEffectData(() -> new MobEffectInstance(effectSupplier.get(), INFINITE_DURATION), ALWAYS_TRUE, 1);
        }

        public static HitEffectData createData(Supplier<MobEffectInstance> instance, Predicate<Player> condition, float chance) {
            return new HitEffectData(instance, condition, chance);
        }

        public static HitEffectData createData(Supplier<MobEffectInstance> instance, Predicate<Player> condition) {
            return new HitEffectData(instance, condition, 1);
        }
    }

    public AbstractArmorBuilder<T> addAttrs(Multimap<Supplier<Attribute>, AttributeModifier> map) {
        attributeMap.putAll(map);
        return this;
    }

    public AbstractArmorBuilder<T> setAttrs(Multimap<Supplier<Attribute>, AttributeModifier> map) {
        attributeMap = map;
        return this;
    }

    public AbstractArmorBuilder<T> addAttr(Supplier<Attribute> attribute, AttributeModifier mod) {
        attributeMap.put(attribute, mod);
        return this;
    }

    public AbstractArmorBuilder<T> hitEffects(List<HitEffectData> data) {
        this.hitData = data;
        return this;
    }

    public AbstractArmorBuilder<T> effects(List<ArmorEffectData> data) {
        this.data = data;
        return this;
    }

    public AbstractArmorBuilder<T> mul(int durabilityMul){
        this.durabilityMultiplier = durabilityMul;
        return this;
    }

    public AbstractArmorBuilder<T> durability(int head, int chest, int leggings, int boots) {
        this.durability = new int[]{head, chest, leggings, boots};
        return this;
    }

    public AbstractArmorBuilder<T> protection(int percent) {
        int head = (percent * headPercent) / 100;
        int chest = (percent * chestPercent) / 100;
        int leggings = (percent * leggingsPercent) / 100;
        int boots = (percent * bootsPercent) / 100;
        int remainder = percent - (head + chest + leggings + boots);
        chest += remainder;

        this.headProtectionAmount = head;
        this.chestplateProtectionAmount = chest;
        this.leggingsProtectionAmount = leggings;
        this.bootsProtectionAmount = boots;
        return this;
    }

    /**
     * Armor percent distribution between parts
     * @apiNote Default: 20, 35, 25, 20
     */
    public AbstractArmorBuilder<T> distribution(int head, int chest, int leggings, int boots) {
        this.headPercent = head;
        this.chestPercent = chest;
        this.leggingsPercent = leggings;
        this.bootsPercent = boots;
        return this;
    }

    public AbstractArmorBuilder<T> protection(int head, int chest, int leggings, int boots) {
        this.headProtectionAmount = head;
        this.chestplateProtectionAmount = chest;
        this.leggingsProtectionAmount = leggings;
        this.bootsProtectionAmount = boots;
        return this;
    }

    public AbstractArmorBuilder<T> enchantValue(int value){
        this.enchantmentValue = value;
        return this;
    }

    public AbstractArmorBuilder<T> sound(SoundEvent sound){
        this.equipSound = sound;
        return this;
    }

    public AbstractArmorBuilder<T> toughness(float value){
        this.toughness = value;
        return this;
    }

    public AbstractArmorBuilder<T> knockbackRes(float value){
        this.knockbackResistance = value;
        return this;
    }

    public AbstractArmorBuilder<T> ingredient(Supplier<Ingredient> value){
        this.repairIngredient = value;
        return this;
    }

    public abstract T build();
}