package github.iri.tridot.util;

import com.google.common.collect.*;
import github.iri.tridot.*;
import github.iri.tridot.registry.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.*;

import java.util.*;
import java.util.function.*;

public class Utils{

    public static ItemStack predicate(Player player, ItemStack pShootable, Predicate<ItemStack> predicate) {
        for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
            ItemStack ammo = player.getInventory().getItem(i);
            if (predicate.test(ammo)) {
                return net.minecraftforge.common.ForgeHooks.getProjectile(player, pShootable, ammo);
            }
        }

        // why cobblestone? i dunno too
        return player.isCreative() ? Items.COBBLESTONE.getDefaultInstance() : ItemStack.EMPTY;
    }

    /**
     * Searches items in player inventory that equals the value of predicate
     */
    public static ItemStack getProjectile(Player player, ItemStack pShootable, Predicate<ItemStack> predicate) {
        return predicate(player, pShootable, predicate);
    }

    /**
     * Searches items in player inventory that equals of tagged items
     */
    public static ItemStack getProjectile(Player player, ItemStack pShootable, TagKey<Item> pTag) {
        Predicate<ItemStack> predicate = (stack) -> stack.is(pTag);
        return predicate(player, pShootable, predicate);
    }

    public static void configExplode(Player player, ItemStack itemstack, Vec3 pos, Vec3 clipPos, float radius, float damage, float knockback) {
        Level level = player.level();
        RandomSource rand = level.random;
        List<Entity> entities = level.getEntitiesOfClass(Entity.class, new AABB(pos.x + clipPos.x - radius, pos.y + clipPos.y - radius, pos.z + clipPos.z - radius, pos.x + clipPos.x + radius, pos.y + clipPos.y + radius, pos.z + clipPos.z + radius));
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity enemy) {
                if (!enemy.equals(player)) {
                    enemy.hurt(level.damageSources().generic(), damage);
                    enemy.knockback(knockback, player.getX() + clipPos.x - entity.getX(), player.getZ() + clipPos.z - entity.getZ());
                    if (EnchantmentHelper.getTagEnchantmentLevel(Enchantments.FIRE_ASPECT, itemstack) > 0) {
                        int i = EnchantmentHelper.getFireAspect(player);
                        enemy.setSecondsOnFire(i * 4);
                    }
                }
            }
        }

        if (level instanceof ServerLevel srv) {
            srv.sendParticles(ParticleTypes.EXPLOSION_EMITTER, pos.x + clipPos.x, pos.y + clipPos.y, player.getZ() + clipPos.z, 1, 0, 0, 0, radius);
            srv.playSound(null, player.blockPosition().offset((int) clipPos.x, (int) (clipPos.y + player.getEyeHeight()), (int) clipPos.z), SoundEvents.GENERIC_EXPLODE, SoundSource.AMBIENT, 10f, 1f);
            srv.sendParticles(ParticleTypes.LARGE_SMOKE, pos.x + clipPos.x + ((rand.nextDouble() - 0.5D) * radius), pos.y + clipPos.y + ((rand.nextDouble() - 0.5D) * radius), pos.z + clipPos.z + ((rand.nextDouble() - 0.5D) * radius), 8, 0.05d * ((rand.nextDouble() - 0.5D) * radius), 0.05d * ((rand.nextDouble() - 0.5D) * radius), 0.05d * ((rand.nextDouble() - 0.5D) * radius), 0.2f);
            srv.sendParticles(ParticleTypes.FLAME, pos.x + clipPos.x + ((rand.nextDouble() - 0.5D) * radius), pos.y + clipPos.y + ((rand.nextDouble() - 0.5D) * radius), pos.z + clipPos.z + ((rand.nextDouble() - 0.5D) * radius), 6, 0.05d * ((rand.nextDouble() - 0.5D) * radius), 0.05d * ((rand.nextDouble() - 0.5D) * radius), 0.05d * ((rand.nextDouble() - 0.5D) * radius), 0.2f);
        }
    }

    public static void chanceEffect(LivingEntity pTarget, ImmutableList<MobEffectInstance> effects, float chance) {
        if (!effects.isEmpty()) {
            for (MobEffectInstance effectInstance : effects) {
                if (new ArcRandom().chance(chance)) {
                    pTarget.addEffect(new MobEffectInstance(effectInstance));
                }
            }
        }
    }

    public static void addList(List<Item> list, Item... T) {
        Collections.addAll(list, T);
    }

    public static ToIntFunction<BlockState> setLightValue(int pValue) {
        return (state) -> !state.isAir() ? pValue : 0;
    }

    public static ToIntFunction<BlockState> getLightValueLit() {
        return (state) -> state.getValue(BlockStateProperties.LIT) ? 13 : 0;
    }

    public static ToIntFunction<BlockState> getPlantLightValue() {
        return (state) -> !state.isAir() ? 9 : 0;
    }
}
