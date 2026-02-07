package rem.endgate_armor;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import rem.endgate_armor.registry.ModCreativeTab;
import rem.endgate_armor.registry.ModItems;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Endgate_armor.MODID)
public class Endgate_armor {

    public static final String MODID = "endgate_armor";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Endgate_armor() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register content
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTab.TABS.register(modEventBus);

        // Register configs
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Endgate Armor loaded.");
    }
}
