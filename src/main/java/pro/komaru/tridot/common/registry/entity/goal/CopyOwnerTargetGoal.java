package pro.komaru.tridot.common.registry.entity.goal;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.ai.targeting.*;

public class CopyOwnerTargetGoal extends TargetGoal{
    private final PathfinderMob minion;
    private final TargetingConditions copyOwnerTargeting = TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting();

    public CopyOwnerTargetGoal(PathfinderMob pMob){
        super(pMob, false);
        this.minion = pMob;
    }

    private LivingEntity getOwnerTarget(OwnableEntity entity){
        LivingEntity lastHurt = entity.getOwner().getLastHurtByMob();
        if(lastHurt != null){
            return lastHurt;
        }

        return entity.getOwner().getLastHurtMob();
    }

    public boolean canUse(){
        if(!(minion instanceof OwnableEntity ownableEntity)) return false;
        var owner = ownableEntity.getOwner();
        return owner != null && getOwnerTarget(ownableEntity) != null && owner.canAttack(getOwnerTarget(ownableEntity)) && this.canAttack(getOwnerTarget(ownableEntity), this.copyOwnerTargeting);
    }

    public void start(){
        super.start();
        if(!(minion instanceof OwnableEntity ownableEntity)) return;
        minion.setTarget(getOwnerTarget(ownableEntity));
    }
}