package pro.komaru.tridot.api.level.loot.conditions;

import com.google.gson.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraftforge.registries.*;
import pro.komaru.tridot.api.level.event.*;

import java.util.*;

public record EventActiveCondition(ResourceLocation eventId) implements LootItemCondition{

    @Override
    public boolean test(LootContext context) {
        ServerLevel server = context.getLevel();
        if (server != null) {
            return GameplayEventManager.get(server).isEventActive(this.eventId);
        }

        return false;
    }

    @Override
    public LootItemConditionType getType() {
        return LootConditionsRegistry.ACTIVE_EVENT_CONDITION.get();
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<EventActiveCondition> {
        @Override
        public void serialize(JsonObject json, EventActiveCondition condition, JsonSerializationContext context){
            json.addProperty("event", condition.eventId.toString());
        }

        @Override
        public EventActiveCondition deserialize(JsonObject json, JsonDeserializationContext context){
            String str = GsonHelper.getAsString(json, "event");
            return new EventActiveCondition(ResourceLocation.tryParse(str));
        }
    }
}