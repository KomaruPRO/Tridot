package pro.komaru.tridot.registry.entity.ai.goals;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.ai.targeting.*;
import pro.komaru.tridot.registry.entity.*;

public class CopyOwnerTargetGoal extends TargetGoal{
    private final TargetingConditions copyOwnerTargeting = TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
    public AbstractMinionEntity mob;
    public CopyOwnerTargetGoal(AbstractMinionEntity pMob){
        super(pMob, false);
        this.mob = pMob;
    }

    private LivingEntity getOwnerTarget(){
        LivingEntity lastHurt = mob.owner.getLastHurtByMob();
        if(lastHurt != null){
            return lastHurt;
        }

        return mob.owner.getLastHurtMob();
    }

    public boolean canUse(){
        return mob.owner != null && getOwnerTarget() != null && this.canAttack(getOwnerTarget(), this.copyOwnerTargeting);
    }

    public void start(){
        mob.setTarget(getOwnerTarget());
        super.start();
    }
}
