package pro.komaru.tridot.core.entity.ai;

import pro.komaru.tridot.core.math.Rand;
import pro.komaru.tridot.core.struct.Pair;
import pro.komaru.tridot.core.struct.Seq;

public class AITaskSelectionContext {
    public Task currentTask;
    public Seq<Task> tasks;

    public int ticks;

    public Seq<Pair<Task,Float>> getGaussianPriorities(Seq<Task> tasks) {
        float avg = tasks.map(Task::priorityScore).sumf(f -> f) / tasks.size;
        float stdDev = tasks.map(t -> (float) Math.pow(t.priorityScore() - avg, 2)).sumf(f -> f) / tasks.size;
        return tasks.map(task -> {
            float priority = task.priorityScore();
            float delta = task.priorityRandomDelta();
            float noise = (Rand.of(0).nextFloat() - 0.5f) * 2f * delta * stdDev;
            return new Pair<>(task, priority + noise);
        });
    }
}
