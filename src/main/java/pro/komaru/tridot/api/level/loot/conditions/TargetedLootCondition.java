package pro.komaru.tridot.api.level.loot.conditions;

import com.google.gson.*;
import net.minecraft.util.*;
import net.minecraft.world.level.storage.loot.LootContext.*;
import net.minecraft.world.level.storage.loot.predicates.*;

public abstract class TargetedLootCondition implements LootItemCondition{
    public EntityTarget target;
    public TargetedLootCondition(EntityTarget target) {
        this.target = target;
    }

    public abstract static class AbstractSerializer<T extends TargetedLootCondition> implements net.minecraft.world.level.storage.loot.Serializer<T>{
        @Override
        public void serialize(JsonObject json, T condition, JsonSerializationContext context){
            String name = "this";
            if(condition.target != null) name = condition.target.getName();
            json.addProperty("target", name);
        }

        public EntityTarget getTarget(JsonObject json) {
            String str = GsonHelper.getAsString(json, "target");
            return EntityTarget.getByName(str);
        }
    }
}
