package art.arcane.thaumcraft.client.rendering;

import art.arcane.thaumcraft.util.Colour;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;

public final class AspectRenderer {

    public static void renderAspectGui(GuiGraphicsExtractor graphics, ResourceKey<Aspect> aspect, int x, int y, int size, int amount, boolean sdf) {
        Holder<Aspect> fromReg = ConfigDataRegistries.ASPECTS.getHolder(Minecraft.getInstance().getConnection().registryAccess(), aspect);
        renderAspectGui(graphics, fromReg, x, y, size, amount, sdf);
    }

    public static void renderAspectGui(GuiGraphicsExtractor graphics, Holder<Aspect> aspect, int x, int y, int size, int amount, boolean sdf) {
        AspectRenderer.RenderData data = getRenderData(aspect, false, false);
        graphics.blit(RenderPipelines.GUI_TEXTURED, data.texture(), x, y, 0, 0,  size, size, 16, 16, data.color());

        if(amount > 1) {
            var stack = graphics.pose();
            stack.pushMatrix();
            stack.scale(.5F, .5F);
            String text = String.valueOf(amount);
            int xOffset = size - Minecraft.getInstance().font.width(text) / 2;
            int yOffset = size - Minecraft.getInstance().font.lineHeight / 2;
            RenderHelper.drawOutlineFont(graphics, (x + xOffset) * 2, (y + yOffset) * 2, text, Colour.WHITE, Colour.BLACK);
            stack.popMatrix();
        }
    }

    public static AspectRenderer.RenderData getRenderData(Holder<Aspect> aspect, boolean pureColorName, boolean primalColorName) {
        Identifier texture = aspect.getKey().identifier().withPrefix("textures/aspects/").withSuffix(".png");
        int color = aspect.value().colour().argb32(true);
        Component name = Aspect.getName(aspect, pureColorName, primalColorName);
        return new AspectRenderer.RenderData(texture, color, name);
    }

    public record RenderData(Identifier texture, int color, Component name) { }
}
