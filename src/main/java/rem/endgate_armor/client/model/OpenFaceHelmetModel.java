package rem.endgate_armor.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * Open-face helmet model that keeps the player's face visible,
 * but still reads like a vanilla-style Minecraft helmet.
 *
 * It uses a thicker outer frame with:
 * - crown / top shell
 * - side panels
 * - back plate
 * - brow ridge
 * - cheek guards
 * - rear neck guard
 */
public class OpenFaceHelmetModel<T extends net.minecraft.world.entity.LivingEntity> extends HumanoidModel<T> {

    public OpenFaceHelmetModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition root = mesh.getRoot();

        // Keep the normal head empty so the player's face remains visible.
        root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition hat = root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

        // Top shell: slightly inset so it feels more like a real helmet cap.
        hat.addOrReplaceChild("crown",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4.5F, -8.5F, -4.5F, 9.0F, 2.0F, 9.0F),
                PartPose.ZERO);

        // Brow ridge across the forehead.
        hat.addOrReplaceChild("brow",
                CubeListBuilder.create().texOffs(0, 12)
                        .addBox(-4.5F, -6.5F, -4.5F, 9.0F, 1.0F, 1.0F),
                PartPose.ZERO);

        // Left / right side panels.
        hat.addOrReplaceChild("left_side",
                CubeListBuilder.create().texOffs(0, 16)
                        .addBox(3.5F, -6.5F, -4.5F, 1.0F, 7.0F, 9.0F),
                PartPose.ZERO);

        hat.addOrReplaceChild("right_side",
                CubeListBuilder.create().texOffs(20, 16)
                        .addBox(-4.5F, -6.5F, -4.5F, 1.0F, 7.0F, 9.0F),
                PartPose.ZERO);

        // Back plate.
        hat.addOrReplaceChild("back",
                CubeListBuilder.create().texOffs(40, 16)
                        .addBox(-3.5F, -6.5F, 3.5F, 7.0F, 7.0F, 1.0F),
                PartPose.ZERO);

        // Cheek guards: leave the center front open so the face is visible.
        hat.addOrReplaceChild("left_cheek",
                CubeListBuilder.create().texOffs(0, 34)
                        .addBox(3.5F, -4.5F, -4.5F, 1.0F, 4.0F, 2.0F),
                PartPose.ZERO);

        hat.addOrReplaceChild("right_cheek",
                CubeListBuilder.create().texOffs(8, 34)
                        .addBox(-4.5F, -4.5F, -4.5F, 1.0F, 4.0F, 2.0F),
                PartPose.ZERO);

        // Small temple braces to help the helmet feel more enclosed without covering the face.
        hat.addOrReplaceChild("left_temple",
                CubeListBuilder.create().texOffs(16, 34)
                        .addBox(3.5F, -6.5F, -2.0F, 1.0F, 2.0F, 2.0F),
                PartPose.ZERO);

        hat.addOrReplaceChild("right_temple",
                CubeListBuilder.create().texOffs(24, 34)
                        .addBox(-4.5F, -6.5F, -2.0F, 1.0F, 2.0F, 2.0F),
                PartPose.ZERO);

        // Rear neck guard for a more helmet-like silhouette.
        hat.addOrReplaceChild("neck_guard",
                CubeListBuilder.create().texOffs(32, 34)
                        .addBox(-2.5F, -1.0F, 3.5F, 5.0F, 1.0F, 1.0F),
                PartPose.ZERO);

        // Placeholders expected by HumanoidModel.
        root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);

        return LayerDefinition.create(mesh, 64, 64);
    }
}
