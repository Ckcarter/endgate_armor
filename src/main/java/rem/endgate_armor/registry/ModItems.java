package rem.endgate_armor.registry;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import rem.endgate_armor.Endgate_armor;
import rem.endgate_armor.content.EndgateArmorMaterial;

public final class ModItems {
    private ModItems() {}

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Endgate_armor.MODID);

    public static final RegistryObject<Item> ENDGATE_HELMET = ITEMS.register("endgate_helmet",
            () -> new ArmorItem(EndgateArmorMaterial.INSTANCE, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<Item> ENDGATE_CHESTPLATE = ITEMS.register("endgate_chestplate",
            () -> new ArmorItem(EndgateArmorMaterial.INSTANCE, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final RegistryObject<Item> ENDGATE_LEGGINGS = ITEMS.register("endgate_leggings",
            () -> new ArmorItem(EndgateArmorMaterial.INSTANCE, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final RegistryObject<Item> ENDGATE_BOOTS = ITEMS.register("endgate_boots",
            () -> new ArmorItem(EndgateArmorMaterial.INSTANCE, ArmorItem.Type.BOOTS, new Item.Properties()));
}
