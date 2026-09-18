package rem.endgate_armor.client.render;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import rem.endgate_armor.registry.ModItems;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/** Draws each armor item's existing vanilla-shaped icon silhouette with the real End Gateway shader. */
public final class EndGatewayArmorIconRenderer extends BlockEntityWithoutLevelRenderer {
    private final Map<String, boolean[][]> masks = new HashMap<>();
    private static final float DEPTH = 0.0125F;

    public EndGatewayArmorIconRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack,
                             MultiBufferSource buffers, int packedLight, int packedOverlay) {
        String piece;
        if (stack.is(ModItems.ENDGATE_HELMET.get())) piece = "helmet";
        else if (stack.is(ModItems.ENDGATE_CHESTPLATE.get())) piece = "chestplate";
        else if (stack.is(ModItems.ENDGATE_LEGGINGS.get())) piece = "leggings";
        else if (stack.is(ModItems.ENDGATE_BOOTS.get())) piece = "boots";
        else return;

        boolean[][] mask = masks.computeIfAbsent(piece, this::loadMask);
        if (mask == null) return;
        poseStack.pushPose();
        // Vanilla generated-item geometry is centered on the model pivot.
        // Our shader silhouette is authored from (0,0) to (1,1), so center it
        // in either hand; do not add a two-block vertical offset.
        if (context == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND
                || context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND
                || context == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                || context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND) {
            poseStack.translate(-0.0D, 0.0D, 0.5D);
        }
        VertexConsumer vertices = buffers.getBuffer(RenderType.endGateway());
        Matrix4f matrix = poseStack.last().pose();
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                if (!solid(mask, x, y)) continue;
                float left = x / 16.0F, right = (x + 1) / 16.0F;
                float top = 1.0F - y / 16.0F, bottom = 1.0F - (y + 1) / 16.0F;
                quad(vertices, matrix, left, bottom, DEPTH, right, bottom, DEPTH,
                        right, top, DEPTH, left, top, DEPTH);
                quad(vertices, matrix, right, bottom, -DEPTH, left, bottom, -DEPTH,
                        left, top, -DEPTH, right, top, -DEPTH);
                if (!solid(mask, x - 1, y)) quad(vertices, matrix, left, bottom, -DEPTH, left, bottom, DEPTH,
                        left, top, DEPTH, left, top, -DEPTH);
                if (!solid(mask, x + 1, y)) quad(vertices, matrix, right, bottom, DEPTH, right, bottom, -DEPTH,
                        right, top, -DEPTH, right, top, DEPTH);
                if (!solid(mask, x, y - 1)) quad(vertices, matrix, left, top, DEPTH, right, top, DEPTH,
                        right, top, -DEPTH, left, top, -DEPTH);
                if (!solid(mask, x, y + 1)) quad(vertices, matrix, left, bottom, -DEPTH, right, bottom, -DEPTH,
                        right, bottom, DEPTH, left, bottom, DEPTH);
            }
        }
        poseStack.popPose();
    }

    private boolean[][] loadMask(String piece) {
        ResourceLocation texture = piece.equals("boots")
                ? new ResourceLocation("minecraft", "textures/item/diamond_boots.png")
                : new ResourceLocation("endgate_armor", "textures/item/endgate_" + piece + ".png");
        try {
            var resource = Minecraft.getInstance().getResourceManager().getResource(texture);
            if (resource.isEmpty()) return null;
            try (InputStream input = resource.get().open(); NativeImage image = NativeImage.read(input)) {
                if (image.getWidth() != 16 || image.getHeight() != 16) return null;
                boolean[][] result = new boolean[16][16];
                for (int y = 0; y < 16; y++) {
                    for (int x = 0; x < 16; x++) {
                        result[y][x] = ((image.getPixelRGBA(x, y) >>> 24) & 255) != 0;
                    }
                }
                return result;
            }
        } catch (Exception ignored) {
            return null;
        }
    }

    private static boolean solid(boolean[][] mask, int x, int y) {
        return mask != null && x >= 0 && x < 16 && y >= 0 && y < 16 && mask[y][x];
    }

    private static void quad(VertexConsumer v, Matrix4f m,
                             float x1, float y1, float z1, float x2, float y2, float z2,
                             float x3, float y3, float z3, float x4, float y4, float z4) {
        v.vertex(m, x1, y1, z1).endVertex();
        v.vertex(m, x2, y2, z2).endVertex();
        v.vertex(m, x3, y3, z3).endVertex();
        v.vertex(m, x4, y4, z4).endVertex();
    }
}
