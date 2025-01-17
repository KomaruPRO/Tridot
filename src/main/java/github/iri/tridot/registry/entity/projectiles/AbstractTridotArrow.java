package github.iri.tridot.registry.entity.projectiles;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

public class AbstractTridotArrow extends AbstractArrow{
    public ItemStack arrowItem = ItemStack.EMPTY;

    public AbstractTridotArrow(EntityType<? extends AbstractArrow> pEntityType, Level pLevel){
        super(pEntityType, pLevel);
    }

    public AbstractTridotArrow(EntityType<? extends AbstractArrow> pEntityType, Level worldIn, LivingEntity thrower, ItemStack thrownStackIn, int baseDamage){
        super(pEntityType, thrower, worldIn);
        arrowItem = new ItemStack(thrownStackIn.getItem());
        this.setBaseDamage(baseDamage);
    }

    public void tick(){
        super.tick();
        if(this.level().isClientSide){
            this.spawnParticlesTrail();
        }
    }

    public void spawnParticlesTrail(){
    }

    @Override
    public ItemStack getPickupItem(){
        return arrowItem;
    }
}