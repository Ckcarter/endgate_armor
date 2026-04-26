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
import rem.endgate_armor.client.model.OpenFaceHelmetModel;
import rem.endgate_armor.client.model.ShortSleeveArmorModel;
import rem.endgate_armor.client.model.VanillaEndgateBootsModel;
import rem.endgate_armor.registry.ModItems;

/**
 * Vanilla-aligned End Gateway armor render layer.
 *
 * Model usage matches Minecraft armor proportions:
 * - Helmet: custom open-face helmet, aligned to vanilla head armor.
 * - Chest: outer armor body, with your short-sleeve arm cutoff.
 * - Legs: vanilla inner armor model.
 * - Boots: custom vanilla-foot-only model so the gateway effect stops at the ankle.
 */
public final class EndgatePortalArmorLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private final HumanoidModel<AbstractClientPlayer> innerArmorModel;
    private final HumanoidModel<AbstractClientPlayer> outerArmorModel;
    private final OpenFaceHelmetModel<AbstractClientPlayer> helmetModel;
    private final ShortSleeveArmorModel<AbstractClientPlayer> shortSleeveChestModel;
    private final VanillaEndgateBootsModel<AbstractClientPlayer> bootsModel;

    public EndgatePortalArmorLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
        this.innerArmorModel = new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
        this.outerArmorModel = new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR));
        this.helmetModel = new OpenFaceHelmetModel<>(OpenFaceHelmetModel.createBodyLayer().bakeRoot());
        this.shortSleeveChestModel = new ShortSleeveArmorModel<>(ShortSleeveArmorModel.createBodyLayer().bakeRoot());
        this.bootsModel = new VanillaEndgateBootsModel<>(VanillaEndgateBootsModel.createBodyLayer().bakeRoot());
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, AbstractClientPlayer player,
                       float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks,
                       float netHeadYaw, float headPitch) {

        if (!isWearingAnyEndgate(player)) return;

        VertexConsumer portalConsumer = bufferSource.getBuffer(RenderType.endPortal());

        renderForSlot(poseStack, portalConsumer, packedLight, player, EquipmentSlot.HEAD,
                helmetModel, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 0.82F);

        renderForSlot(poseStack, portalConsumer, packedLight, player, EquipmentSlot.CHEST,
                shortSleeveChestModel, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 0.82F);

        renderForSlot(poseStack, portalConsumer, packedLight, player, EquipmentSlot.LEGS,
                innerArmorModel, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 0.82F);

        renderForSlot(poseStack, portalConsumer, packedLight, player, EquipmentSlot.FEET,
                bootsModel, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 0.82F);
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
                               float netHeadYaw, float headPitch, float alpha) {

        if (!isEndgateForSlot(player, slot)) return;

        this.getParentModel().copyPropertiesTo(model);
        model.prepareMobModel(player, limbSwing, limbSwingAmount, 0.0F);
        model.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        setVisibleForSlot(model, slot);

        poseStack.pushPose();
        model.renderToBuffer(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, alpha);
        poseStack.popPose();
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
                model.body.visible = false;
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
            }
            case FEET -> {
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
            }
            default -> { }
        }
    }
}
