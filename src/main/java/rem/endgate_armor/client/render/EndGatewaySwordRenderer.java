package rem.endgate_armor.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.resources.ResourceLocation;
import java.io.InputStream;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

/** Reads the real vanilla diamond sword sprite alpha, then renders that exact outline with the gateway shader. */
public final class EndGatewaySwordRenderer extends BlockEntityWithoutLevelRenderer {
    private static final ResourceLocation VANILLA_SWORD = new ResourceLocation("minecraft", "textures/item/diamond_sword.png");
    private boolean[][] vanillaShape;
    private boolean attemptedLoad;
    private static final float DEPTH = 0.025F;

    public EndGatewaySwordRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack,
                             MultiBufferSource buffers, int packedLight, int packedOverlay) {
        loadVanillaShape();
        poseStack.pushPose();
        // Vanilla generated item models occupy -0.5..+0.5 on X/Y.
        // The sprite quads below occupy 0..1, so center them before the
        // vanilla handheld display transforms are applied.
        // Keep the original hand transforms; use the native 0..1 item space for inventory/hotbar.
        if (context != ItemDisplayContext.GUI) {
            poseStack.translate(0.3D, -0.0D, 0.5D);
        }
        VertexConsumer vertices = buffers.getBuffer(RenderType.endGateway());
        Matrix4f matrix = poseStack.last().pose();
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                if (!solid(x, y)) continue;
                float left = x / 16.0F, right = (x + 1) / 16.0F;
                float top = 1.0F - y / 16.0F, bottom = 1.0F - (y + 1) / 16.0F;
                quad(vertices, matrix, left, bottom, DEPTH, right, bottom, DEPTH,
                        right, top, DEPTH, left, top, DEPTH);
                quad(vertices, matrix, right, bottom, -DEPTH, left, bottom, -DEPTH,
                        left, top, -DEPTH, right, top, -DEPTH);
                if (!solid(x - 1, y)) quad(vertices, matrix, left, bottom, -DEPTH, left, bottom, DEPTH,
                        left, top, DEPTH, left, top, -DEPTH);
                if (!solid(x + 1, y)) quad(vertices, matrix, right, bottom, DEPTH, right, bottom, -DEPTH,
                        right, top, -DEPTH, right, top, DEPTH);
                if (!solid(x, y - 1)) quad(vertices, matrix, left, top, DEPTH, right, top, DEPTH,
                        right, top, -DEPTH, left, top, -DEPTH);
                if (!solid(x, y + 1)) quad(vertices, matrix, left, bottom, -DEPTH, right, bottom, -DEPTH,
                        right, bottom, DEPTH, left, bottom, DEPTH);
            }
        }
        poseStack.popPose();
    }

    /** Use Minecraft's own sprite rather than guessing a sword-shaped pixel mask. */
    private void loadVanillaShape() {
        if (attemptedLoad) return;
        attemptedLoad = true;
        Minecraft.getInstance().getResourceManager().getResource(VANILLA_SWORD).ifPresent(resource -> {
            try (InputStream input = resource.open(); NativeImage image = NativeImage.read(input)) {
                if (image.getWidth() != 16 || image.getHeight() != 16) return;
                boolean[][] shape = new boolean[16][16];
                for (int y = 0; y < 16; y++) {
                    for (int x = 0; x < 16; x++) {
                        shape[y][x] = ((image.getPixelRGBA(x, y) >>> 24) & 0xFF) != 0;
                    }
                }
                vanillaShape = shape;
            } catch (Exception exception) {
                // Resource packs may omit the vanilla file; avoid crashing the renderer.
                vanillaShape = null;
            }
        });
    }

    private boolean solid(int x, int y) {
        return vanillaShape != null && x >= 0 && x < 16 && y >= 0 && y < 16 && vanillaShape[y][x];
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
