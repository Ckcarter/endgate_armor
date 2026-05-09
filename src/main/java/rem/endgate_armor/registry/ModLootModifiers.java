package rem.endgate_armor.registry;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import rem.endgate_armor.Endgate_armor;
import rem.endgate_armor.loot.EndgateArmorLootModifier;

public class ModLootModifiers {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Endgate_armor.MODID);

    public static final RegistryObject<Codec<EndgateArmorLootModifier>> ENDGATE_ARMOR_LOOT =
            LOOT_MODIFIER_SERIALIZERS.register("endgate_armor_loot", () -> EndgateArmorLootModifier.CODEC);
}
