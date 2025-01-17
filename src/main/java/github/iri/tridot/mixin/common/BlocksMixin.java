package github.iri.tridot.mixin.common;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(Blocks.class)
public abstract class BlocksMixin{
    @ModifyArg(method = "<clinit>",
    at = @At(value = "INVOKE",
    target = "Lnet/minecraft/world/level/block/FlowerBlock;<init>(Lnet/minecraft/world/effect/MobEffect;ILnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V",
    ordinal = 0),
    slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=torchflower")))
    private static BlockBehaviour.Properties fluffy_fur$modifyTorchflower(BlockBehaviour.Properties properties){
        return properties.lightLevel(blockState -> 12);
    }

    @ModifyArg(method = "flowerPot(Lnet/minecraft/world/level/block/Block;[Lnet/minecraft/world/flag/FeatureFlag;)Lnet/minecraft/world/level/block/FlowerPotBlock;",
    at = @At(value = "INVOKE",
    target = "Lnet/minecraft/world/level/block/FlowerPotBlock;<init>(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V",
    ordinal = 0))
    private static BlockBehaviour.Properties fluffy_fur$modifyPottedTorchflower(Block block, BlockBehaviour.Properties properties){
        if(block.getDescriptionId().equals("block.minecraft.torchflower"))
            return properties.lightLevel(blockState -> 12);
        return properties;
    }

    @ModifyArg(method = "<clinit>",
    at = @At(value = "INVOKE",
    target = "Lnet/minecraft/world/level/block/TorchflowerCropBlock;<init>(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V",
    ordinal = 0),
    slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=torchflower_crop")))
    private static BlockBehaviour.Properties fluffy_fur$modifyTorchflowerCrop(BlockBehaviour.Properties properties){

        return properties.lightLevel(blockState -> switch(blockState.getValue(TorchflowerCropBlock.AGE)){
            case 0 -> 4;
            case 1 -> 8;
            default -> 12;
        });
    }
}
