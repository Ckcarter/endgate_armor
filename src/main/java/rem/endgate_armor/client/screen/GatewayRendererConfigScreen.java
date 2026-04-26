package rem.endgate_armor.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import rem.endgate_armor.client.config.GatewayRenderConfig;

public class GatewayRendererConfigScreen extends Screen {
    private final Screen parent;

    public GatewayRendererConfigScreen(Screen parent) {
        super(Component.literal("End Gateway Renderer Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int y = 45;

        addRenderableWidget(new FloatSlider(
                centerX - 120, y, 240, 20,
                "Global Size",
                0.50F, 2.00F,
                GatewayRenderConfig.GLOBAL_SCALE,
                value -> GatewayRenderConfig.GLOBAL_SCALE = value
        ));

        y += 25;
        addRenderableWidget(new FloatSlider(
                centerX - 120, y, 240, 20,
                "Helmet Size",
                0.50F, 2.00F,
                GatewayRenderConfig.HELMET_SCALE,
                value -> GatewayRenderConfig.HELMET_SCALE = value
        ));

        y += 25;
        addRenderableWidget(new FloatSlider(
                centerX - 120, y, 240, 20,
                "Chest Size",
                0.50F, 2.00F,
                GatewayRenderConfig.CHEST_SCALE,
                value -> GatewayRenderConfig.CHEST_SCALE = value
        ));

        y += 25;
        addRenderableWidget(new FloatSlider(
                centerX - 120, y, 240, 20,
                "Legs Size",
                0.50F, 2.00F,
                GatewayRenderConfig.LEGS_SCALE,
                value -> GatewayRenderConfig.LEGS_SCALE = value
        ));

        y += 25;
        addRenderableWidget(new FloatSlider(
                centerX - 120, y, 240, 20,
                "Boots Size",
                0.50F, 2.00F,
                GatewayRenderConfig.BOOTS_SCALE,
                value -> GatewayRenderConfig.BOOTS_SCALE = value
        ));

        y += 25;
        addRenderableWidget(new FloatSlider(
                centerX - 120, y, 240, 20,
                "Legs Up/Down",
                -0.50F, 0.25F,
                GatewayRenderConfig.LEGS_Y_OFFSET,
                value -> GatewayRenderConfig.LEGS_Y_OFFSET = value
        ));

        y += 35;
        addRenderableWidget(Button.builder(Component.literal("Reset"), button -> {
            GatewayRenderConfig.reset();
            this.minecraft.setScreen(new GatewayRendererConfigScreen(parent));
        }).bounds(centerX - 120, y, 115, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Done"), button -> {
            this.minecraft.setScreen(parent);
        }).bounds(centerX + 5, y, 115, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 18, 0xFFFFFF);
        graphics.drawCenteredString(this.font, Component.literal("Change End Gateway renderer size live in-game"), this.width / 2, 30, 0xAAAAAA);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }

    private static class FloatSlider extends AbstractSliderButton {
        private final String label;
        private final float min;
        private final float max;
        private final ValueSetter setter;

        public FloatSlider(int x, int y, int width, int height, String label, float min, float max, float current, ValueSetter setter) {
            super(x, y, width, height, Component.empty(), (current - min) / (max - min));
            this.label = label;
            this.min = min;
            this.max = max;
            this.setter = setter;
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            setMessage(Component.literal(label + ": " + String.format("%.2f", getRealValue())));
        }

        @Override
        protected void applyValue() {
            setter.set(getRealValue());
            updateMessage();
        }

        private float getRealValue() {
            return min + (float)this.value * (max - min);
        }
    }

    @FunctionalInterface
    private interface ValueSetter {
        void set(float value);
    }
}
