package pro.komaru.tridot.common.registry.entity.misc;

import net.minecraft.core.*;
import net.minecraft.network.protocol.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.tags.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.vehicle.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraftforge.network.*;
import net.minecraftforge.registries.*;
import org.jetbrains.annotations.*;

public class CustomChestBoatEntity extends ChestBoat{
    private final RegistryObject<Item> boatItem;
    private final boolean isRaft;

    public CustomChestBoatEntity(EntityType<? extends CustomChestBoatEntity> type, Level level, RegistryObject<Item> boatItem, boolean isRaft){
        super(type, level);
        this.boatItem = boatItem;
        this.isRaft = isRaft;
    }

    @Override
    protected void checkFallDamage(double dY, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos){
        this.lastYd = this.getDeltaMovement().y;
        if(!this.isPassenger()){
            if(onGround){
                if(this.fallDistance > 3.0F){
                    if(this.status != Status.ON_LAND){
                        this.fallDistance = 0.0F;
                        return;
                    }

                    this.causeFallDamage(this.fallDistance, 1.0F, level().damageSources().fall());
                    if(!this.level().isClientSide && !this.isRemoved()){
                        this.kill();
                        if(this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)){
                            for(int i = 0; i < 3; ++i){
                                this.spawnAtLocation(this.getVariant().getPlanks());
                            }

                            for(int j = 0; j < 2; ++j){
                                this.spawnAtLocation(Items.STICK);
                            }
                        }
                    }
                }

                this.fallDistance = 0.0F;
            }else if(!this.level().getFluidState(this.blockPosition().below()).is(FluidTags.WATER) && dY < 0.0D){
                this.fallDistance = (float)((double)this.fallDistance - dY);
            }

        }
    }

    @Override
    public double getPassengersRidingOffset(){
        return isRaft ? 0.25D : -0.1D;
    }

    @Override
    @NotNull
    public Item getDropItem(){
        return boatItem.get();
    }

    @Override
    @NotNull
    public Packet<ClientGamePacketListener> getAddEntityPacket(){
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
