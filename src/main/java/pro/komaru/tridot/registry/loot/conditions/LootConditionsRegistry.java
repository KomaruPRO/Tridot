package pro.komaru.tridot.registry.loot.conditions;

import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.storage.loot.predicates.*;

public class LootConditionsRegistry{
    public static final LootItemConditionType MOB_CATEGORY_CONDITION = new LootItemConditionType(new MobCategoryCondition.Serializer());
    public static final LootItemConditionType LOCAL_DATE_CONDITION = new LootItemConditionType(new LocalDateCondition.Serializer());

    public static void register(){
        Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, new ResourceLocation("valoria", "local_date"), LOCAL_DATE_CONDITION);
        Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, new ResourceLocation("valoria", "mob_category"), MOB_CATEGORY_CONDITION);
    }
}
