package rem.endgate_armor.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;

/**
 * Plain vanilla-style Minecraft shield.
 *
 * No custom renderer.
 * No custom texture.
 * Uses Minecraft's built-in shield model/hand pose behavior.
 */
public class EndgateShieldItem extends ShieldItem {

    public EndgateShieldItem() {
        super(new Item.Properties().durability(336));
    }

    @Override
    public boolean isValidRepairItem(ItemStack shield, ItemStack repair) {
        return repair.is(Items.OAK_PLANKS) || super.isValidRepairItem(shield, repair);
    }
}
