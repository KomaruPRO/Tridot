package pro.komaru.tridot.api.level.loot.conditions;

import com.google.gson.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.LootContext.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraftforge.registries.*;

import java.util.*;

public class MobEffectCondition extends TargetedLootCondition{
    private final MobEffect effect;

    public MobEffectCondition(EntityTarget target, MobEffect effect){
        super(target);
        this.effect = effect;
    }

    @Override
    public boolean test(LootContext lootContext){
        if(lootContext.getParamOrNull(target.getParam()) instanceof LivingEntity livingEntity){
            return livingEntity.hasEffect(effect);
        }
        return false;
    }

    @Override
    public LootItemConditionType getType(){
        return LootConditionsRegistry.MOB_EFFECT_CONDITION.get();
    }

    public static class Serializer extends TargetedLootCondition.AbstractSerializer<MobEffectCondition> {
        @Override
        public void serialize(JsonObject json, MobEffectCondition condition, JsonSerializationContext context){
            String key = "";
            if(ForgeRegistries.MOB_EFFECTS.getKey(condition.effect) != null) key = Objects.requireNonNull(ForgeRegistries.MOB_EFFECTS.getKey(condition.effect)).toString();
            json.addProperty("effect", key);
        }

        @Override
        public MobEffectCondition deserialize(JsonObject json, JsonDeserializationContext context){
            String str = GsonHelper.getAsString(json, "effect");
            return new MobEffectCondition(this.getTarget(json), ForgeRegistries.MOB_EFFECTS.getValue(ResourceLocation.of(str, ':')));
        }
    }
}