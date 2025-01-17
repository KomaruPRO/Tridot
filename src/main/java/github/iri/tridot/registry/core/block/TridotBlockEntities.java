package github.iri.tridot.registry.core.block;

import github.iri.tridot.*;
import github.iri.tridot.common.block.sign.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.registries.*;

public class TridotBlockEntities{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TridotLib.ID);

    public static final RegistryObject<BlockEntityType<CustomSignBlockEntity>> SIGN = BLOCK_ENTITIES.register("sign", () -> BlockEntityType.Builder.of(CustomSignBlockEntity::new, TridotBlocks.getBlocks(CustomStandingSignBlock.class, CustomWallSignBlock.class)).build(null));
    public static final RegistryObject<BlockEntityType<CustomHangingSignBlockEntity>> HANGING_SIGN = BLOCK_ENTITIES.register("hanging_sign", () -> BlockEntityType.Builder.of(CustomHangingSignBlockEntity::new, TridotBlocks.getBlocks(CustomCeilingHangingSignBlock.class, CustomWallHangingSignBlock.class)).build(null));

    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }

    @Mod.EventBusSubscriber(modid = TridotLib.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientRegistryEvents{
        @SubscribeEvent
        public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event){
            BlockEntityRenderers.register(SIGN.get(), SignRenderer::new);
            BlockEntityRenderers.register(HANGING_SIGN.get(), HangingSignRenderer::new);
        }
    }
}
