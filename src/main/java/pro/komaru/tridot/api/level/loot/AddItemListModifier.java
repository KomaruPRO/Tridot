package pro.komaru.tridot.api.level.loot;

import com.google.common.base.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraftforge.common.loot.*;
import net.minecraftforge.registries.*;

import javax.annotation.*;
import java.util.*;
import java.util.function.Supplier;

public class AddItemListModifier extends LootModifier{
    public static final Supplier<Codec<AddItemListModifier>> CODEC = Suppliers.memoize(()
    -> RecordCodecBuilder.create(inst -> codecStart(inst).and(ExtraCodecs.nonEmptyList(ForgeRegistries.ITEMS.getCodec().listOf())
    .fieldOf("items").forGetter(m -> m.items)).apply(inst, AddItemListModifier::new)));

    private final List<Item> items;

    protected AddItemListModifier(LootItemCondition[] conditions, List<Item> items){
        super(conditions);
        this.items = items;
    }

    @Nonnull
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context){
        ObjectArrayList<ItemStack> newLoot = new ObjectArrayList<>();

        for(LootItemCondition condition : this.conditions){
            if(!condition.test(context)){
                return generatedLoot;
            }
        }

        items.forEach(item -> generatedLoot.add(item.getDefaultInstance()));
        newLoot.add(Util.getRandom(generatedLoot, context.getRandom()));
        return newLoot;
    }

    public Codec<? extends IGlobalLootModifier> codec(){
        return CODEC.get();
    }
}
