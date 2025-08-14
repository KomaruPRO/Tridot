package pro.komaru.tridot.core.entity.ai;

import net.minecraft.world.entity.Entity;

public abstract class Task {
    public boolean finished = false;
    public int runTime = 0;
    public Entity entity;
    /**
     * Returns the priority score of this task.
     * @return the priority score, where higher values indicate higher priority.
     */
    public float priorityScore() {
        return 0f;
    }
    /**
     * Returns the delta that will randomly lower the priority score of this task.
     * This allows for some randomness in task selection, making it more dynamic.
     * @return the random delta to apply to the priority score. Important tasks should return a small value,
     * while less important tasks can return a larger value.
     */
    public float priorityRandomDelta() {
        return 0.1f;
    }
    /**
     * Returns whether this task can be used.
     * @return true if the task can be used, false otherwise.
     */
    public boolean canUse() {return true;}
    /**
     * Returns whether this task can be interrupted by other tasks.
     * @return true if the task can be interrupted, false otherwise.
     */
    public boolean canInterrupt() {
        return true;
    }

    public abstract void start();
    public abstract void update();
    public abstract void stop(boolean interrupted);

    public void markAsFinished() {finished = true;}
    public boolean isFinished() {return finished;}
}
