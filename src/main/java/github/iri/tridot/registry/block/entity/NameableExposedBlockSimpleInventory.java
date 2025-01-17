package github.iri.tridot.registry.block.entity;

import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

import javax.annotation.*;

public abstract class NameableExposedBlockSimpleInventory extends ExposedBlockSimpleInventory implements MenuProvider, Nameable{

    @Nullable
    public Component name;

    public NameableExposedBlockSimpleInventory(BlockEntityType<?> type, BlockPos pos, BlockState blockState){
        super(type, pos, blockState);
    }

    public void load(CompoundTag tag){
        super.load(tag);
        if(tag.contains("CustomName", 8)){
            this.name = Component.Serializer.fromJson(tag.getString("CustomName"));
        }
    }

    public void saveAdditional(CompoundTag tag){
        super.saveAdditional(tag);
        if(this.name != null){
            tag.putString("CustomName", Component.Serializer.toJson(this.name));
        }
    }

    @Override
    public Component getName(){
        return name;
    }

    public void setCustomName(Component pName){
        this.name = pName;
    }

    public Component getDefaultName(){
        return Component.empty();
    }

    @Override
    public Component getDisplayName(){
        return this.name != null ? this.name : this.getDefaultName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player){
        return null;
    }
}
