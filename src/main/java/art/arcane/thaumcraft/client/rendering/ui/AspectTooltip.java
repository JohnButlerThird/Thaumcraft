package art.arcane.thaumcraft.client.rendering.ui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import art.arcane.thaumcraft.client.rendering.AspectRenderer;
import art.arcane.thaumcraft.data.aspects.AspectList;

public class AspectTooltip implements ClientTooltipComponent {

    private static final int SIZE = 16;
    private static final int SPACING = 2;

    private final AspectList aspects;

    public AspectTooltip(AspectList list) {
        this.aspects = list;
    }

    @Override
    public int getHeight(Font font) {
        return SIZE + 3;
    }


    @Override
    public int getWidth(Font pFont) {
        return SIZE * aspects.aspectCount() + SPACING * Math.max(aspects.aspectCount() - 1, 0);
    }

    @Override
    public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor graphics) {
        aspects.indexedForEach((aspect, amount, index) -> {
            int offset = index * (SIZE + SPACING);
            AspectRenderer.renderAspectGui(graphics, aspect, x + offset, y + 1, SIZE, amount, false);
        });
    }



    public record Data(AspectList aspects) implements TooltipComponent { }
}
