package pro.komaru.tridot;

import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.fml.config.ModConfig.*;
import net.minecraftforge.registries.*;
import pro.komaru.tridot.client.*;
import pro.komaru.tridot.client.event.*;
import pro.komaru.tridot.client.graphics.gui.*;
import pro.komaru.tridot.core.config.*;
import pro.komaru.tridot.core.event.*;
import pro.komaru.tridot.core.interfaces.*;
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
import pro.komaru.tridot.registry.item.types.*;
import pro.komaru.tridot.registry.loot.conditions.*;

import java.util.*;

@Mod("tridot")
public class TridotLib{
    public static final String ID = "tridot";
    public static UUID BASE_PROJECTILE_DAMAGE_UUID = UUID.fromString("5334b818-69d4-417e-b4b8-1869d4917e29");
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ID);
    public static final RegistryObject<Item> TEST = ITEMS.register("test", () -> new TestItem(new Item.Properties().rarity(Rarity.EPIC)));

    public static final ISidedProxy proxy = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    public TridotLib(){
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EnchantmentsRegistry.register(eventBus);
        AttributeRegistry.register(eventBus);
        TridotBlocks.register(eventBus);
        TridotBlockEntities.register(eventBus);
        TridotParticles.register(eventBus);
        TridotLootModifier.register(eventBus);
        ITEMS.register(eventBus);

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
            forgeBus.addListener(OverlayRender::tick);
            forgeBus.addListener(OverlayRender::onDrawScreenPost);
            forgeBus.addListener(OverlayRenderItem::onDrawScreenPost);
            forgeBus.addListener(ClientTickHandler::clientTickEnd);
            TridotLibClient.clientInit();
            return new Object();
        });

        ModLoadingContext.get().registerConfig(Type.SERVER, ServerConfig.SPEC);
        ModLoadingContext.get().registerConfig(Type.CLIENT, ClientConfig.SPEC);
        eventBus.addListener(this::setup);
        eventBus.addListener(TridotLibClient::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new Events());
    }

    private void setup(final FMLCommonSetupEvent event){
        TridotBlocks.setFireBlock();
        PacketHandler.init();
        LootConditionsRegistry.register();
        for(ItemSkin skin : ItemSkinHandler.getSkins()){
            skin.setupSkinEntries();
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents{
        @SubscribeEvent
        public static void attachAttribute(EntityAttributeModificationEvent event) {
            event.add(EntityType.PLAYER, AttributeRegistry.PROJECTILE_DAMAGE.get());
        }
    }
}