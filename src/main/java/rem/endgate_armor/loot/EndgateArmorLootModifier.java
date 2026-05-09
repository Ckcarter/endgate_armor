package rem.endgate_armor.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;
import rem.endgate_armor.registry.ModItems;
import rem.endgate_armor.registry.ModLootModifiers;

public class EndgateArmorLootModifier extends LootModifier {

    public static final Codec<EndgateArmorLootModifier> CODEC = RecordCodecBuilder.create(instance ->
            codecStart(instance).apply(instance, EndgateArmorLootModifier::new)
    );

    public EndgateArmorLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(
            ObjectArrayList<ItemStack> generatedLoot,
            LootContext context
    ) {
        // 0.08F = 8% chance per matching chest.
        if (context.getRandom().nextFloat() > 0.08F) {
            return generatedLoot;
        }

        Item[] armorPieces = new Item[]{
                ModItems.ENDGATE_HELMET.get(),
                ModItems.ENDGATE_CHESTPLATE.get(),
                ModItems.ENDGATE_LEGGINGS.get(),
                ModItems.ENDGATE_BOOTS.get()
        };

        Item chosenPiece = armorPieces[context.getRandom().nextInt(armorPieces.length)];
        generatedLoot.add(new ItemStack(chosenPiece));

        return generatedLoot;
    }

    @Override
    public Codec<? extends LootModifier> codec() {
        return ModLootModifiers.ENDGATE_ARMOR_LOOT.get();
    }
}
