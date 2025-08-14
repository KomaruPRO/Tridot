package pro.komaru.tridot.api.entity.comp;

import pro.komaru.tridot.core.anno.CompAnnotations.*;
import pro.komaru.tridot.core.entity.ai.AITaskSelectionContext;
import pro.komaru.tridot.core.entity.ai.Sensor;
import pro.komaru.tridot.core.entity.ai.Task;
import pro.komaru.tridot.core.entity.ai.TaskSelector;
import pro.komaru.tridot.core.entity.ecs.EntityComp;
import pro.komaru.tridot.core.struct.CallInfo;
import pro.komaru.tridot.core.struct.Seq;
import pro.komaru.tridot.core.struct.enums.GameSide;

import java.util.HashMap;
import java.util.Map;

@ApplyOn(GameSide.SERVER)
public class AIComp extends EntityComp {
    private TaskSelector taskSelector = TaskSelector.STATE_AVAILABLE_HIGHEST_PRIORITY;
    private Seq<Sensor> sensors = Seq.empty();
    private Seq<Task> tasks = Seq.empty();

    private Task currentTask = null;

    private int ticks = 0;

    private final Map<Integer,Seq<Sensor>> sensorIntervalsCache = new HashMap<>();

    public AIComp() {}

    /**
     * Adds a sensor to the AI component.
     * @param sensor the sensor to add
     */
    public void addSensor(Sensor sensor) {
        sensors = sensors.add(sensor);
    }
    /**
     * Adds multiple sensors to the AI component.
     * @param sensor the sensors to add
     */
    public void addSensors(Sensor ...sensor) {
        sensors = sensors.addAll(sensor);
    }
    /**
     * Adds a task to the AI component.
     * @param task the task to add
     */
    public void addTask(Task task) {
        tasks = tasks.add(task);
    }
    /**
     * Adds multiple tasks to the AI component.
     * @param task the tasks to add
     */
    public void addTasks(Task ...task) {
        tasks = tasks.addAll(task);
    }
    /**
     * Sets the task selector for this AI component.
     * @param taskSelector the task selector to set
     */
    public void setTaskSelector(TaskSelector taskSelector) {
        this.taskSelector = taskSelector;
    }

    @Override
    public void onAdded() {
        recacheSensorIntervals();
    }

    @Override
    public void onTick(CallInfo callInfo) {
        stopCurrentTaskIfFinished();
        updateSensors();
        selectTask();
        updateTask();

        ticks++;
    }

    @Override
    public void onRemoved() {
        stopCurrentTask();
    }

    public void recacheSensorIntervals() {
        sensorIntervalsCache.clear();
        for(Sensor sensor : sensors) {
            Seq<Sensor> intervalSensors = sensorIntervalsCache.computeIfAbsent(
                    sensor.updateInterval, k -> Seq.empty());
            intervalSensors.add(sensor);
        }
    }

    public void selectTask() {
        AITaskSelectionContext taskSelCtx = new AITaskSelectionContext();
        taskSelCtx.currentTask = currentTask;
        taskSelCtx.ticks = ticks;
        taskSelCtx.tasks = tasks;

        Task newTask = taskSelector.selectTask(taskSelCtx);
        if(newTask != null && newTask != currentTask) setCurrentTask(newTask);
    }

    public void updateSensors() {
        for (Integer interval : sensorIntervalsCache.keySet()) {
            if (ticks % interval != 0) continue;

            Seq<Sensor> sensors = sensorIntervalsCache.get(interval);
            for (Sensor sensor : sensors)
                sensor.update();
        }
    }

    public void updateTask() {
        if(currentTask == null) return;
        currentTask.update();
        currentTask.runTime++;
    }

    public void setCurrentTask(Task task) {
        if(!interruptCurrentTask()) return;
        currentTask = task;
        if(currentTask != null) {
            configureTask(currentTask);
            currentTask.start();
        }
    }

    public boolean interruptCurrentTask() {
        if(currentTask == null) return true;
        if(currentTask.isFinished()) return true;
        if(!currentTask.canInterrupt()) return false;
        currentTask.stop(true);
        configureTask(currentTask);
        currentTask = null;
        return true;
    }

    public void stopCurrentTaskIfFinished() {
        if(currentTask == null) return;
        if(!currentTask.isFinished()) return;
        currentTask.stop(false);
        configureTask(currentTask);
        currentTask = null;
    }

    public void stopCurrentTask() {
        if(currentTask == null) return;
        currentTask.markAsFinished();
        stopCurrentTaskIfFinished();
    }

    public void configureTask(Task task) {
        task.entity = getEntity();
        task.runTime = 0;
        task.finished = false;
    }
}
