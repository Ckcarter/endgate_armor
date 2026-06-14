package rem.endgate_armor.registry;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import rem.endgate_armor.Endgate_armor;
import rem.endgate_armor.content.EndgateArmorMaterial;
import rem.endgate_armor.item.TransparentEndgateArmorItem;
import rem.endgate_armor.item.EndgateShieldItem;
public final class ModItems {
    private ModItems() {}

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Endgate_armor.MODID);

    public static final RegistryObject<Item> ENDGATE_HELMET = ITEMS.register("endgate_helmet",
            () -> new TransparentEndgateArmorItem(EndgateArmorMaterial.INSTANCE, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<Item> ENDGATE_CHESTPLATE = ITEMS.register("endgate_chestplate",
            () -> new TransparentEndgateArmorItem(EndgateArmorMaterial.INSTANCE, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final RegistryObject<Item> ENDGATE_LEGGINGS = ITEMS.register("endgate_leggings",
            () -> new TransparentEndgateArmorItem(EndgateArmorMaterial.INSTANCE, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final RegistryObject<Item> ENDGATE_BOOTS = ITEMS.register("endgate_boots",
            () -> new TransparentEndgateArmorItem(EndgateArmorMaterial.INSTANCE, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> ENDGATE_SWORD = ITEMS.register("endgate_sword",
            () -> new SwordItem(Tiers.DIAMOND, 4, -2.4F, new Item.Properties().durability(1561)));

    public static final RegistryObject<Item> ENDGATE_SHIELD = ITEMS.register("endgate_shield",
            EndgateShieldItem::new);

    public static final RegistryObject<Item> COSMIC_INGOT = ITEMS.register("cosmic_ingot",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COSMIC_DUST = ITEMS.register("cosmic_dust",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COSMIC_CHIP = ITEMS.register("cosmic_chip",
            () -> new Item(new Item.Properties()));
}
