package rem.endgate_armor.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import rem.endgate_armor.menu.ItemGeneratorMenu;

public class ItemGeneratorScreen extends AbstractContainerScreen<ItemGeneratorMenu> {
    public ItemGeneratorScreen(ItemGeneratorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = this.leftPos;
        int y = this.topPos;

        graphics.fill(x, y, x + imageWidth, y + imageHeight, 0xFF2B2B2B);
        graphics.fill(x + 4, y + 4, x + imageWidth - 4, y + imageHeight - 4, 0xFF3A3A3A);
        graphics.fill(x + 78, y + 33, x + 98, y + 53, 0xFF1E1E1E);

        int energyHeight = (int) (48.0F * menu.getEnergy() / menu.getMaxEnergy());
        graphics.fill(x + 151, y + 18, x + 163, y + 66, 0xFF111111);
        graphics.fill(x + 151, y + 66 - energyHeight, x + 163, y + 66, 0xFFE0C03A);

        int progressWidth = (int) (52.0F * menu.getProgress() / menu.getMaxProgress());
        graphics.fill(x + 62, y + 60, x + 114, y + 68, 0xFF111111);
        graphics.fill(x + 62, y + 60, x + 62 + progressWidth, y + 68, 0xFFFFD700);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFFFFFF, false);
        graphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0xFFFFFF, false);

        int secondsLeft = Math.max(0, (menu.getMaxProgress() - menu.getProgress()) / 20);
        graphics.drawString(this.font, "FE: " + menu.getEnergy() + "/" + menu.getMaxEnergy(), 8, 20, 0xFFFFFF, false);
        graphics.drawString(this.font, "Gold in: " + secondsLeft + "s", 8, 32, 0xFFFFFF, false);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}
