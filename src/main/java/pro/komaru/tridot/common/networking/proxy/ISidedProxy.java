package pro.komaru.tridot.common.networking.proxy;

import net.minecraft.sounds.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;

import java.util.*;

public interface ISidedProxy{
    Player getPlayer();

    Level getLevel();
}