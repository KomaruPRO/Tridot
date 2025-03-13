package pro.komaru.tridot.oclient.model.armor;

import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

public class EmptyArmorModel extends ArmorModel{
    public EmptyArmorModel(ModelPart root){
        super(root);
    }

    public static LayerDefinition createBodyLayer(){
        MeshDefinition mesh = HumanoidModel.createMesh(new CubeDeformation(0), 0);
        PartDefinition root = createHumanoidAlias(mesh);

        PartDefinition body = root.getChild("body");

        return LayerDefinition.create(mesh, 16, 16);
    }
}
