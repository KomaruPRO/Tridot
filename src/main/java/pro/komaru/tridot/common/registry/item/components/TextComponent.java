package pro.komaru.tridot.common.registry.item.components;

import net.minecraft.network.chat.*;
import net.minecraft.world.inventory.tooltip.*;

public record TextComponent(MutableComponent component) implements TooltipComponent{
}