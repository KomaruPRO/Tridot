package pro.komaru.tridot.api.level.loot;

import com.google.common.base.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraftforge.common.loot.*;
import net.minecraftforge.registries.*;
import pro.komaru.tridot.*;

import javax.annotation.*;
import java.util.function.Supplier;

public class AddLootModifier extends LootModifier{
    public static final Supplier<Codec<AddLootModifier>> CODEC = Suppliers.memoize(() ->
    RecordCodecBuilder.create(inst -> codecStart(inst).and(inst.group(ResourceLocation.CODEC.fieldOf("loot_table").forGetter(m -> m.lootTable),
    Codec.FLOAT.optionalFieldOf("chance", 1.0F).forGetter((m) -> m.chance))).apply(inst, AddLootModifier::new)));

    private final ResourceLocation lootTable;
    private final float chance;

    public AddLootModifier(LootItemCondition[] conditionsIn, ResourceLocation lootTable, float chance) {
        super(conditionsIn);
        this.lootTable = lootTable;
        this.chance = chance;
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if(context.getRandom().nextFloat() <= chance){
            LootTable extraLoot = context.getLevel().getServer().getLootData().getLootTable(this.lootTable);
            extraLoot.getRandomItemsRaw(context, LootTable.createStackSplitter(context.getLevel(), generatedLoot::add));
        }

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}