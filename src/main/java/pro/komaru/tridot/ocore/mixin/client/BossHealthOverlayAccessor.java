package pro.komaru.tridot.ocore.mixin.client;

import net.minecraft.client.gui.components.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

import java.util.*;

@Mixin(BossHealthOverlay.class)
public interface BossHealthOverlayAccessor{

    @Accessor("events")
    Map<UUID, LerpingBossEvent> getEvents();
}
