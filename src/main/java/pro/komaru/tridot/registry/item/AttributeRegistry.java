package pro.komaru.tridot.registry.item;

import pro.komaru.tridot.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.registries.*;

public class AttributeRegistry {
    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.Keys.ATTRIBUTES, TridotLib.ID);
    public static final RegistryObject<Attribute> PROJECTILE_DAMAGE = ATTRIBUTES.register("projectile_damage", () -> new RangedAttribute("attribute.tridot.projectile_damage", 0.0D, 0.0D, 1024.0D).setSyncable(true));
    public static final RegistryObject<Attribute> DASH_DISTANCE = ATTRIBUTES.register("dash_distance", () -> new RangedAttribute("attribute.tridot.dash_distance", 0.0D, 0.0D, 128.0D).setSyncable(true));
    public static final RegistryObject<Attribute> ATTACK_RADIUS = ATTRIBUTES.register("attack_radius", () -> new RangedAttribute("attribute.tridot.attack_radius", 0.0D, 0.0D, 32.0D).setSyncable(true));

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
}