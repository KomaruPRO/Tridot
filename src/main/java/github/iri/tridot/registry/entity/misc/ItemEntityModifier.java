package github.iri.tridot.registry.entity.misc;

import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

public class ItemEntityModifier{

    public boolean isItem(Level level, ItemEntity entity, ItemStack stack){
        return false;
    }

    public void tick(Level level, ItemEntity entity, ItemStack stack){

    }

    public boolean rejectHurt(Level level, ItemEntity entity, ItemStack stack, DamageSource source, float amount){
        return false;
    }
}
