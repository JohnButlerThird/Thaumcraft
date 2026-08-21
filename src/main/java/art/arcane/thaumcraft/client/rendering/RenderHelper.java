package art.arcane.thaumcraft.client.rendering;

import art.arcane.thaumcraft.util.Colour;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import org.joml.Vector2f;
import org.joml.Vector3f;
import art.arcane.thaumcraft.Thaumcraft;
import org.joml.Vector4f;

public final class RenderHelper {

    public static TextureAtlasSprite getFluidSprite(Fluid b) {
        FluidModel fluid = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(b.defaultFluidState());
        return fluid.stillMaterial().sprite();
    }

    public static int getFluidTint(Fluid b) {
        FluidModel fluid = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(b.defaultFluidState());
        return fluid.fluidTintSource().color(b.defaultFluidState());
    }

    public static void drawOutlineFont(GuiGraphicsExtractor graphics, int x, int y, String text, Colour color, Colour outlineColor) {
        Font font =  Minecraft.getInstance().font;
        int bg = outlineColor.argb32(true);
        graphics.text(font, text, x + 1, y, bg, false);
        graphics.text(font, text, x - 1, y, bg, false);
        graphics.text(font, text, x, y + 1, bg, false);
        graphics.text(font, text, x, y - 1, bg, false);
        graphics.text(font, text, x, y, color.argb32(true), false);
    }

    public static void drawFace(Direction dir, VertexConsumer consumer, PoseStack.Pose modelMatrix, Vector3f min, Vector3f max, int colour, Vector4f uv, boolean applyLight, int light, boolean applyOverlay, int overlay) {
        switch(dir) {
            case UP -> {
                fillVertex(consumer, modelMatrix, max.x(), max.y(), min.z(), colour, uv.z(), uv.y(), applyLight, light, applyOverlay, overlay);
                fillVertex(consumer, modelMatrix, min.x(), max.y(), min.z(), colour, uv.x(), uv.y(), applyLight, light, applyOverlay, overlay);
                fillVertex(consumer, modelMatrix, min.x(), max.y(), max.z(), colour, uv.x(), uv.w(), applyLight, light, applyOverlay, overlay);
                fillVertex(consumer, modelMatrix, max.x(), max.y(), max.z(), colour, uv.z(), uv.w(), applyLight, light, applyOverlay, overlay);
            }
            case DOWN -> {
                fillVertex(consumer, modelMatrix, max.x(), min.y(), min.z(), colour, uv.z(), uv.y(), applyLight, light, applyOverlay, overlay);
                fillVertex(consumer, modelMatrix, max.x(), min.y(), max.z(), colour, uv.z(), uv.w(), applyLight, light, applyOverlay, overlay);
                fillVertex(consumer, modelMatrix, min.x(), min.y(), max.z(), colour, uv.x(), uv.w(), applyLight, light, applyOverlay, overlay);
                fillVertex(consumer, modelMatrix, min.x(), min.y(), min.z(), colour, uv.x(), uv.y(), applyLight, light, applyOverlay, overlay);
            }
            case NORTH -> {
                fillVertex(consumer, modelMatrix, max.x(), min.y(), min.z(), colour, uv.z(), uv.y(), applyLight, light, applyOverlay, overlay);
                fillVertex(consumer, modelMatrix, min.x(), min.y(), min.z(), colour, uv.x(), uv.y(), applyLight, light, applyOverlay, overlay);
                fillVertex(consumer, modelMatrix, min.x(), max.y(), min.z(), colour, uv.x(), uv.w(), applyLight, light, applyOverlay, overlay);
                fillVertex(consumer, modelMatrix, max.x(), max.y(), min.z(), colour, uv.z(), uv.w(), applyLight, light, applyOverlay, overlay);
            }
            case SOUTH -> {
                fillVertex(consumer, modelMatrix, max.x(), min.y(), max.z(), colour, uv.z(), uv.y(), applyLight, light, applyOverlay, overlay);
                fillVertex(consumer, modelMatrix, max.x(), max.y(), max.z(), colour, uv.z(), uv.w(), applyLight, light, applyOverlay, overlay);
                fillVertex(consumer, modelMatrix, min.x(), max.y(), max.z(), colour, uv.x(), uv.w(), applyLight, light, applyOverlay, overlay);
                fillVertex(consumer, modelMatrix, min.x(), min.y(), max.z(), colour, uv.x(), uv.y(), applyLight, light, applyOverlay, overlay);
            }
            case EAST -> {
                fillVertex(consumer, modelMatrix, max.x(), min.y(), max.z(), colour, uv.z(), uv.y(), applyLight, light, applyOverlay, overlay);
                fillVertex(consumer, modelMatrix, max.x(), min.y(), min.z(), colour, uv.x(), uv.y(), applyLight, light, applyOverlay, overlay);
                fillVertex(consumer, modelMatrix, max.x(), max.y(), min.z(), colour, uv.x(), uv.w(), applyLight, light, applyOverlay, overlay);
                fillVertex(consumer, modelMatrix, max.x(), max.y(), max.z(), colour, uv.z(), uv.w(), applyLight, light, applyOverlay, overlay);
            }
            case WEST -> {
                fillVertex(consumer, modelMatrix, min.x(), min.y(), max.z(), colour, uv.z(), uv.y(), applyLight, light, applyOverlay, overlay);
                fillVertex(consumer, modelMatrix, min.x(), max.y(), max.z(), colour, uv.z(), uv.w(), applyLight, light, applyOverlay, overlay);
                fillVertex(consumer, modelMatrix, min.x(), max.y(), min.z(), colour, uv.x(), uv.w(), applyLight, light, applyOverlay, overlay);
                fillVertex(consumer, modelMatrix, min.x(), min.y(), min.z(), colour, uv.x(), uv.y(), applyLight, light, applyOverlay, overlay);
            }
        }
    }

    public static void drawQuad(VertexConsumer consumer, PoseStack.Pose modelMatrix, Vector2f min, Vector2f max, int colour, float minU, float minV, float maxU, float maxV, boolean applyLight, int light, boolean applyOverlay, int overlay) {
        fillVertex(consumer, modelMatrix, min.x(), min.y(), 0, colour, minU, maxV, applyLight, light, applyOverlay, overlay);
        fillVertex(consumer, modelMatrix, max.x(), min.y(), 0, colour, maxU, maxV, applyLight, light, applyOverlay, overlay);
        fillVertex(consumer, modelMatrix, max.x(), max.y(), 0, colour, maxU, minV, applyLight, light, applyOverlay, overlay);
        fillVertex(consumer, modelMatrix, min.x(), max.y(), 0, colour, minU, minV, applyLight, light, applyOverlay, overlay);
    }

    public static void drawQuadCentered(VertexConsumer consumer, PoseStack.Pose modelMatrix, Vector2f position, float width, float height, int colour, float minU, float minV, float maxU, float maxV, boolean applyLight, int light, boolean applyOverlay, int overlay) {
        Vector2f min = new Vector2f(position).sub(width / 2, height / 2);
        Vector2f max = new Vector2f(position).add(width / 2, height / 2);
        drawQuad(consumer, modelMatrix, min, max, colour, minU, minV, maxU, maxV, applyLight, light, applyOverlay, overlay);
    }

    public static boolean debugIsLookingAtBlock(BlockPos pos) {
        if(Thaumcraft.isDev() && Minecraft.getInstance().player.isCrouching() && Minecraft.getInstance().hitResult instanceof BlockHitResult hit) {
            return hit.getBlockPos().equals(pos);
        }
        return false;
    }

    private static void fillVertex(VertexConsumer consumer, PoseStack.Pose modelMatrix, float x, float y, float z, int colour, float u, float v, boolean applyLight, int packedLight, boolean applyOverlay, int overlay) {
        consumer.addVertex(modelMatrix, x, y, z).setColor(colour).setUv(u, v);
        if(applyOverlay) {
            consumer.setOverlay(overlay);
        }
        if(applyLight) {
            consumer.setLight(packedLight);
        }
        consumer.setNormal(0, 0, 1);
    }
}
