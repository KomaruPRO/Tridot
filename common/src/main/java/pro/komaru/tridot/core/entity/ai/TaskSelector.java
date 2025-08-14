package pro.komaru.tridot.core.entity.ai;

import pro.komaru.tridot.core.struct.Pair;
import pro.komaru.tridot.core.struct.Seq;

public interface TaskSelector {
    TaskSelector STATE_AVAILABLE_HIGHEST_PRIORITY = context -> {
        int interruptCheckInterval = 20;

        if(context.currentTask != null && !context.currentTask.canInterrupt()) return null;
        if(context.currentTask != null && context.ticks % interruptCheckInterval != 0) return null;

        Seq<Task> available = context.tasks
                .select(Task::canUse);

        if(available.isEmpty()) return null;

        if(context.currentTask != null)
            return available
                    .max(Task::priorityScore);
        Seq<Pair<Task, Float>> priorities = context.getGaussianPriorities(available);
        return priorities
                .max(Pair::get2)
                .get1();
    };

    Task selectTask(AITaskSelectionContext context);
}
