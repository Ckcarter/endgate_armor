package rem.endgate_armor.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import rem.endgate_armor.client.model.OpenFaceHelmetModel;
import rem.endgate_armor.registry.ModItems;

import java.util.function.Consumer;

public class TransparentEndgateArmorItem extends ArmorItem {

    public TransparentEndgateArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return slot == EquipmentSlot.LEGS ? "endgate_armor:textures/models/armor/endgate_layer_2.png" : "endgate_armor:textures/models/armor/endgate_layer_1.png";
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private OpenFaceHelmetModel<LivingEntity> openFaceHelmetModel;

            @SuppressWarnings("unchecked")
            @Override
            public @Nullable HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack stack,
                                                                    EquipmentSlot slot, HumanoidModel<?> original) {
                if (slot == EquipmentSlot.HEAD && stack.is(ModItems.ENDGATE_HELMET.get())) {
                    if (openFaceHelmetModel == null) {
                        openFaceHelmetModel = new OpenFaceHelmetModel<>(OpenFaceHelmetModel.createBodyLayer().bakeRoot());
                    }

                    ((HumanoidModel<LivingEntity>) original).copyPropertiesTo(openFaceHelmetModel);
                    openFaceHelmetModel.setAllVisible(false);
                    openFaceHelmetModel.hat.visible = true;
                    openFaceHelmetModel.head.visible = false;
                    return openFaceHelmetModel;
                }
                return original;
            }
        });
    }
}