package pro.komaru.tridot.api.level.event;

import net.minecraft.resources.*;
import net.minecraft.server.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.predicates.*;

import java.util.function.*;

public interface GameplayEvent extends LootItemCondition{

    ResourceLocation getId();

    boolean test(ServerLevel server);

    void onStart(ServerLevel server);

    void onStop(ServerLevel server);

    default void onTick(ServerLevel server) {}
}
