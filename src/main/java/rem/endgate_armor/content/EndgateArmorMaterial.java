package rem.endgate_armor.content;

import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Items;

import java.util.EnumMap;

/**
 * Diamond-tier armor material with End Gateway theming.
 * Stats intentionally match vanilla diamond.
 */
public class EndgateArmorMaterial implements ArmorMaterial {

    public static final EndgateArmorMaterial INSTANCE = new EndgateArmorMaterial();

    // Vanilla-style base durability multipliers (helmet, chest, legs, boots) via type.
    private static final EnumMap<ArmorItem.Type, Integer> DURABILITY_MULTIPLIERS = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 13);
        map.put(ArmorItem.Type.LEGGINGS, 15);
        map.put(ArmorItem.Type.CHESTPLATE, 16);
        map.put(ArmorItem.Type.HELMET, 11);
    });

    // Vanilla diamond defenses: 3, 8, 6, 3 (boots, chest, legs, helmet)
    private static final EnumMap<ArmorItem.Type, Integer> DEFENSE = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 3);
        map.put(ArmorItem.Type.LEGGINGS, 6);
        map.put(ArmorItem.Type.CHESTPLATE, 8);
        map.put(ArmorItem.Type.HELMET, 3);
    });

    private EndgateArmorMaterial() {}

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        // Diamond uses 33 as the base multiplier
        return DURABILITY_MULTIPLIERS.getOrDefault(type, 0) * 33;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return DEFENSE.getOrDefault(type, 0);
    }

    @Override
    public int getEnchantmentValue() {
        // Diamond enchantability is 10
        return 10;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_DIAMOND;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(Items.DIAMOND);
    }

    @Override
    public String getName() {
        // This is used for armor textures: textures/models/armor/<name>_layer_1.png
        return "endgate";
    }

    @Override
    public float getToughness() {
        // Diamond toughness is 2.0
        return 2.0F;
    }

    @Override
    public float getKnockbackResistance() {
        // Diamond knockback resistance is 0
        return 0.0F;
    }
}
