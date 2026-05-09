package rem.endgate_armor.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

/**
 * Use this item class for your Endgate armor pieces.
 *
 * The normal vanilla armor texture pass returns blank transparent textures.
 * The visible gateway effect is drawn by EndgateArmorRenderLayer.
 */
public class EndGatewayArmorItem extends ArmorItem {

    public EndGatewayArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if (slot == EquipmentSlot.LEGS) {
            return "endgate_armor:textures/models/armor/endgate_layer_2.png";
        }

        return "endgate_armor:textures/models/armor/endgate_layer_1.png";
    }
}
