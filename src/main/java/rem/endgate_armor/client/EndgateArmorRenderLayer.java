package rem.endgate_armor.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import rem.endgate_armor.Endgate_armor;
import rem.endgate_armor.registry.ModItems;

/**
 * Safe End Gateway-style armor renderer for Forge 1.20.1 / Direwolf20.
 *
 * IMPORTANT:
 * - Does NOT use RenderType.endGateway()
 * - Does NOT use RenderType.endPortal()
 * - Uses normal entity armor rendering with an emissive animated texture
 * - Vanilla armor pass should use blank transparent textures
 */
public class EndgateArmorRenderLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

//    private static final ResourceLocation PORTAL_LAYER_1 =
//            new ResourceLocation(Endgate_armor.MODID, "textures/models/armor/endgate_portal_layer_1.png");

//    private static final ResourceLocation PORTAL_LAYER_2 =
//            new ResourceLocation(Endgate_armor.MODID, "textures/models/armor/endgate_portal_layer_2.png");

    private final HumanoidModel<AbstractClientPlayer> innerArmorModel;
    private final HumanoidModel<AbstractClientPlayer> outerArmorModel;

    public EndgateArmorRenderLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);

        this.innerArmorModel = new HumanoidModel<>(
                Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)
        );

        this.outerArmorModel = new HumanoidModel<>(
                Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)
        );
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            AbstractClientPlayer player,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
//        renderSlot(poseStack, bufferSource, packedLight, player, EquipmentSlot.HEAD,
//                outerArmorModel, PORTAL_LAYER_1, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
//
//        renderSlot(poseStack, bufferSource, packedLight, player, EquipmentSlot.CHEST,
//                outerArmorModel, PORTAL_LAYER_1, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

//        renderSlot(poseStack, bufferSource, packedLight, player, EquipmentSlot.LEGS,
//                innerArmorModel, PORTAL_LAYER_2, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

//        renderSlot(poseStack, bufferSource, packedLight, player, EquipmentSlot.FEET,
//                outerArmorModel, PORTAL_LAYER_1, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private void renderSlot(
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            AbstractClientPlayer player,
            EquipmentSlot slot,
            HumanoidModel<AbstractClientPlayer> model,
            ResourceLocation texture,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        if (!isEndgateArmor(player.getItemBySlot(slot), slot)) {
            return;
        }

        this.getParentModel().copyPropertiesTo(model);
        model.prepareMobModel(player, limbSwing, limbSwingAmount, 0.0F);
        model.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        setVisibleForSlot(model, slot);

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucentEmissive(texture));

        poseStack.pushPose();
        model.renderToBuffer(
                poseStack,
                consumer,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                1.0F,
                1.0F,
                1.0F,
                0.92F
        );
        poseStack.popPose();
    }

    private static boolean isEndgateArmor(ItemStack stack, EquipmentSlot slot) {
        if (stack.isEmpty()) {
            return false;
        }

        return switch (slot) {
            case HEAD -> stack.is(ModItems.ENDGATE_HELMET.get());
            case CHEST -> stack.is(ModItems.ENDGATE_CHESTPLATE.get());
            case LEGS -> stack.is(ModItems.ENDGATE_LEGGINGS.get());
            case FEET -> stack.is(ModItems.ENDGATE_BOOTS.get());
            default -> false;
        };
    }

    private static void setVisibleForSlot(HumanoidModel<?> model, EquipmentSlot slot) {
        model.setAllVisible(false);

        switch (slot) {
            case HEAD -> {
                model.head.visible = true;
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
            default -> {
            }
        }
    }
}
