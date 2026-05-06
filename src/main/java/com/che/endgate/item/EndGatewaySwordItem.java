package com.che.endgate.item;

import com.che.endgate.client.render.EndGatewaySwordRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class EndGatewaySwordItem extends SwordItem {

    public EndGatewaySwordItem() {
        super(Tiers.NETHERITE, 5, -2.4F,
                new Item.Properties()
                        .stacksTo(1)
                        .fireResistant());
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {

        consumer.accept(new IClientItemExtensions() {

            private final EndGatewaySwordRenderer renderer =
                    new EndGatewaySwordRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }
}
