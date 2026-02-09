package rem.endgate_armor.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import rem.endgate_armor.registry.ModItems;

/**
 * Renders an End-Portal/Gateway style overlay on top of Endgate armor pieces
 * using vanilla portal shaders (RenderType.endPortal()).
 *
 * This is what gives the real "infinite starfield" movement, which cannot be
 * reproduced with animated textures alone.
 */
public final class EndgatePortalArmorLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private final HumanoidModel<AbstractClientPlayer> innerModel;
    private final HumanoidModel<AbstractClientPlayer> outerModel;

    public EndgatePortalArmorLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
        this.innerModel = new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
        this.outerModel = new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, AbstractClientPlayer player,
                       float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks,
                       float netHeadYaw, float headPitch) {

        // Only render overlay if the player is wearing at least one Endgate armor piece.
        if (!isWearingAnyEndgate(player)) return;

        // Vanilla portal shader layer. Fullbright-ish, ignores normal armor texture sampling.
        VertexConsumer vc = bufferSource.getBuffer(RenderType.endPortal());

        // Render each equipped piece with the proper armor model (inner for legs, outer for others).
        renderForSlot(poseStack, vc, packedLight, player, EquipmentSlot.HEAD,
                outerModel, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        renderForSlot(poseStack, vc, packedLight, player, EquipmentSlot.CHEST,
                outerModel, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        renderForSlot(poseStack, vc, packedLight, player, EquipmentSlot.LEGS,
                innerModel, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        renderForSlot(poseStack, vc, packedLight, player, EquipmentSlot.FEET,
                outerModel, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private static boolean isWearingAnyEndgate(AbstractClientPlayer player) {
        for (ItemStack stack : player.getInventory().armor) {
            if (stack.is(ModItems.ENDGATE_HELMET.get())
                    || stack.is(ModItems.ENDGATE_CHESTPLATE.get())
                    || stack.is(ModItems.ENDGATE_LEGGINGS.get())
                    || stack.is(ModItems.ENDGATE_BOOTS.get())) {
                return true;
            }
        }
        return false;
    }

    private static boolean isEndgateForSlot(AbstractClientPlayer player, EquipmentSlot slot) {
        ItemStack stack = player.getItemBySlot(slot);
        if (stack.isEmpty()) return false;
        return (slot == EquipmentSlot.HEAD && stack.is(ModItems.ENDGATE_HELMET.get()))
                || (slot == EquipmentSlot.CHEST && stack.is(ModItems.ENDGATE_CHESTPLATE.get()))
                || (slot == EquipmentSlot.LEGS && stack.is(ModItems.ENDGATE_LEGGINGS.get()))
                || (slot == EquipmentSlot.FEET && stack.is(ModItems.ENDGATE_BOOTS.get()));
    }

    private void renderForSlot(PoseStack poseStack, VertexConsumer vc, int packedLight, AbstractClientPlayer player,
                               EquipmentSlot slot, HumanoidModel<AbstractClientPlayer> model,
                               float limbSwing, float limbSwingAmount, float ageInTicks,
                               float netHeadYaw, float headPitch) {

        if (!isEndgateForSlot(player, slot)) return;

        // Match player pose/animation.
        this.getParentModel().copyPropertiesTo(model);
        model.prepareMobModel(player, limbSwing, limbSwingAmount, 0.0F);
        model.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        // Only show the parts for this slot (and hide everything else).
        setVisibleForSlot(model, slot);

        // Slight scale-up to avoid z-fighting with the regular armor layer.
        poseStack.pushPose();
        float s = (slot == EquipmentSlot.LEGS) ? 1.005f : 1.01f;
        poseStack.scale(s, s, s);

        model.renderToBuffer(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 0.55f);

        poseStack.popPose();
    }

    private static void setVisibleForSlot(HumanoidModel<?> model, EquipmentSlot slot) {
        model.setAllVisible(false);
        switch (slot) {
            case HEAD -> {
                // For helmets, render ONLY the outer "hat" cube.
                // Rendering the "head" cube makes the portal effect cover the player's face
                // and reads like a solid block.
                model.head.visible = false;
                model.hat.visible = true;
            }
            case CHEST -> {
                model.body.visible = true;
                model.rightArm.visible = true;
                model.leftArm.visible = true;
            }
            case LEGS -> {
                model.body.visible = true;
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
            }
            case FEET -> {
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
            }
            default -> {}
        }
    }
}
