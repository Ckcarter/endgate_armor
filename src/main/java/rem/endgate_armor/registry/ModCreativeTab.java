package rem.endgate_armor.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import rem.endgate_armor.Endgate_armor;

public final class ModCreativeTab {
    private ModCreativeTab() {}

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Endgate_armor.MODID);

    public static final RegistryObject<CreativeModeTab> ENDGATE_TAB = TABS.register("endgate_tab", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.endgate_armor.endgate_tab"))
                    .icon(() -> new ItemStack(ModItems.ENDGATE_CHESTPLATE.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.ENDGATE_HELMET.get());
                        output.accept(ModItems.ENDGATE_CHESTPLATE.get());
                        output.accept(ModItems.ENDGATE_LEGGINGS.get());
                        output.accept(ModItems.ENDGATE_BOOTS.get());
                    })
                    .build()
    );
}
