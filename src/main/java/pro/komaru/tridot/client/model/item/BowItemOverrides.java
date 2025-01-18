package pro.komaru.tridot.client.model.item;

import net.minecraft.client.multiplayer.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;

import javax.annotation.*;
import java.util.*;

public class BowItemOverrides extends CustomItemOverrides{
    public ArrayList<BakedModel> models = new ArrayList<>();

    @Override
    public BakedModel resolve(BakedModel originalModel, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed){
        if(getPulling(stack, level, entity, seed) > 0){
            float pull = getPull(stack, level, entity, seed);
            BakedModel model = models.get(0);
            if(pull >= 0.65f){
                model = models.get(1);
            }
            if(pull >= 0.9f){
                model = models.get(2);
            }
            return model;
        }
        return originalModel;
    }

    public float getPull(ItemStack stack, ClientLevel level, LivingEntity entity, int seed){
        if(entity == null){
            return 0.0F;
        }else{
            return entity.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
        }
    }

    public float getPulling(ItemStack stack, ClientLevel level, LivingEntity entity, int seed){
        return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
    }
}