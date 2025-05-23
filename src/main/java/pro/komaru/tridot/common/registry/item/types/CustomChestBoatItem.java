package pro.komaru.tridot.common.registry.item.types;

import pro.komaru.tridot.common.registry.entity.misc.CustomChestBoatEntity;
import net.minecraft.stats.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import net.minecraftforge.registries.*;

import java.util.*;
import java.util.function.*;

public class CustomChestBoatItem extends Item{
    private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);
    private final RegistryObject<EntityType<CustomChestBoatEntity>> boat;

    public CustomChestBoatItem(Properties properties, RegistryObject<EntityType<CustomChestBoatEntity>> boat){
        super(properties);
        this.boat = boat;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand){
        ItemStack itemstack = player.getItemInHand(hand);
        HitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        if(hitResult.getType() == HitResult.Type.MISS){
            return InteractionResultHolder.pass(itemstack);
        }else{
            Vec3 vector3d = player.getViewVector(1.0F);
            List<Entity> list = level.getEntities(player, player.getBoundingBox().expandTowards(vector3d.scale(5.0D)).inflate(1.0D), ENTITY_PREDICATE);
            if(!list.isEmpty()){
                Vec3 vector3d1 = player.getEyePosition(1.0F);

                for(Entity entity : list){
                    AABB AABB = entity.getBoundingBox().inflate(entity.getPickRadius());
                    if(AABB.contains(vector3d1)){
                        return InteractionResultHolder.pass(itemstack);
                    }
                }
            }

            if(hitResult.getType() == HitResult.Type.BLOCK){
                CustomChestBoatEntity boatEntity = boat.get().create(level);
                boatEntity.setPos(hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z);
                boatEntity.setYRot(player.getYRot());
                if(!level.noCollision(boatEntity, boatEntity.getBoundingBox().inflate(-0.1D))){
                    return InteractionResultHolder.fail(itemstack);
                }else{
                    if(!level.isClientSide){
                        level.addFreshEntity(boatEntity);
                        if(!player.getAbilities().instabuild){
                            itemstack.shrink(1);
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
                }
            }else{
                return InteractionResultHolder.pass(itemstack);
            }
        }
    }
}