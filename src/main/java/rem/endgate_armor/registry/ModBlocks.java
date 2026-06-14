package rem.endgate_armor.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import rem.endgate_armor.Endgate_armor;
import rem.endgate_armor.block.ItemGeneratorBlock;
import rem.endgate_armor.block.CreativePowerSupplyBlock;

public final class ModBlocks {
    private ModBlocks() {}

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Endgate_armor.MODID);

    public static final RegistryObject<Block> ITEM_GENERATOR = registerBlock("item_generator",
            () -> new ItemGeneratorBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(4.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops().noOcclusion()));

    public static final RegistryObject<Block> CREATIVE_POWER_SUPPLY = registerBlock("creative_power_supply",
            () -> new CreativePowerSupplyBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()));

    private static RegistryObject<Block> registerBlock(String name, java.util.function.Supplier<Block> block) {
        RegistryObject<Block> registryObject = BLOCKS.register(name, block);
        ModItems.ITEMS.register(name, () -> new BlockItem(registryObject.get(), new Item.Properties()));
        return registryObject;
    }
}
