package rem.endgate_armor.registry;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import rem.endgate_armor.Endgate_armor;
import rem.endgate_armor.menu.ItemGeneratorMenu;

public final class ModMenus {
    private ModMenus() {}

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Endgate_armor.MODID);

    public static final RegistryObject<MenuType<ItemGeneratorMenu>> ITEM_GENERATOR_MENU = MENUS.register(
            "item_generator_menu",
            () -> IForgeMenuType.create(ItemGeneratorMenu::new)
    );
}
