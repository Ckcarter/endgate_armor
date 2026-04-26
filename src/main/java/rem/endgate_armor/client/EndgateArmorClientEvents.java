package rem.endgate_armor.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import rem.endgate_armor.EndgateArmorMod;
import rem.endgate_armor.client.key.GatewayConfigKeybind;
import rem.endgate_armor.client.screen.GatewayRendererConfigScreen;

@Mod.EventBusSubscriber(modid = EndgateArmorMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class EndgateArmorClientEvents {
    private EndgateArmorClientEvents() {}

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        GatewayConfigKeybind.register(event);
    }

    @Mod.EventBusSubscriber(modid = EndgateArmorMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static final class ForgeClientEvents {
        private ForgeClientEvents() {}

        @SubscribeEvent
        public static void clientTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.END) return;

            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;

            while (GatewayConfigKeybind.OPEN_CONFIG.consumeClick()) {
                mc.setScreen(new GatewayRendererConfigScreen(mc.screen));
            }
        }
    }
}
