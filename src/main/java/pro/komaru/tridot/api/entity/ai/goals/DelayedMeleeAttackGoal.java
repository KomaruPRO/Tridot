package pro.komaru.tridot.api.entity.ai.goals;

import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.pathfinder.*;

import java.util.*;

public class DelayedMeleeAttackGoal extends Goal{
    public final PathfinderMob mob;
    public final double speedModifier;
    public final boolean followingTargetEvenIfNotSeen;
    public Path path;
    public double pathedTargetX;
    public double pathedTargetY;
    public double pathedTargetZ;
    public int ticksUntilNextPathRecalculation;
    public int ticksUntilNextAttack;
    public int attackInterval = 20;
    public long lastCanUseCheck;
    public int failedPathFindingPenalty = 0;
    public final boolean canPenalize = false;

    public DelayedMeleeAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen){
        this.mob = pMob;
        this.speedModifier = pSpeedModifier;
        this.followingTargetEvenIfNotSeen = pFollowingTargetEvenIfNotSeen;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public DelayedMeleeAttackGoal(PathfinderMob pMob, double pSpeedModifier, int pDelay, boolean pFollowingTargetEvenIfNotSeen){
        this.mob = pMob;
        this.speedModifier = pSpeedModifier;
        this.attackInterval = pDelay;
        this.followingTargetEvenIfNotSeen = pFollowingTargetEvenIfNotSeen;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse(){
        long i = this.mob.level().getGameTime();
        if(i - this.lastCanUseCheck < getAttackInterval()){
            return false;
        }else{
            this.lastCanUseCheck = i;
            LivingEntity livingentity = this.mob.getTarget();
            if(livingentity == null){
                return false;
            }else if(!livingentity.isAlive()){
                return false;
            }else{
                if(canPenalize){
                    if(--this.ticksUntilNextPathRecalculation <= 0){
                        this.path = this.mob.getNavigation().createPath(livingentity, 0);
                        this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                        return this.path != null;
                    }else{
                        return true;
                    }
                }
                this.path = this.mob.getNavigation().createPath(livingentity, 0);
                if(this.path != null){
                    return true;
                }else{
                    return this.getAttackReachSqr(livingentity) >= this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                }
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean canContinueToUse(){
        LivingEntity livingentity = this.mob.getTarget();
        if(livingentity == null){
            return false;
        }else if(!livingentity.isAlive()){
            return false;
        }else if(!this.followingTargetEvenIfNotSeen){
            return !this.mob.getNavigation().isDone();
        }else if(!this.mob.isWithinRestriction(livingentity.blockPosition())){
            return false;
        }else{
            return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player)livingentity).isCreative();
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start(){
        this.mob.getNavigation().moveTo(this.path, this.speedModifier);
        this.mob.setAggressive(true);
        this.ticksUntilNextPathRecalculation = 0;
        this.ticksUntilNextAttack = 0;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop(){
        LivingEntity livingentity = this.mob.getTarget();
        if(!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)){
            this.mob.setTarget(null);
        }

        this.mob.setAggressive(false);
        this.mob.getNavigation().stop();
    }

    public boolean requiresUpdateEveryTick(){
        return true;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick(){
        LivingEntity livingentity = this.mob.getTarget();
        if(livingentity != null){
            this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack(livingentity);
            this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
            if((this.followingTargetEvenIfNotSeen || this.mob.getSensing().hasLineOfSight(livingentity)) && this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == 0.0D && this.pathedTargetY == 0.0D && this.pathedTargetZ == 0.0D || livingentity.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0D || this.mob.getRandom().nextFloat() < 0.05F)){
                this.pathedTargetX = livingentity.getX();
                this.pathedTargetY = livingentity.getY();
                this.pathedTargetZ = livingentity.getZ();
                this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                if(this.canPenalize){
                    this.ticksUntilNextPathRecalculation += failedPathFindingPenalty;
                    if(this.mob.getNavigation().getPath() != null){
                        net.minecraft.world.level.pathfinder.Node finalPathPoint = this.mob.getNavigation().getPath().getEndNode();
                        if(finalPathPoint != null && livingentity.distanceToSqr(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
                            failedPathFindingPenalty = 0;
                        else
                            failedPathFindingPenalty += 10;
                    }else{
                        failedPathFindingPenalty += 10;
                    }
                }
                if(d0 > 1024.0D){
                    this.ticksUntilNextPathRecalculation += 10;
                }else if(d0 > 256.0D){
                    this.ticksUntilNextPathRecalculation += 5;
                }

                if(!this.mob.getNavigation().moveTo(livingentity, this.speedModifier)){
                    this.ticksUntilNextPathRecalculation += 15;
                }

                this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
            }

            this.ticksUntilNextAttack = Math.max(getTicksUntilNextAttack() - 1, 0);
            this.checkAndPerformAttack(livingentity, d0);
        }
    }

    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr){
        double d0 = this.getAttackReachSqr(pEnemy);
        if(pDistToEnemySqr <= d0 && isTimeToAttack()){
            this.resetAttackCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(pEnemy);
        }
    }

    protected void resetAttackCooldown(){
        this.ticksUntilNextAttack = this.adjustedTickDelay(attackInterval);
    }

    protected boolean isTimeToAttack(){
        return this.ticksUntilNextAttack <= 0;
    }

    protected int getTicksUntilNextAttack(){
        return this.ticksUntilNextAttack;
    }

    protected int getAttackInterval(){
        return this.adjustedTickDelay(attackInterval);
    }

    protected double getAttackReachSqr(LivingEntity pAttackTarget){
        return this.mob.getBbWidth() * 3.0F * this.mob.getBbWidth() * 3.0F + pAttackTarget.getBbWidth();
    }
}
