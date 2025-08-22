package pro.komaru.tridot.common.registry.item.skins;

import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import pro.komaru.tridot.util.Col;

import java.util.*;

public class SkinBuilder{
    public String id;
    public List<SkinEntry> skinEntries = new ArrayList<>();
    public Col color;
    public List<MutableComponent> component;
    public Component hoverName;

    public SkinBuilder(String namespace, String id){
        this.id = namespace + ":" + id;
    }

    public SkinBuilder(ResourceLocation loc){
        this.id = loc.toString();
    }

    /**
     * Lore color, used in tooltips
     */
    public SkinBuilder color(Col color){
        this.color = color;
        return this;
    }

    public SkinBuilder add(SkinEntry entry){
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
        ItemSkin skin = new ItemSkin(this);
        SkinRegistryManager.add(skin);
        for(SkinEntry entry : skinEntries) skin.entries.add(entry);
        return skin;
    }
}
