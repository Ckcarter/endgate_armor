package com.che.endportalarmor.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class OpenFrameHelmetModel<T extends net.minecraft.world.entity.LivingEntity> extends HumanoidModel<T> {
    public OpenFrameHelmetModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

        PartDefinition head = root.getChild("head");

        // Remove the default full-cube look and replace it with an open-frame design.
        head.addOrReplaceChild("crown_front",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4.5F, -8.5F, -4.5F, 9.0F, 2.0F, 1.0F, CubeDeformation.NONE),
                PartPose.ZERO);

        head.addOrReplaceChild("crown_back",
                CubeListBuilder.create().texOffs(0, 3)
                        .addBox(-4.5F, -8.5F, 3.5F, 9.0F, 2.0F, 1.0F, CubeDeformation.NONE),
                PartPose.ZERO);

        head.addOrReplaceChild("crown_left",
                CubeListBuilder.create().texOffs(0, 6)
                        .addBox(-4.5F, -8.5F, -3.5F, 1.0F, 2.0F, 7.0F, CubeDeformation.NONE),
                PartPose.ZERO);

        head.addOrReplaceChild("crown_right",
                CubeListBuilder.create().texOffs(16, 6)
                        .addBox(3.5F, -8.5F, -3.5F, 1.0F, 2.0F, 7.0F, CubeDeformation.NONE),
                PartPose.ZERO);

        head.addOrReplaceChild("visor",
                CubeListBuilder.create().texOffs(20, 0)
                        .addBox(-4.0F, -6.75F, -4.75F, 8.0F, 2.0F, 1.0F, CubeDeformation.NONE),
                PartPose.ZERO);

        head.addOrReplaceChild("crest_top",
                CubeListBuilder.create().texOffs(20, 4)
                        .addBox(-1.0F, -10.5F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 64, 32);
    }
}
