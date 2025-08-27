package pro.komaru.tridot.common.registry.item.armor;

import net.minecraft.world.item.ArmorItem.*;
import pro.komaru.tridot.common.registry.item.builders.*;

public interface TridotArmorMat{
    AbstractArmorBuilder<?> builder();
    float getPercentDefenseForType(Type pType);
}
