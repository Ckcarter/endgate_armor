package rem.endgate_armor.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import rem.endgate_armor.Endgate_armor;

/**
 * Client-only: inject a portal-style overlay layer onto player renderers.
 */
@Mod.EventBusSubscriber(modid = Endgate_armor.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class EndgateClientEvents {

    private EndgateClientEvents() {}

    public static final KeyMapping TELEPORT_KEY = new KeyMapping(
            "key.endgate_armor.teleport",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "key.categories.endgate_armor"
    );

    @SubscribeEvent
    public static void onRegisterKeys(RegisterKeyMappingsEvent event) {
        event.register(TELEPORT_KEY);
    }
@SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        PlayerRenderer defaultRenderer = event.getSkin("default");
        if (defaultRenderer != null) {
            defaultRenderer.addLayer(new EndgatePortalArmorLayer(defaultRenderer));
        }

        PlayerRenderer slimRenderer = event.getSkin("slim");
        if (slimRenderer != null) {
            slimRenderer.addLayer(new EndgatePortalArmorLayer(slimRenderer));
        }
    }
}
