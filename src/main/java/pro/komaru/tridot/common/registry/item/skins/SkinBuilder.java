package pro.komaru.tridot.common.registry.item.skins;

import net.minecraft.network.chat.*;
import pro.komaru.tridot.util.Col;

import java.util.*;

public class SkinBuilder{
    public String name;
    public List<ItemSkinEntry> skinEntries = new ArrayList<>();
    public Col color;
    public List<MutableComponent> component;
    public Component hoverName;
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
        this.component.add(component);
        return this;
    }

    public SkinBuilder component(MutableComponent... components){
        Collections.addAll(this.component, components);
        return this;
    }

    public SkinBuilder contributor(String name){
        this.hoverName = Component.literal(" ༶" + name + "༶");
        return this;
    }

    public SkinBuilder contributor(String name, Style style){
        this.hoverName = Component.literal(" ༶" + name + "༶").setStyle(style);
        return this;
    }

    public ItemSkin build() {
        ItemSkin skin = component != null ? new DetailedItemSkin(this) : new ItemSkin(name, color);
        for(ItemSkinEntry entry : skinEntries)
            skin.entries.add(entry);
        return skin;
    }
}
