package rem.endgate_armor.client.key;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public final class GatewayConfigKeybind {
    private GatewayConfigKeybind() {}

    public static final KeyMapping OPEN_CONFIG = new KeyMapping(
            "key.endgate_armor.open_gateway_config",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "key.categories.endgate_armor"
    );

    public static void register(RegisterKeyMappingsEvent event) {
        event.register(OPEN_CONFIG);
    }
}
