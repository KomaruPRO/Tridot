package pro.komaru.tridot.client.gui.screen;

import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;

public class TridotPanorama{
    public String id;
    public Component name;
    public ResourceLocation texture = new ResourceLocation("textures/gui/title/background/panorama");
    public ResourceLocation logo;
    public ItemStack itemStack = new ItemStack(Items.DIRT);
    public TridotMod mod;
    public int sort = 0;

    public TridotPanorama(String id, Component name){
        this.id = id;
        this.name = name;
    }

    public TridotPanorama setTexture(ResourceLocation texture){
        this.texture = texture;
        return this;
    }

    public TridotPanorama setLogo(ResourceLocation logo){
        this.logo = logo;
        return this;
    }

    public TridotPanorama setItem(ItemStack itemStack){
        this.itemStack = itemStack;
        return this;
    }

    public TridotPanorama setMod(TridotMod mod){
        this.mod = mod;
        return this;
    }

    public TridotPanorama setSort(int sort){
        this.sort = sort;
        return this;
    }

    public String getId(){
        return id;
    }

    public Component getName(){
        return name;
    }

    public ResourceLocation getTexture(){
        return texture;
    }

    public ResourceLocation getLogo(){
        return logo;
    }

    public ItemStack getItem(){
        return itemStack;
    }

    public TridotMod getMod(){
        return mod;
    }

    public int getSort(){
        return sort;
    }
}
