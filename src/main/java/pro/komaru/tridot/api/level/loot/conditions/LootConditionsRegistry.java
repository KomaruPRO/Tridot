package pro.komaru.tridot.api.level.loot.conditions;

import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.registries.*;
import pro.komaru.tridot.*;

public class LootConditionsRegistry{
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPES = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, Tridot.ID);
    public static final RegistryObject<LootItemConditionType> LOCAL_DATE_CONDITION = LOOT_CONDITION_TYPES.register("local_date", () -> new LootItemConditionType(new LocalDateCondition.Serializer()));
    public static final RegistryObject<LootItemConditionType> DIFFICULTY_CONDITION = LOOT_CONDITION_TYPES.register("difficulty", () -> new LootItemConditionType(new DifficultyCondition.Serializer()));
    public static final RegistryObject<LootItemConditionType> MOB_CATEGORY_CONDITION = LOOT_CONDITION_TYPES.register("mob_category", () -> new LootItemConditionType(new MobCategoryCondition.Serializer()));
    public static final RegistryObject<LootItemConditionType> MOB_EFFECT_CONDITION = LOOT_CONDITION_TYPES.register("mob_effect", () -> new LootItemConditionType(new MobEffectCondition.Serializer()));
    public static final RegistryObject<LootItemConditionType> ACTIVE_EVENT_CONDITION = LOOT_CONDITION_TYPES.register("active_event", () -> new LootItemConditionType(new EventActiveCondition.Serializer()));

    public static void init(IEventBus bus){
        LOOT_CONDITION_TYPES.register(bus);
    }
}
