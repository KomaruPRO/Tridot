package pro.komaru.tridot.registry.entity.projectiles;

import com.google.common.collect.*;
import net.minecraft.nbt.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AbstractTridotArrow extends AbstractArrow {
    public ItemStack arrowItem = ItemStack.EMPTY;
    private final Set<MobEffectInstance> effects = Sets.newHashSet();
    public AbstractTridotArrow(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public AbstractTridotArrow(EntityType<? extends AbstractArrow> pEntityType, Level worldIn, LivingEntity thrower, ItemStack thrownStackIn, int baseDamage) {
        super(pEntityType, thrower, worldIn);
        arrowItem = new ItemStack(thrownStackIn.getItem());
        this.setBaseDamage(baseDamage);
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.spawnParticlesTrail();
        }
    }

    public void addEffect(MobEffectInstance pEffectInstance) {
        this.effects.add(pEffectInstance);
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (!this.effects.isEmpty()) {
            ListTag listtag = new ListTag();
            for (MobEffectInstance mobeffectinstance : this.effects) {
                listtag.add(mobeffectinstance.save(new CompoundTag()));
            }

            compound.put("CustomPotionEffects", listtag);
        }
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        for (MobEffectInstance mobeffectinstance : PotionUtils.getCustomEffects(pCompound)) {
            this.addEffect(mobeffectinstance);
        }
    }

    protected void doPostHurtEffects(LivingEntity pLiving) {
        super.doPostHurtEffects(pLiving);
        Entity entity = this.getEffectSource();
        if (!this.effects.isEmpty()) {
            for (MobEffectInstance effect : this.effects) {
                pLiving.addEffect(effect, entity);
            }
        }
    }

    public void setEffectsFromList(ImmutableList<MobEffectInstance> effects) {
        for (MobEffectInstance mobeffectinstance : effects) {
            this.effects.add(new MobEffectInstance(mobeffectinstance));
        }
    }

    public void spawnParticlesTrail() {
    }

    @Override
    public ItemStack getPickupItem() {
        return arrowItem;
    }
}