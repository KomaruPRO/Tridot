package pro.komaru.tridot.api.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import pro.komaru.tridot.core.entity.ai.Task;

public class TestTask extends Task {
    @Override
    public void start() {
        System.out.println("TestTask started with runtime: " + runTime);
    }

    @Override
    public void update() {
        if(runTime > 40) markAsFinished();
        else if (runTime > 20 && entity instanceof Mob mob) {
            mob.getJumpControl().jump();
            mob.setZza(0.5f);
            mob.setXxa(runTime/20f);
        }
    }

    @Override
    public void stop(boolean interrupted) {
        System.out.println("TestTask "+(interrupted ? "interrupted" : "finished"));
    }

    @Override
    public float priorityScore() {
        return 3f;
    }
}
