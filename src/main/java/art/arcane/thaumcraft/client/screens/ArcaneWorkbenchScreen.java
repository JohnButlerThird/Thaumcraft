package art.arcane.thaumcraft.client.screens;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.phys.Vec2;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.menus.ArcaneWorkbenchMenu;
import art.arcane.thaumcraft.util.BitPacker;

import java.util.List;

public class ArcaneWorkbenchScreen extends AbstractContainerScreen<ArcaneWorkbenchMenu> {

    private static final Identifier TEXTURE = Thaumcraft.id("textures/ui/arcane_workbench.png");

    private static final List<Vec2> RADIAL_POS = List.of(
            new Vec2(65, 89), new Vec2(113, 9), new Vec2(113, 67),
            new Vec2(65, -13), new Vec2(18, 9), new Vec2(18, 67));

    public ArcaneWorkbenchScreen(ArcaneWorkbenchMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        extractBackground(graphics, mouseX, mouseY, a);
        var pPoseStack = graphics.pose();
        BitPacker.readFlags(menu.getData().get(ArcaneWorkbenchMenu.DATA_ACTIVE_CRYSTALS), Aspect.Primal.class, BitPacker.Length.BYTE).forEach(p -> {
            pPoseStack.pushMatrix();
            Vec2 pos = RADIAL_POS.get(p.ordinal());
            pPoseStack.scale(.5F, .5F);
            graphics.blit(RenderPipelines.GUI, TEXTURE, (int)pos.x, (int)pos.y, 256 - 64, 0, 64, 64, 256, 256);
            pPoseStack.popMatrix();
        });

        int requiredVis = menu.getData().get(ArcaneWorkbenchMenu.DATA_REQUIRED_VIS);
        if(requiredVis > -1) {
            pPoseStack.pushMatrix();
            int xBase = (this.width - 190) / 2 + 168;
            int yBase = (this.height - 234) / 2 + 46;
            pPoseStack.translate(xBase, yBase);
            pPoseStack.scale(.5F, .5F);
            graphics.centeredText(font, "145 available", 0, 0, TextColor.fromRgb(0x6E6EEE).getValue());
            pPoseStack.popMatrix();
        }
        super.extractRenderState(graphics, mouseX, mouseY, a);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) { }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.blit(RenderPipelines.GUI, TEXTURE, width / 2 - 95, height / 2 - 117, 0, 0, 192, 256, 256, 256);
    }
}
