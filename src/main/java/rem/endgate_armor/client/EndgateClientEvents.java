package rem.endgate_armor.client;

import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import rem.endgate_armor.Endgate_armor;

/**
 * Client-only: inject a portal-style overlay layer onto player renderers.
 */
@Mod.EventBusSubscriber(modid = Endgate_armor.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class EndgateClientEvents {

    private EndgateClientEvents() {}

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        // Player renderers exist for both skin types.
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
