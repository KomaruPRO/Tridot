package pro.komaru.tridot.oclient.model.item;

import net.minecraft.client.multiplayer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;

import javax.annotation.*;

//todo fluffy
public class CustomItemOverrides extends ItemOverrides{

    @Override
    public BakedModel resolve(BakedModel originalModel, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed){
        return originalModel;
    }
}