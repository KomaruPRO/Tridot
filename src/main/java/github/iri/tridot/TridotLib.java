package github.iri.tridot;

import github.iri.tridot.client.*;
import github.iri.tridot.core.config.*;
import github.iri.tridot.registry.*;
import github.iri.tridot.registry.item.itemskin.*;
import github.iri.tridot.core.network.*;
import github.iri.tridot.core.proxy.*;
import github.iri.tridot.registry.block.*;
import github.iri.tridot.registry.event.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.common.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.config.*;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.*;
import org.apache.logging.log4j.*;

@Mod("tridot")
public class TridotLib{
    public static final String ID = "tridot";
    public static final String NAME = "Tridot";
    public static final String VERSION = "0.0.1";
    public static final int VERSION_NUMBER = 7;

    public static final ISidedProxy proxy = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    public static final Logger LOGGER = LogManager.getLogger();

    public TridotLib(){
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        TridotBlocks.register(eventBus);
        TridotBlockEntities.register(eventBus);
        TridotParticles.register(eventBus);
        TridotLootModifier.register(eventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
            TridotLibClient.ClientOnly.clientInit();
            return new Object();
        });

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
}