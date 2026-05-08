package rem.endgate_armor.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class EndgateShieldRenderer extends BlockEntityWithoutLevelRenderer {

    private ShieldModel shieldModel;

    public EndgateShieldRenderer() {
        super(
                Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels()
        );
    }

    private ShieldModel getShieldModel() {
        if (shieldModel == null) {
            EntityModelSet models = Minecraft.getInstance().getEntityModels();
            if (models == null) return null;

            shieldModel = new ShieldModel(
                    models.bakeLayer(ModelLayers.SHIELD)
            );
        }

        return shieldModel;
    }

    @Override
    public void renderByItem(
            ItemStack stack,
            ItemDisplayContext displayContext,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay) {

        ShieldModel model = getShieldModel();
        if (model == null) return;

        poseStack.pushPose();
        poseStack.scale(1.0F, -1.0F, -1.0F);

        VertexConsumer handleBuffer = bufferSource.getBuffer(
                RenderType.entityCutoutNoCull(
                        new ResourceLocation("textures/entity/shield_base.png")
                )
        );

        model.handle().render(
                poseStack,
                handleBuffer,
                packedLight,
                OverlayTexture.NO_OVERLAY
        );

        VertexConsumer gatewayBuffer =
                bufferSource.getBuffer(RenderType.endGateway());

        model.plate().render(
                poseStack,
                gatewayBuffer,
                packedLight,
                OverlayTexture.NO_OVERLAY
        );

        poseStack.popPose();
    }
}
