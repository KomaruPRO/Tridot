package pro.komaru.tridot.client.render.gui.particle;

import net.minecraft.world.item.*;

public record ScreenParticleItemStackKey(boolean isHotbarItem, boolean isRenderedAfterItem, ItemStack itemStack){

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof ScreenParticleItemStackKey key)){
            return false;
        }
        return key.isHotbarItem == isHotbarItem && key.isRenderedAfterItem == isRenderedAfterItem && key.itemStack == itemStack;
    }

}