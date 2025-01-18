package pro.komaru.tridot.registry;

import pro.komaru.tridot.*;
import pro.komaru.tridot.core.interfaces.*;
import pro.komaru.tridot.registry.enchantments.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.registries.*;
import pro.komaru.tridot.TridotLib;
import pro.komaru.tridot.core.interfaces.CooldownReductionItem;
import pro.komaru.tridot.core.interfaces.DashItem;
import pro.komaru.tridot.core.interfaces.RadiusItem;
import pro.komaru.tridot.registry.enchantments.DashEnchantment;
import pro.komaru.tridot.registry.enchantments.OverdriveEnchantment;
import pro.komaru.tridot.registry.enchantments.RadiusEnchantment;

import java.util.function.*;

public class EnchantmentsRegistry {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, TridotLib.ID);
    public static final EnchantmentCategory DASH_WEAPON = EnchantmentCategory.create("radius_weapon", item -> item instanceof DashItem);
    public static final EnchantmentCategory RADIUS_WEAPON = EnchantmentCategory.create("radius_weapon", item -> item instanceof RadiusItem);
    public static final EnchantmentCategory OVERDRIVE_CATEGORY = EnchantmentCategory.create("overdrive", item -> item instanceof CooldownReductionItem);

    public static final RegistryObject<Enchantment> DASH = registerEnchantment("dash", DashEnchantment::new);
    public static final RegistryObject<Enchantment> RADIUS = registerEnchantment("radius", RadiusEnchantment::new);
    public static final RegistryObject<Enchantment> OVERDRIVE = registerEnchantment("overdrive", OverdriveEnchantment::new);

    private static RegistryObject<Enchantment> registerEnchantment(String id, Supplier<Enchantment> enchantment) {
        return ENCHANTMENTS.register(id, enchantment);
    }

    public static void register(IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
    }
}