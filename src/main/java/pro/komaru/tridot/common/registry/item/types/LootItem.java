package pro.komaru.tridot.common.registry.item.types;

import net.minecraft.advancements.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.stats.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import pro.komaru.tridot.api.Utils;

public abstract class LootItem extends Item{
    public ResourceLocation loot;

    public LootItem(ResourceLocation loot, Properties properties){
        super(properties);
        this.loot = loot;
    }

    public abstract SoundEvent getOpenSound();

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand hand){
        ItemStack heldStack = player.getItemInHand(hand);
        worldIn.playSound(null, player.blockPosition(), getOpenSound(), SoundSource.PLAYERS, 1f, 1f);
        if(player instanceof ServerPlayer serverPlayer){
            Vec3 playerPos = serverPlayer.position();
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, heldStack);
            if(!serverPlayer.isCreative()){
                heldStack.shrink(1);
            }

            Utils.Items.giveLoot(serverPlayer, Utils.Items.createLoot(loot, Utils.Items.getGiftParameters((ServerLevel)worldIn, playerPos, serverPlayer.getLuck(), serverPlayer)));
            return InteractionResultHolder.consume(heldStack);
        }

        return InteractionResultHolder.consume(heldStack);
    }
}