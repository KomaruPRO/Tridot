package github.iri.tridot.registry;

import com.mojang.serialization.*;
import github.iri.tridot.*;
import github.iri.tridot.registry.loot.*;
import net.minecraftforge.common.loot.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.registries.*;

public class TridotLootModifier{
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, TridotLib.ID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM = LOOT_MODIFIERS.register("add_item", AddItemModifier.CODEC);
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM_LIST = LOOT_MODIFIERS.register("add_item_list", AddItemListModifier.CODEC);

    public static void register(IEventBus eventBus){
        LOOT_MODIFIERS.register(eventBus);
    }
}
