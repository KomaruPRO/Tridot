package github.iri.tridot.client.render.entity;

import com.mojang.blaze3d.vertex.*;
import github.iri.tridot.client.playerskin.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;

public class ExtraLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M>{
    public final HumanoidModel defaultModel;

    public ExtraLayer(RenderLayerParent<T, M> renderer){
        super(renderer);
        this.defaultModel = (HumanoidModel)renderer.getModel();
    }

    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch){
        if(livingEntity instanceof Player player){
            PlayerSkin skin = PlayerSkinHandler.getSkin(player);

            if(skin != null){
                skin.extraRender(poseStack, bufferSource, packedLight, player, limbSwing, limbSwingAmount, partialTick, ageInTicks, netHeadYaw, headPitch, defaultModel);
            }
        }
    }
}