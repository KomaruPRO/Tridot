package pro.komaru.tridot.common.registry.entity.system;

import net.minecraft.world.entity.*;
import pro.komaru.tridot.util.struct.data.*;

import javax.annotation.*;
import java.util.*;

public class AttackSelector {
    public final Seq<AttackInstance> attacks = Seq.with();

    public void addAttack(AttackInstance attack) {
        this.attacks.add(attack);
    }

    /**
     * Selects the best available attack for the current situation.
     * @param mobEntity The mob that needs to choose an attack.
     * @param target The target of the attack.
     * @return The chosen AttackInstance, or null if no attack is suitable.
     */
    @Nullable
    public AttackInstance selectAttack(PathfinderMob mobEntity, LivingEntity target) {
        Seq<AttackInstance> usableAttacks = Seq.with();
        int totalWeight = 0;
        for (AttackInstance attack : this.attacks) {
            if (!attack.isOnCooldown() && attack.canUse(target)) {
                usableAttacks.add(attack);
                totalWeight += attack.preference(target);
            }
        }

        if (usableAttacks.isEmpty()) {
            return null;
        }

        if(totalWeight <= 0) {
            return usableAttacks.get(mobEntity.getRandom().nextInt(usableAttacks.size));
        } else {
            int random = mobEntity.getRandom().nextInt(totalWeight);
            for(AttackInstance attack : usableAttacks){
                int weight = attack.preference(target);
                if(random < weight){
                    return attack;
                }
                random -= weight;
            }
        }

        return null;
    }
}