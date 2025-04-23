package pro.komaru.tridot.common.registry.item.skins;

import net.minecraft.client.model.*;
import net.minecraft.client.player.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.*;
import pro.komaru.tridot.Tridot;
import pro.komaru.tridot.client.gfx.text.DotStyle;
import pro.komaru.tridot.client.model.TridotModels;
import pro.komaru.tridot.client.model.armor.ArmorModel;
import pro.komaru.tridot.util.Col;
import pro.komaru.tridot.util.struct.Structs;
import pro.komaru.tridot.util.struct.data.Seq;

public class ItemSkin{

    public ResourceLocation id;
    public Col color;
    public Seq<ItemSkinEntry> entries = Seq.with();

    public static Col loreCol = new Col(249/255f, 210/255f, 129/255f);

    public ItemSkin(ResourceLocation id, Col color) {
        this.id = id;
        this.color = color;
    }
    public ItemSkin(ResourceLocation id) {
        this(id,Col.white);
    }
    public ItemSkin(String id, Col color) {
        this(new ResourceLocation(id),color);
    }
    public ItemSkin(String id) {
        this(id,Col.white);
    }

    public void setupSkinEntries(){

    }


    public boolean appliesOn(ItemStack stack) {
        return entries.contains(e -> e.appliesOn(stack));
    }
    public ItemStack apply(ItemStack stack) {
        stack.getOrCreateTag().putString("skin",id.toString());
        return stack;
    }

    public static ItemSkin itemSkin(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        return ItemSkinHandler.get(nbt.getString("skin"));
    }

    public Seq<ItemSkinEntry> skinEntries(){
        return entries;
    }
    public ResourceLocation id() {
        return id;
    }
    public Col color() {
        return color;
    }
    public String translatedName(){
        return Component.translatable("item_skin."+id.toLanguageKey()).getString();
    }
    public String translatedLoreName(){
        return Component.translatable("item_skin."+id.toLanguageKey()+".lore").getString();
    }

    public Component skinName(){
        return Component.translatable(translatedName())
                .setStyle(DotStyle.of().color(color()));
    }
    public Component skinComponent(){
        return Component.translatable("lore.tridot.skin")
                .setStyle(DotStyle.of().color(loreCol)).append(" ").append(skinName());
    }

    @OnlyIn(Dist.CLIENT)
    public ArmorModel getArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel _default){
        return Structs.safeGet(
                entries.find(e -> e.appliesOn(stack)),
                e -> e.armorModel(entity,stack,slot,_default),
                () -> TridotModels.EMPTY_ARMOR);
    }
    @OnlyIn(Dist.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type){
        return Structs.safeGet(
                entries.find(e -> e.appliesOn(stack)),
                e -> e.armorTexture(entity,stack,slot,type),
                () -> Tridot.ID + ":textures/models/armor/skin/empty.png");
    }
    @OnlyIn(Dist.CLIENT)
    public String getItemModelName(ItemStack stack){
        return Structs.safeGet(
                entries.find(e -> e.appliesOn(stack)),
                e -> e.itemModel(stack));
    }
    @OnlyIn(Dist.CLIENT)
    public static boolean defaultModel(Entity entity){
        if(entity instanceof AbstractClientPlayer player){
            return player.getModelName().equals("default");
        }
        return true;
    }
}
