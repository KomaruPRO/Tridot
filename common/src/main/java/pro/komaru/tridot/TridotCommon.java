package pro.komaru.tridot;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.komaru.tridot.api.entity.ai.TestSensor;
import pro.komaru.tridot.api.entity.ai.TestTask;
import pro.komaru.tridot.api.entity.comp.AIComp;
import pro.komaru.tridot.core.entity.ai.TaskSelector;
import pro.komaru.tridot.core.entity.ecs.EntityCompRegistry;
import pro.komaru.tridot.core.struct.Seq;

public class TridotCommon {
    public static final String MOD_ID = "tridot";
    public static final String MOD_NAME = "Tridot";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        EntityCompRegistry.registerFor(IronGolem.class, entity -> Seq.with(
            new AIComp() {{
                setTaskSelector(TaskSelector.STATE_AVAILABLE_HIGHEST_PRIORITY);

                addSensors(
                        new TestSensor()
                );
                addTasks(
                        new TestTask()
                );
            }}
        ));
    }
}