package pro.komaru.tridot.common.registry.item.skins;

import pro.komaru.tridot.common.registry.item.builders.SkinBuilder;
import net.minecraft.network.chat.*;

//todo fluffy (custom)
public class AuthoredItemSkin extends ItemSkin{
    public SkinBuilder skinBuilder;
    public AuthoredItemSkin(SkinBuilder skinBuilder){
        super(skinBuilder.name, skinBuilder.color);
        this.skinBuilder = skinBuilder;
    }

    public Component getContributorComponent(){
        return skinBuilder.component;
    }
}
