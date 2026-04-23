package com.che.endportalarmor.registry;

import com.che.endportalarmor.EndPortalArmorMod;
import com.che.endportalarmor.item.EndPortalArmorItem;
import com.che.endportalarmor.item.ModArmorMaterial;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EndPortalArmorMod.MOD_ID);

    public static final ModArmorMaterial END_PORTAL_MATERIAL = new ModArmorMaterial();

    public static final RegistryObject<Item> END_PORTAL_HELMET = ITEMS.register("end_portal_helmet",
            () -> new EndPortalArmorItem(END_PORTAL_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> END_PORTAL_CHESTPLATE = ITEMS.register("end_portal_chestplate",
            () -> new EndPortalArmorItem(END_PORTAL_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> END_PORTAL_LEGGINGS = ITEMS.register("end_portal_leggings",
            () -> new EndPortalArmorItem(END_PORTAL_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> END_PORTAL_BOOTS = ITEMS.register("end_portal_boots",
            () -> new EndPortalArmorItem(END_PORTAL_MATERIAL, ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
