package com.che.endportalarmor.registry;

import com.che.endportalarmor.EndPortalArmorMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EndPortalArmorMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> END_PORTAL_ARMOR_TAB = CREATIVE_MODE_TABS.register("end_portal_armor_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.endportalarmor.end_portal_armor_tab"))
                    .icon(() -> new ItemStack(ModItems.END_PORTAL_HELMET.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.END_PORTAL_HELMET.get());
                        output.accept(ModItems.END_PORTAL_CHESTPLATE.get());
                        output.accept(ModItems.END_PORTAL_LEGGINGS.get());
                        output.accept(ModItems.END_PORTAL_BOOTS.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
