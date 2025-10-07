package pro.komaru.tridot.common.registry.item.components;

import net.minecraft.network.chat.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.inventory.tooltip.*;

import javax.annotation.*;
import java.util.*;

public record EffectsListComponent(List<MobEffectInstance> list, @Nullable Component component) implements TooltipComponent{
}