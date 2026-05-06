package rem.endgate_armor.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class EndGatewaySwordRenderer extends BlockEntityWithoutLevelRenderer {

    public EndGatewaySwordRenderer() {
        super(
                Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels()
        );
    }

    @Override
    public void renderByItem(
            ItemStack stack,
            ItemDisplayContext displayContext,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            int packedOverlay
    ) {

        poseStack.pushPose();

        poseStack.translate(0.0F, 0.1F, 0.0F);

        VertexConsumer consumer =
                buffer.getBuffer(RenderType.endGateway());

        PoseStack.Pose pose = poseStack.last();

        float minX = -0.06F;
        float maxX = 0.06F;

        float minY = -0.7F;
        float maxY = 0.7F;

        float z = 0.0F;

        consumer.vertex(pose.pose(), minX, minY, z).endVertex();
        consumer.vertex(pose.pose(), maxX, minY, z).endVertex();
        consumer.vertex(pose.pose(), maxX, maxY, z).endVertex();
        consumer.vertex(pose.pose(), minX, maxY, z).endVertex();

        poseStack.popPose();
    }
}
