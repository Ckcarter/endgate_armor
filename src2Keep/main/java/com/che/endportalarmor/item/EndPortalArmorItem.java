package com.che.endportalarmor.item;

import com.che.endportalarmor.EndPortalArmorMod;
import com.che.endportalarmor.client.EndPortalArmorClient;
import com.che.endportalarmor.client.model.OpenFrameHelmetModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class EndPortalArmorItem extends ArmorItem {
    public EndPortalArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private HumanoidModel<LivingEntity> helmetModel;

            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> defaultModel) {
                if (slot != EquipmentSlot.HEAD) {
                    return defaultModel;
                }

                if (helmetModel == null) {
                    ModelPart baked = Minecraft.getInstance().getEntityModels().bakeLayer(EndPortalArmorClient.OPEN_FRAME_HELMET_LAYER);
                    helmetModel = new OpenFrameHelmetModel<>(baked);
                }

                ((HumanoidModel<LivingEntity>) defaultModel).copyPropertiesTo(helmetModel);

                helmetModel.crouching = defaultModel.crouching;
                helmetModel.riding = defaultModel.riding;
                helmetModel.young = defaultModel.young;

                helmetModel.head.visible = true;
                helmetModel.hat.visible = true;
                helmetModel.body.visible = false;
                helmetModel.rightArm.visible = false;
                helmetModel.leftArm.visible = false;
                helmetModel.rightLeg.visible = false;
                helmetModel.leftLeg.visible = false;

                return helmetModel;
            }
        });
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        int tick = entity != null ? entity.tickCount : 0;
        int frame = Math.floorMod(tick / 2, 8);
        String layer = slot == EquipmentSlot.LEGS ? "2" : "1";
        return EndPortalArmorMod.MOD_ID + ":textures/models/armor/end_portal_layer_" + layer + "_" + frame + ".png";
    }
}
