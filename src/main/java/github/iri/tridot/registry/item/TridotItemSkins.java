package github.iri.tridot.registry.item;

import github.iri.tridot.*;
import github.iri.tridot.client.model.item.*;
import github.iri.tridot.client.render.item.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;

import java.util.*;

public class TridotItemSkins{

    @Mod.EventBusSubscriber(modid = TridotLib.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientRegistryEvents{
        @SubscribeEvent
        public static void modelRegistrySkins(ModelEvent.RegisterAdditional event){
            for(String skin : ItemSkinModels.getSkins()){
                event.register(ItemSkinModels.getModelLocationSkin(skin));
            }
        }

        @SubscribeEvent
        public static void modelBakeSkins(ModelEvent.ModifyBakingResult event){
            Map<ResourceLocation, BakedModel> map = event.getModels();

            for(String skin : ItemSkinModels.getSkins()){
                BakedModel model = map.get(ItemSkinModels.getModelLocationSkin(skin));
                ItemSkinModels.addModelSkins(skin, model);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void addSkinModel(Map<ResourceLocation, BakedModel> map, ResourceLocation id){
        BakedModel model = map.get(new ModelResourceLocation(id, "inventory"));
        CustomModel newModel = new CustomModel(model, new ItemSkinItemOverrides());
        map.replace(new ModelResourceLocation(id, "inventory"), newModel);
    }

    @OnlyIn(Dist.CLIENT)
    public static void addLargeModel(Map<ResourceLocation, BakedModel> map, String modId, String skin){
        LargeItemRenderer.bakeModel(map, modId, "skin/" + skin);
        ItemSkinModels.addModelSkins(modId + ":" + skin, map.get(ItemSkinModels.getModelLocationSkin(modId + ":" + skin)));
    }
}
