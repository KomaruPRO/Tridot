package pro.komaru.tridot;

import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Zombie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.komaru.tridot.api.entity.comp.HelloWorldComp;
import pro.komaru.tridot.core.ecs.EntityCompRegistry;
import pro.komaru.tridot.core.struct.Seq;

public class TridotCommon {
    public static final String MOD_ID = "tridot";
    public static final String MOD_NAME = "Tridot";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        EntityCompRegistry.registerFor(Animal.class, entity -> Seq.with(
                new HelloWorldComp()
        ));
    }
}