package rem.endgate_armor.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import rem.endgate_armor.Endgate_armor;
import rem.endgate_armor.network.EndgateNetwork;
import rem.endgate_armor.network.TeleportAbilityPacket;

@Mod.EventBusSubscriber(modid = Endgate_armor.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class EndgateClientForgeEvents {
    private EndgateClientForgeEvents() {}

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null || minecraft.screen != null) return;

        while (EndgateClientEvents.TELEPORT_KEY.consumeClick()) {
            EndgateNetwork.CHANNEL.sendToServer(new TeleportAbilityPacket());
        }
    }
}
