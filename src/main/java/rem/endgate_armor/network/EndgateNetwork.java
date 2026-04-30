package rem.endgate_armor.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import rem.endgate_armor.Endgate_armor;

public final class EndgateNetwork {
    private EndgateNetwork() {}

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Endgate_armor.MODID, "main"))
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .simpleChannel();

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(id++, TeleportAbilityPacket.class,
                TeleportAbilityPacket::encode,
                TeleportAbilityPacket::decode,
                TeleportAbilityPacket::handle);
    }
}
