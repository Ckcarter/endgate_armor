package rem.endgate_armor.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import rem.endgate_armor.Endgate_armor;
import rem.endgate_armor.block.entity.ItemGeneratorBlockEntity;

public final class ModBlockEntities {
    private ModBlockEntities() {}

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Endgate_armor.MODID);

    public static final RegistryObject<BlockEntityType<ItemGeneratorBlockEntity>> ITEM_GENERATOR =
            BLOCK_ENTITIES.register("item_generator", () ->
                    BlockEntityType.Builder.of(ItemGeneratorBlockEntity::new, ModBlocks.ITEM_GENERATOR.get()).build(null));
}
