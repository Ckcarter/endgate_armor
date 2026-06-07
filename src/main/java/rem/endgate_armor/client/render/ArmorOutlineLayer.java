package rem.endgate_armor.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

import rem.endgate_armor.Endgate_armor;
import rem.endgate_armor.registry.ModItems;

public class ArmorOutlineLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private static final ResourceLocation OUTLINE_TEXTURE =
            new ResourceLocation(Endgate_armor.MODID, "textures/armor/endgate_outline.png");

    public ArmorOutlineLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            AbstractClientPlayer player,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        if (!hasEndgateArmor(player)) return;

        poseStack.pushPose();

        // Slightly larger than player model to create outline effect
        poseStack.scale(1.04F, 1.04F, 1.04F);

        this.getParentModel().renderToBuffer(
                poseStack,
                buffer.getBuffer(RenderType.entityTranslucentEmissive(OUTLINE_TEXTURE)),
                15728640,
                net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,
                0.2F, 0.8F, 1.0F, 0.65F
        );

        poseStack.popPose();
    }

    private boolean hasEndgateArmor(AbstractClientPlayer player) {
        return player.getInventory().armor.stream().anyMatch(stack ->
                stack.is(ModItems.ENDGATE_HELMET.get()) ||
                stack.is(ModItems.ENDGATE_CHESTPLATE.get()) ||
                stack.is(ModItems.ENDGATE_LEGGINGS.get()) ||
                stack.is(ModItems.ENDGATE_BOOTS.get())
        );
    }
}
