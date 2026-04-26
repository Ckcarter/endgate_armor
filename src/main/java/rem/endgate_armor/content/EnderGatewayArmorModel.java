package rem.endgate_armor.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public class EnderGatewayArmorModel extends HumanoidModel<LivingEntity> {

    public EnderGatewayArmorModel(EquipmentSlot slot) {
        super(Minecraft.getInstance().getEntityModels().bakeLayer(
                slot == EquipmentSlot.LEGS
                        ? ModelLayers.PLAYER_INNER_ARMOR
                        : ModelLayers.PLAYER_OUTER_ARMOR
        ));

        setupVisibleParts(slot);
    }

    private void setupVisibleParts(EquipmentSlot slot) {
        this.head.visible = slot == EquipmentSlot.HEAD;
        this.hat.visible = slot == EquipmentSlot.HEAD;

        this.body.visible = slot == EquipmentSlot.CHEST || slot == EquipmentSlot.LEGS;

        this.rightArm.visible = slot == EquipmentSlot.CHEST;
        this.leftArm.visible = slot == EquipmentSlot.CHEST;

        this.rightLeg.visible = slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET;
        this.leftLeg.visible = slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer ignoredBuffer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer gatewayBuffer = bufferSource.getBuffer(RenderType.endGateway());

        int fullBright = 0x00F000F0;

        renderPart(this.head, poseStack, gatewayBuffer, fullBright, packedOverlay);
        renderPart(this.hat, poseStack, gatewayBuffer, fullBright, packedOverlay);
        renderPart(this.body, poseStack, gatewayBuffer, fullBright, packedOverlay);
        renderPart(this.rightArm, poseStack, gatewayBuffer, fullBright, packedOverlay);
        renderPart(this.leftArm, poseStack, gatewayBuffer, fullBright, packedOverlay);
        renderPart(this.rightLeg, poseStack, gatewayBuffer, fullBright, packedOverlay);
        renderPart(this.leftLeg, poseStack, gatewayBuffer, fullBright, packedOverlay);
    }

    private static void renderPart(ModelPart part, PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        if (part != null && part.visible) {
            part.render(poseStack, consumer, light, overlay, 1.0F, 1.0F, 1.0F, 0.95F);
        }
    }
}
