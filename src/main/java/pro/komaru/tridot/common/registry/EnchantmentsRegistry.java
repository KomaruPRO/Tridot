package pro.komaru.tridot.common.registry;

import pro.komaru.tridot.*;
import pro.komaru.tridot.api.interfaces.*;
import pro.komaru.tridot.common.registry.enchantments.DashEnchantment;
import pro.komaru.tridot.common.registry.enchantments.OverdriveEnchantment;
import pro.komaru.tridot.common.registry.enchantments.RadiusEnchantment;
import net.minecraft.world.item.enchantment.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.registries.*;

import java.util.function.*;

public class EnchantmentsRegistry {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Tridot.ID);
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