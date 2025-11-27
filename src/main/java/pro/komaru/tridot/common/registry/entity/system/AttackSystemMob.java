package pro.komaru.tridot.common.registry.entity.system;

import net.minecraft.nbt.*;

public interface AttackSystemMob{
    String NBT_COOLDOWNS_KEY = "AttackCooldowns";
    String NBT_CURRENT_ATTACK_KEY = "AttackCooldowns";

    AttackSelector getAttackSelector();
    AttackInstance getActiveAttack();

    void setActiveAttack(AttackInstance attack);

    default boolean isPreparingAttack() {
        return this.getActiveAttack() != null;
    }

    default void readAttackInfo(CompoundTag pCompound) {
        if (pCompound.contains(NBT_CURRENT_ATTACK_KEY)) {
            CompoundTag tag = pCompound.getCompound(NBT_CURRENT_ATTACK_KEY);
            for (AttackInstance attack : this.getAttackSelector().attacks) {
                String attackId = attack.getId().toString();
                if (tag.contains(attackId)) {
                    this.setActiveAttack(attack);
                }
            }
        }

        if (pCompound.contains(NBT_COOLDOWNS_KEY)) {
            CompoundTag tag = pCompound.getCompound(NBT_COOLDOWNS_KEY);
            for (AttackInstance attack : this.getAttackSelector().attacks) {
                String attackId = attack.getId().toString();
                if (tag.contains(attackId)) {
                    attack.cooldownTimer = tag.getInt(attackId);
                }
            }
        }
    }

    default void writeAttackInfo(CompoundTag pCompound) {
        CompoundTag tag = new CompoundTag();
        for (AttackInstance attack : this.getAttackSelector().attacks) {
            if (attack.isOnCooldown()) {
                tag.putInt(attack.getId().toString(), attack.cooldownTimer);
            }
        }

        if(this.getActiveAttack() != null) pCompound.putString(NBT_CURRENT_ATTACK_KEY, this.getActiveAttack().getId().toString());
        if (!tag.isEmpty()) {
            pCompound.put(NBT_COOLDOWNS_KEY, tag);
        }
    }

    default void tickSystem() {
        tickAttacks();
        tickCooldowns();
    }

    default void tickAttacks() {
        if(this.getActiveAttack() != null) {
            this.getActiveAttack().tick();
        }
    }

    default void tickCooldowns() {
        this.getAttackSelector().attacks.forEach(attackInstance -> {
          if(attackInstance.cooldownTimer > 0) attackInstance.cooldownTimer--;
      });
    }
}
