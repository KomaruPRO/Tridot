package pro.komaru.tridot.client.model.render.entity;

import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraftforge.registries.*;
import org.jetbrains.annotations.*;
import pro.komaru.tridot.*;
import pro.komaru.tridot.common.registry.block.entity.TridotTrappedChestBlockEntity;

public class TridotTrappedChestRender extends ChestRenderer<TridotTrappedChestBlockEntity> {

    public static final ResourceLocation CHEST_SHEET = new ResourceLocation("textures/atlas/chest.png");

    public TridotTrappedChestRender(BlockEntityRendererProvider.Context pContext) {
        super(pContext);
    }

    private static Material chestMaterial(String pChestName) {
        return new Material(CHEST_SHEET, new ResourceLocation(TridotLib.ID, "entity/chest/" + pChestName));
    }

    @Override
    public @NotNull Material getMaterial(@NotNull TridotTrappedChestBlockEntity tile, @NotNull ChestType type) {
        Block block = tile.getBlockState().getBlock();
        if (type.name().equals("SINGLE")) {
            return chestMaterial(ForgeRegistries.BLOCKS.getKey(block).getPath());
        } else {
            return chestMaterial(ForgeRegistries.BLOCKS.getKey(block).getPath() + "_" + type.name().toLowerCase());
        }
    }
}
