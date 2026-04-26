package rem.endgate_armor.client;

import net.minecraft.world.entity.EquipmentSlot;

public final class GatewayArmorAnim {
    private GatewayArmorAnim() {}

    public static float getGatewayTime() {
        return (System.currentTimeMillis() % 100000L) / 1000.0F;
    }

    public static float getVerticalOffset(EquipmentSlot slot) {
        if (slot == EquipmentSlot.CHEST) {
            return 0.0F;
        }

        if (slot == EquipmentSlot.LEGS) {
            return 0.55F;
        }

        return 0.0F;
    }
}
