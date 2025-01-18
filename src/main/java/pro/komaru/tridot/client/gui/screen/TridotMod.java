package pro.komaru.tridot.client.gui.screen;

import net.minecraft.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;

import java.awt.*;
import java.util.List;
import java.util.*;

public class TridotMod{
    public String id;
    public String name;
    public String version;
    public int edition = 0;
    public String dev = "Unknown";
    public ItemStack itemStack = new ItemStack(Items.DIRT);
    public Color nameColor = Color.WHITE;
    public Color versionColor = Color.GRAY;
    public Component description = Component.empty();
    public List<Link> links = new ArrayList<>();

    public TridotMod(String id, String name, String version){
        this.id = id;
        this.name = name;
        this.version = version;
    }

    public TridotMod setEdition(int edition){
        this.edition = edition;
        return this;
    }

    public TridotMod setDev(String dev){
        this.dev = dev;
        return this;
    }

    public TridotMod setItem(ItemStack itemStack){
        this.itemStack = itemStack;
        return this;
    }

    public TridotMod setNameColor(Color nameColor){
        this.nameColor = nameColor;
        return this;
    }

    public TridotMod setVersionColor(Color versionColor){
        this.versionColor = versionColor;
        return this;
    }

    public TridotMod setDescription(Component description){
        this.description = description;
        return this;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getVersion(){
        return version;
    }

    public int getEdition(){
        return edition;
    }

    public String getDev(){
        return dev;
    }

    public ItemStack getItem(){
        return itemStack;
    }

    public Color getNameColor(){
        return nameColor;
    }

    public Color getVersionColor(){
        return versionColor;
    }

    public Component getDescription(){
        return description;
    }

    public static class Link{
        public Component component;
        public String link;

        public Link(Component component, String link){
            this.component = component;
            this.link = link;
        }

        public Component getComponent(){
            return component;
        }

        public String getLink(){
            return link;
        }
    }

    public List<Link> getLinks(){
        return links;
    }

    public TridotMod addLink(Component component, String string){
        links.add(new Link(component, string));
        return this;
    }

    public TridotMod addGithubLink(String string){
        addLink(Component.literal("Github").withStyle(ChatFormatting.DARK_GRAY), string);
        return this;
    }

    public TridotMod addCurseForgeLink(String string){
        addLink(Component.literal("CurseForge").withStyle(ChatFormatting.GOLD), string);
        return this;
    }

    public TridotMod addModrinthLink(String string){
        addLink(Component.literal("Modrinth").withStyle(ChatFormatting.GREEN), string);
        return this;
    }

    public TridotMod addDiscordLink(String string){
        addLink(Component.literal("Discord").withStyle(ChatFormatting.BLUE), string);
        return this;
    }
}
