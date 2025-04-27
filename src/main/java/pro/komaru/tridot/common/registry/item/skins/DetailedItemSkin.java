package pro.komaru.tridot.common.registry.item.skins;

import net.minecraft.network.chat.*;

import java.util.*;

public class DetailedItemSkin extends ItemSkin{
    public SkinBuilder skinBuilder;
    public DetailedItemSkin(SkinBuilder skinBuilder){
        super(skinBuilder.name, skinBuilder.color);
        this.skinBuilder = skinBuilder;
    }

    public Component getHoverName(){
        return skinBuilder.hoverName;
    }

    public List<MutableComponent> getComponents(){
        return skinBuilder.component;
    }
}
