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
import rem.endgate_armor.network.EndgateNetwork;
import rem.endgate_armor.registry.ModBlockEntities;
import rem.endgate_armor.registry.ModBlocks;
import rem.endgate_armor.registry.ModCreativeTab;
import rem.endgate_armor.registry.ModItems;
import rem.endgate_armor.registry.ModMenus;

@Mod(Endgate_armor.MODID)
public class Endgate_armor {

    public static final String MODID = "endgate_armor";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Endgate_armor() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);
        ModCreativeTab.TABS.register(modEventBus);

        EndgateNetwork.register();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Endgate Armor loaded.");
    }
}
