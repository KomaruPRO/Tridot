package pro.komaru.tridot.registry.block;

import com.google.common.base.*;
import com.google.common.collect.*;
import pro.komaru.tridot.*;
import pro.komaru.tridot.registry.block.fire.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.registries.*;
import pro.komaru.tridot.TridotLib;
import pro.komaru.tridot.registry.block.fire.FireBlockHandler;
import pro.komaru.tridot.registry.block.fire.FireBlockModifier;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Supplier;

public class TridotBlocks{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TridotLib.ID);

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }

    public static FireBlock fireblock;

    public static Block[] getBlocks(Class<?>... blockClasses){
        IForgeRegistry<Block> blocks = ForgeRegistries.BLOCKS;
        ArrayList<Block> matchingBlocks = new ArrayList<>();
        for(Block block : blocks){
            if(Arrays.stream(blockClasses).anyMatch(b -> b.isInstance(block))){
                matchingBlocks.add(block);
            }
        }
        return matchingBlocks.toArray(new Block[0]);
    }

    public static Block[] getBlocksExact(Class<?> clazz){
        IForgeRegistry<Block> blocks = ForgeRegistries.BLOCKS;
        ArrayList<Block> matchingBlocks = new ArrayList<>();
        for(Block block : blocks){
            if(clazz.equals(block.getClass())){
                matchingBlocks.add(block);
            }
        }
        return matchingBlocks.toArray(new Block[0]);
    }

    public static void setFireBlock(){
        fireblock = (FireBlock)Blocks.FIRE;
        FireBlockHandler.register(new FireBlockModifier());
    }

    public static void axeStrippables(Block block, Block result){
        AxeItem.STRIPPABLES = new ImmutableMap.Builder<Block, Block>().putAll(AxeItem.STRIPPABLES).put(block, result).build();
    }

    public static void fireBlock(Block block, int encouragement, int flammability){
        fireblock.setFlammable(block, encouragement, flammability);
    }

    public static void weatheringCopper(Block block, Block result){
        try{
            Field delegateField = WeatheringCopper.NEXT_BY_BLOCK.getClass().getDeclaredField("delegate");
            delegateField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Supplier<BiMap<Block, Block>> originalWeatheringMapDelegate = (Supplier<BiMap<Block, Block>>)delegateField.get(WeatheringCopper.NEXT_BY_BLOCK);
            com.google.common.base.Supplier<BiMap<Block, Block>> weatheringMapDelegate = () -> {
                ImmutableBiMap.Builder<Block, Block> builder = ImmutableBiMap.builder();
                builder.putAll(originalWeatheringMapDelegate.get());
                builder.put(block, result);
                return builder.build();
            };

            delegateField.set(WeatheringCopper.NEXT_BY_BLOCK, weatheringMapDelegate);
        }catch(Exception e){
            TridotLib.LOGGER.error("Failed weathering copper", e);
        }
    }

    public static void waxedCopper(Block block, Block result){
        Supplier<BiMap<Block, Block>> originalWaxableMapSupplier = HoneycombItem.WAXABLES;
        HoneycombItem.WAXABLES = Suppliers.memoize(() -> {
            ImmutableBiMap.Builder<Block, Block> builder = ImmutableBiMap.builder();
            builder.putAll(originalWaxableMapSupplier.get());
            builder.put(block, result);
            return builder.build();
        });
    }
}
