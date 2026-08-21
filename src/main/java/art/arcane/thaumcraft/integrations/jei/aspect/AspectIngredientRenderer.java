package art.arcane.thaumcraft.integrations.jei.aspect;

import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.client.rendering.AspectRenderer;
import art.arcane.thaumcraft.data.aspects.AspectList;

import java.util.List;

public class AspectIngredientRenderer implements IIngredientRenderer<AspectList> {

    @Override
    public void render(GuiGraphicsExtractor guiGraphics, AspectList ingredient) {
        var aspect = ingredient.aspectsPresent().get(0);
        var amount = ingredient.getAspect(aspect);
        AspectRenderer.renderAspectGui(guiGraphics, aspect, 0, 0, 16, 1, false);
        if (amount > 1) {
            guiGraphics.pose().translate(12, 10);
            guiGraphics.text(Minecraft.getInstance().font, String.valueOf(amount), 0, 0, 0xFFFFFF);
        }
    }

    @Override
    public List<Component> getTooltip(AspectList ingredient, TooltipFlag tooltipFlag) {
        return List.of(Aspect.getName(Minecraft.getInstance().getConnection().registryAccess(), ingredient.aspectsPresent().get(0), false, false));
    }
}
