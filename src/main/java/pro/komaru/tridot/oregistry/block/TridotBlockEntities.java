package pro.komaru.tridot.oregistry.block;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType.*;
import pro.komaru.tridot.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.registries.*;
import pro.komaru.tridot.oclient.graphics.render.entity.*;
import pro.komaru.tridot.oregistry.block.chest.*;
import pro.komaru.tridot.oregistry.block.entity.*;
import pro.komaru.tridot.oregistry.block.sign.*;

import java.util.*;

public class TridotBlockEntities{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TridotLib.ID);

    public static final RegistryObject<BlockEntityType<CustomSignBlockEntity>> SIGN = BLOCK_ENTITIES.register("sign", () -> BlockEntityType.Builder.of(CustomSignBlockEntity::new, TridotBlocks.getBlocks(CustomStandingSignBlock.class, CustomWallSignBlock.class)).build(null));
    public static final RegistryObject<BlockEntityType<CustomHangingSignBlockEntity>> HANGING_SIGN = BLOCK_ENTITIES.register("hanging_sign", () -> BlockEntityType.Builder.of(CustomHangingSignBlockEntity::new, TridotBlocks.getBlocks(CustomCeilingHangingSignBlock.class, CustomWallHangingSignBlock.class)).build(null));

    public static final RegistryObject<BlockEntityType<TridotChestBlockEntity>> CHEST_BLOCK_ENTITY = BLOCK_ENTITIES.register("mod_chest", () -> {
        ArrayList<Block> chestBlocks = new ArrayList<>();
        for(var block : ForgeRegistries.BLOCKS.getEntries()) {
            if(block.getValue() instanceof TridotChestBlock chestBlock && chestBlock.autoReg) {
                chestBlocks.add(chestBlock) ;
            }
        }

        return Builder.of(TridotChestBlockEntity::new, chestBlocks.toArray(new Block[0])).build(null);
    });

    public static final RegistryObject<BlockEntityType<TridotTrappedChestBlockEntity>> TRAPPED_CHEST_BLOCK_ENTITY = BLOCK_ENTITIES.register("mod_trapped_chest", () -> {
        ArrayList<Block> chestBlocks = new ArrayList<>();
        for(var block : ForgeRegistries.BLOCKS.getEntries()) {
            if(block.getValue() instanceof TridotTrappedChestBlock chestBlock && chestBlock.autoReg) {
                chestBlocks.add(chestBlock) ;
            }
        }

        return Builder.of(TridotTrappedChestBlockEntity::new, chestBlocks.toArray(new Block[0])).build(null);
    });


    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }

    @Mod.EventBusSubscriber(modid = TridotLib.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientRegistryEvents{
        @SubscribeEvent
        public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event){
            BlockEntityRenderers.register(SIGN.get(), SignRenderer::new);
            BlockEntityRenderers.register(HANGING_SIGN.get(), HangingSignRenderer::new);
            BlockEntityRenderers.register(CHEST_BLOCK_ENTITY.get(), TridotChestRender::new);
            BlockEntityRenderers.register(TRAPPED_CHEST_BLOCK_ENTITY.get(), TridotTrappedChestRender::new);

        }
    }
}
