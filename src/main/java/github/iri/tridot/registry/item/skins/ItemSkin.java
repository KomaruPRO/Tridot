package github.iri.tridot.registry.item.skins;

import github.iri.tridot.*;
import github.iri.tridot.client.*;
import github.iri.tridot.client.model.armor.*;
import github.iri.tridot.utilities.client.*;
import net.minecraft.client.model.*;
import net.minecraft.client.player.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.*;

import java.awt.*;
import java.util.List;
import java.util.*;

public class ItemSkin{
    public String id;
    public Color color;
    public List<ItemSkinEntry> skinEntries = new ArrayList<>();

    public ItemSkin(String id){
        this.id = id;
        this.color = Color.WHITE;
    }

    public ItemSkin(String id, Color color){
        this.id = id;
        this.color = color;
    }

    public String getId(){
        return id;
    }

    public Color getColor(){
        return color;
    }

    public String getTranslatedName(){
        return getTranslatedName(id);
    }

    public String getTranslatedLoreName(){
        return getTranslatedLoreName(id);
    }

    public static String getTranslatedName(String id){
        int i = id.indexOf(":");
        String modId = id.substring(0, i);
        String monogramId = id.substring(i + 1);
        return "item_skin." + modId + "." + monogramId;
    }

    public static String getTranslatedLoreName(String id){
        return getTranslatedName(id) + ".lore";
    }

    public static Component getSkinName(ItemSkin skin){
        Color color = skin.getColor();

        return Component.translatable(skin.getTranslatedName()).withStyle(Style.EMPTY.withColor(Clr.packColor(255, color.getRed(), color.getGreen(), color.getBlue())));
    }

    public static Component getSkinComponent(ItemSkin skin){
        return Component.translatable("lore.tridot.skin").withStyle(Style.EMPTY.withColor(Clr.packColor(255, 249, 210, 129))).append(" ").append(getSkinName(skin));
    }

    public Component getSkinName(){
        return getSkinName(this);
    }

    public Component getSkinComponent(){
        return getSkinComponent(this);
    }

    public boolean canApplyOnItem(ItemStack itemStack){
        for(ItemSkinEntry skinEntry : getSkinEntries()){
            if(skinEntry.canApplyOnItem(itemStack)) return true;
        }
        return false;
    }

    public ItemStack applyOnItem(ItemStack itemStack){
        CompoundTag nbt = itemStack.getOrCreateTag();
        nbt.putString("skin", this.getId());
        return itemStack;
    }

    public static ItemSkin getSkinFromItem(ItemStack itemStack){
        CompoundTag nbt = itemStack.getOrCreateTag();
        if(nbt.contains("skin")){
            return ItemSkinHandler.getSkin(nbt.getString("skin"));
        }
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public ArmorModel getArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel _default){
        for(ItemSkinEntry skinEntry : getSkinEntries()){
            if(skinEntry.canApplyOnItem(itemStack)) return skinEntry.getArmorModel(entity, itemStack, armorSlot, _default);
        }
        return TridotModels.EMPTY_ARMOR;
    }

    @OnlyIn(Dist.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type){
        for(ItemSkinEntry skinEntry : getSkinEntries()){
            if(skinEntry.canApplyOnItem(stack)) return skinEntry.getArmorTexture(stack, entity, slot, type);
        }
        return TridotLib.ID + ":textures/models/armor/skin/empty.png";
    }

    @OnlyIn(Dist.CLIENT)
    public String getItemModelName(ItemStack stack){
        for(ItemSkinEntry skinEntry : getSkinEntries()){
            if(skinEntry.canApplyOnItem(stack)) return skinEntry.getItemModelName(stack);
        }
        return null;
    }

    public List<ItemSkinEntry> getSkinEntries(){
        return skinEntries;
    }

    public void addSkinEntry(ItemSkinEntry skinEntry){
        skinEntries.add(skinEntry);
    }

    public void setupSkinEntries(){

    }

    @OnlyIn(Dist.CLIENT)
    public static boolean isDefaultModel(Entity entity){
        if(entity instanceof AbstractClientPlayer player){
            return player.getModelName().equals("default");
        }

        return true;
    }
}
