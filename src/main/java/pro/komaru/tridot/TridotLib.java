package pro.komaru.tridot;

import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.registries.*;
import org.slf4j.Logger;
import pro.komaru.tridot.client.*;
import pro.komaru.tridot.core.config.*;
import pro.komaru.tridot.core.event.*;
import pro.komaru.tridot.core.net.*;
import pro.komaru.tridot.core.proxy.*;
import pro.komaru.tridot.registry.*;
import pro.komaru.tridot.registry.block.*;
import pro.komaru.tridot.registry.item.*;
import pro.komaru.tridot.registry.item.skins.*;
import net.minecraft.world.entity.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.entity.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.config.*;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.*;
import org.apache.logging.log4j.*;
import pro.komaru.tridot.client.TridotParticles;
import pro.komaru.tridot.core.config.ClientConfig;
import pro.komaru.tridot.core.event.Events;
import pro.komaru.tridot.core.net.PacketHandler;
import pro.komaru.tridot.core.proxy.ClientProxy;
import pro.komaru.tridot.core.proxy.ISidedProxy;
import pro.komaru.tridot.core.proxy.ServerProxy;
import pro.komaru.tridot.registry.EnchantmentsRegistry;
import pro.komaru.tridot.registry.TridotLootModifier;
import pro.komaru.tridot.registry.block.TridotBlockEntities;
import pro.komaru.tridot.registry.block.TridotBlocks;
import pro.komaru.tridot.registry.item.AttributeRegistry;
import pro.komaru.tridot.registry.item.skins.ItemSkin;
import pro.komaru.tridot.registry.item.skins.ItemSkinHandler;
import pro.komaru.tridot.registry.item.types.*;

import java.util.*;

@Mod("tridot")
public class TridotLib{
    public static final String ID = "tridot";
    public static final String NAME = "Tridot";
    public static final String VERSION = "0.0.1";
    public static final int VERSION_NUMBER = 7;
    public static UUID BASE_ENTITY_REACH_UUID = UUID.fromString("c2e6b27c-fff1-4296-a6b2-7cfff13296cf");
    public static UUID BASE_PROJECTILE_DAMAGE_UUID = UUID.fromString("5334b818-69d4-417e-b4b8-1869d4917e29");
    public static UUID BASE_DASH_DISTANCE_UUID = UUID.fromString("b0e5853a-d071-40db-a585-3ad07100db82");
    public static UUID BASE_ATTACK_RADIUS_UUID = UUID.fromString("49438567-6ad2-41bd-8385-676ad2a1bd5e");
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ID);
    public static final RegistryObject<Item> TEST = ITEMS.register("test", () -> new TestItem(new Item.Properties().rarity(Rarity.EPIC)));

    public static final ISidedProxy proxy = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    public TridotLib(){
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        AttributeRegistry.register(eventBus);
        EnchantmentsRegistry.register(eventBus);
        TridotBlocks.register(eventBus);
        TridotBlockEntities.register(eventBus);
        TridotParticles.register(eventBus);
        TridotLootModifier.register(eventBus);
        ITEMS.register(eventBus);

        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> {
            return () -> {
                TridotLibClient.clientInit();
                return new Object();
            };
        });

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        eventBus.addListener(this::setup);
        eventBus.addListener(TridotLibClient::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new Events());
    }

    private void setup(final FMLCommonSetupEvent event){
        TridotBlocks.setFireBlock();
        PacketHandler.init();
        for(ItemSkin skin : ItemSkinHandler.getSkins()){
            skin.setupSkinEntries();
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents{
        @SubscribeEvent
        public static void attachAttribute(EntityAttributeModificationEvent event) {
            event.add(EntityType.PLAYER, AttributeRegistry.DASH_DISTANCE.get());
            event.add(EntityType.PLAYER, AttributeRegistry.ATTACK_RADIUS.get());
            event.add(EntityType.PLAYER, AttributeRegistry.PROJECTILE_DAMAGE.get());
        }
    }
}