package com.che.endportalarmor;

import com.che.endportalarmor.registry.ModCreativeTabs;
import com.che.endportalarmor.registry.ModItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(EndPortalArmorMod.MOD_ID)
public class EndPortalArmorMod {
    public static final String MOD_ID = "endportalarmor";

    public EndPortalArmorMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
    }
}
