package github.iri.tridot.registry.item.skins;

import github.iri.tridot.registry.item.builders.*;
import net.minecraft.network.chat.*;

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
