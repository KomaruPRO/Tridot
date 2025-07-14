package pro.komaru.tridot;

import net.minecraft.core.registries.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraftforge.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.registries.*;
import pro.komaru.tridot.api.tabs.*;

import java.util.*;

@Mod.EventBusSubscriber(modid = Tridot.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public abstract class ItemTabRegistry{
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Tridot.ID);

    public static final RegistryObject<CreativeModeTab> VALORIA_BLOCKS_TAB = CREATIVE_MODE_TABS.register("valoria_blocks", () -> MultiCreativeTab.builder().title(Component.literal(Tridot.ID))
    .withSubTabs(ItemTabRegistry.ALL, ItemTabRegistry.TEST1, ItemTabRegistry.TEST2, ItemTabRegistry.TEST3, ItemTabRegistry.TEST4, ItemTabRegistry.TEST5, ItemTabRegistry.TEST6).build());

    public static final SubCreativeTab ALL =
    SubCreativeTab.create().icon(() -> new ItemStack(Items.DIAMOND))
    .title(Component.translatable("subtab.tridot.test", 1));

    public static final SubCreativeTab TEST1 =
    SubCreativeTab.create().icon(() -> new ItemStack(Items.IRON_AXE))
    .title(Component.translatable("subtab.tridot.test", 2));

    public static final SubCreativeTab TEST2 =
    SubCreativeTab.create().icon(() -> new ItemStack(Items.HOGLIN_SPAWN_EGG))
    .title(Component.translatable("subtab.tridot.test", 2));

    public static final SubCreativeTab TEST3 =
    SubCreativeTab.create().icon(() -> new ItemStack(Items.HOGLIN_SPAWN_EGG))
    .title(Component.translatable("subtab.tridot.test", 3));

    public static final SubCreativeTab TEST4 =
    SubCreativeTab.create().icon(() -> new ItemStack(Items.HOGLIN_SPAWN_EGG))
    .title(Component.translatable("subtab.tridot.test", 4));

    public static final SubCreativeTab TEST5 =
    SubCreativeTab.create().icon(() -> new ItemStack(Items.HOGLIN_SPAWN_EGG))
    .title(Component.translatable("subtab.tridot.test", 5));

    public static final SubCreativeTab TEST6 =
    SubCreativeTab.create().icon(() -> new ItemStack(Items.HOGLIN_SPAWN_EGG))
    .title(Component.translatable("subtab.tridot.test", 6));

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }

    public static void addCreative(BuildCreativeModeTabContentsEvent event){
        if (event.getTabKey() == VALORIA_BLOCKS_TAB.getKey()){
            for(RegistryObject<Item> item : Tridot.ITEMS.getEntries()){
                addInSub(event, ALL, item.get());
            }
        }
    }

    public static void addInSub(BuildCreativeModeTabContentsEvent event, SubCreativeTab subTab, ItemLike item) {
        event.accept(item);
        subTab.addDisplayItem(item);
    }

    public static void addInSub(BuildCreativeModeTabContentsEvent event, SubCreativeTab subTab, ItemStack item) {
        event.accept(item);
        subTab.addDisplayItem(item);
    }

    public static void addInSub(BuildCreativeModeTabContentsEvent event, SubCreativeTab subTab, Collection<ItemStack> items) {
        event.acceptAll(items);
        subTab.addDisplayItems(items);
    }
}