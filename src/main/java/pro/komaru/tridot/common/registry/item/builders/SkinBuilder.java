package pro.komaru.tridot.common.registry.item.builders;

import pro.komaru.tridot.common.registry.item.skins.AuthoredItemSkin;
import pro.komaru.tridot.common.registry.item.skins.ItemSkin;
import pro.komaru.tridot.common.registry.item.skins.ItemSkinEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.*;
import pro.komaru.tridot.util.Col;

import java.util.List;
import java.util.*;
import java.util.function.*;

public class SkinBuilder{
    public String name;
    public List<ItemSkinEntry> skinEntries = new ArrayList<>();
    public Col color;
    public MutableComponent component;
    public SkinBuilder(String id, String name){
        this.name = id + ":" + name;
    }

    public SkinBuilder color(Col color){
        this.color = color;
        return this;
    }

    public SkinBuilder add(ItemSkinEntry entry){
        this.skinEntries.add(entry);
        return this;
    }

    public SkinBuilder component(MutableComponent component){
        this.component = component;
        return this;
    }

    public SkinBuilder style(UnaryOperator<Style> pModifyFunc) {
        component.withStyle(pModifyFunc);
        return this;
    }
    public SkinBuilder style(Style pStyle) {
        component.setStyle(pStyle);
        return this;
    }

    public SkinBuilder contributor(String name){
        this.component = Component.literal(" ༶" + name + "༶");
        return this;
    }

    public ItemSkin build() {
        ItemSkin skin = component != null ? new AuthoredItemSkin(this) : new ItemSkin(name, color);
        for(ItemSkinEntry entry : skinEntries)
            skin.entries.add(entry);
        return skin;
    }
}
