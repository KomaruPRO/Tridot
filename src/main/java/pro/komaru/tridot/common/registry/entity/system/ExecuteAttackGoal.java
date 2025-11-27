package pro.komaru.tridot.common.registry.entity.system;

import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.level.*;

import java.util.*;

public class ExecuteAttackGoal extends Goal{
    private final PathfinderMob mob;
    private final AttackSystemMob attackableMob;
    private LivingEntity target;
    private AttackInstance chosenAttack;

    private enum State { IDLE, PREPARING, EXECUTING }
    private State currentState;
    private int timer;
    private int tryCount;

    public ExecuteAttackGoal(PathfinderMob mob) {
        this.mob = mob;
        if (!(mob instanceof AttackSystemMob)) {
            throw new IllegalArgumentException("Mob must implement IAttackableMob!");
        }

        this.attackableMob = (AttackSystemMob) mob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public boolean isInterruptable() {
        return false;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.mob.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }

        if (this.chosenAttack == null) {
            this.chosenAttack = this.attackableMob.getAttackSelector().selectAttack(this.mob, target);
        }

        return this.chosenAttack != null;
    }

    @Override
    public void start() {
        this.target = this.mob.getTarget();
        this.attackableMob.setActiveAttack(this.chosenAttack);
        this.currentState = State.PREPARING;
        this.timer = this.chosenAttack.attackDelay;
        this.chosenAttack.start(attackableMob);

        Level level = this.mob.level();
        if(level != null){
            level.playSound(null, this.mob.blockPosition(), this.chosenAttack.getPrepareSound(), SoundSource.HOSTILE, 1.0F, 1.0F);
        }
    }

    @Override
    public void stop() {
        if (this.chosenAttack != null) {
             this.chosenAttack.setAttackOnCooldown();
        }

        this.chosenAttack = null;
        this.attackableMob.setActiveAttack(null);
        this.target = null;
        this.currentState = State.IDLE;
        this.timer = 0;
        this.tryCount = 0;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.target == null || !this.target.isAlive() || this.chosenAttack == null) {
            stop();
            return;
        }

        this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        this.timer--;
        switch (this.currentState) {
            case PREPARING:
                if (!this.chosenAttack.canUse(this.target)) {
                    stop();
                    return;
                }

                if (this.timer <= 0) {
                    this.currentState = State.EXECUTING;
                    if(this.chosenAttack.canPerformAttack(this.target)){
                        this.timer = this.chosenAttack.attackDuration;
                        this.chosenAttack.performAttack();
                        Level level = this.mob.level();
                        if(level != null){
                            level.playSound(null, this.mob.blockPosition(), this.chosenAttack.getAttackSound(), SoundSource.HOSTILE, 1.0F, 1.0F);
                        }
                    } else {
                        this.tryCount++;
                    }

                    if(tryCount >= 10){
                        stop();
                    }
                }

                break;
            case EXECUTING:
                if (this.timer <= 0 || this.chosenAttack.isFinished()) {
                    stop();
                }

                break;
        }
    }
}