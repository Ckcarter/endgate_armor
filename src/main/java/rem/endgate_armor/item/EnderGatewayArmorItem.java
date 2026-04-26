package com.che.endergatewayarmor.item;

import com.che.endergatewayarmor.client.EnderGatewayArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class EnderGatewayArmorItem extends ArmorItem {
    public EnderGatewayArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack,
                                                           EquipmentSlot slot, HumanoidModel<?> original) {
                EnderGatewayArmorModel<LivingEntity> model = new EnderGatewayArmorModel<>(slot);
                original.copyPropertiesTo(model);
                return model;
            }
        });
    }
}
