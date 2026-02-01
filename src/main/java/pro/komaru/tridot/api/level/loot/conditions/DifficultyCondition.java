package pro.komaru.tridot.api.level.loot.conditions;

import com.google.gson.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import org.jetbrains.annotations.*;

import javax.annotation.Nullable;
import java.time.*;

public class DifficultyCondition implements LootItemCondition{
    public final Difficulty difficulty;
    public final boolean exactMatch;

    DifficultyCondition(Difficulty difficulty, boolean exactMatch){
        this.difficulty = difficulty;
        this.exactMatch = exactMatch;
    }

    @NotNull
    public LootItemConditionType getType(){
        return LootConditionsRegistry.DIFFICULTY_CONDITION.get();
    }

    public boolean test(LootContext lootContext){
        Level level = lootContext.getLevel();
        var currentID = level.getDifficulty().getId();
        var targetID = this.difficulty.getId();
        return exactMatch ? targetID == currentID : targetID >= currentID;
    }

    public static Builder difficulty(Difficulty difficulty, boolean exactMatch){
        return new Builder(difficulty, exactMatch);
    }

    public static class Builder implements LootItemCondition.Builder{
        private final Difficulty difficulty;
        public final boolean exactMatch;

        public Builder(Difficulty difficulty, boolean exactMatch){
            this.difficulty = difficulty;
            this.exactMatch = exactMatch;
        }

        public DifficultyCondition build(){
            return new DifficultyCondition(this.difficulty, exactMatch);
        }
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<DifficultyCondition>{
        @Override
        public void serialize(JsonObject json, DifficultyCondition condition, JsonSerializationContext context){
            json.add("difficulty", context.serialize(condition.difficulty));
            json.add("exact_match", context.serialize(condition.exactMatch));
        }

        @Override
        public DifficultyCondition deserialize(JsonObject json, JsonDeserializationContext context){
            Difficulty difficulty = GsonHelper.getAsObject(json, "difficulty", context, Difficulty.class);
            boolean exactMatch = GsonHelper.getAsBoolean(json, "exact_match", false);
            return new DifficultyCondition(difficulty, exactMatch);
        }
    }
}