package rem.endgate_armor.client.config;

public final class GatewayRenderConfig {
    private GatewayRenderConfig() {}

    // Main renderer size
    public static float GLOBAL_SCALE = 1.0F;

    // Per armor part size controls
    public static float HELMET_SCALE = 1.0F;
    public static float CHEST_SCALE = 1.0F;
    public static float LEGS_SCALE = 1.0F;
    public static float BOOTS_SCALE = 1.0F;

    // Useful for fixing tiny seams
    public static float LEGS_Y_OFFSET = -0.25F;

    public static void reset() {
        GLOBAL_SCALE = 1.0F;
        HELMET_SCALE = 1.0F;
        CHEST_SCALE = 1.0F;
        LEGS_SCALE = 1.0F;
        BOOTS_SCALE = 1.0F;
        LEGS_Y_OFFSET = -0.25F;
    }

    public static float getPartScale(net.minecraft.world.entity.EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> HELMET_SCALE;
            case CHEST -> CHEST_SCALE;
            case LEGS -> LEGS_SCALE;
            case FEET -> BOOTS_SCALE;
            default -> 1.0F;
        };
    }
}
