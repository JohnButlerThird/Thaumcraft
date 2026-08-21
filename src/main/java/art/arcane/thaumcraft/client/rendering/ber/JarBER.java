package art.arcane.thaumcraft.client.rendering.ber;

import art.arcane.thaumcraft.client.rendering.CuboidRenderer;
import art.arcane.thaumcraft.util.simple.SimpleBER;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.blocks.entities.JarBlockEntity;
import art.arcane.thaumcraft.client.rendering.AspectRenderer;
import art.arcane.thaumcraft.client.rendering.RenderHelper;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;
import art.arcane.thaumcraft.util.Colour;
import art.arcane.thaumcraft.util.MathUtils;

public class JarBER extends SimpleBER<JarBlockEntity, JarBER.RenderState> {

    private static final float FLUID_HEIGHT = MathUtils.px(10);
    private static final float FLUID_WIDTH = MathUtils.px(8);
    private static final Identifier FILLED_TEXTURE = Thaumcraft.id("block/misc/essentia_fluid");
    private static final Identifier LABEL = Thaumcraft.id("block/misc/label");
    private static final Colour LABEL_COLOUR = Colour.fromARGB(0.8F, 0.1F, 0.1F, 0.1F);

    public JarBER() {
        super(RenderState::new);
    }

    @Override
    protected void extractRenderState(JarBlockEntity blockEntity, RenderState state, float partialTicks, Vec3 cameraPosition) {
        if(blockEntity.getCurrentAspect() != null) {
            state.aspect = AspectRenderer.getRenderData(ConfigDataRegistries.ASPECTS.getHolder(blockEntity.getLevel().registryAccess(), blockEntity.getCurrentAspect()), false, false);
            state.fillPercent = blockEntity.getFillPercent();
        }
        if(blockEntity.getLabel() != null) {
            state.label = AspectRenderer.getRenderData(ConfigDataRegistries.ASPECTS.getHolder(blockEntity.getLevel().registryAccess(), blockEntity.getLabel()), false, false);
            state.labelDirection = getRotation(blockEntity.getLabelDirection());
        }
    }

    @Override
    public void submit(RenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if(RenderHelper.debugIsLookingAtBlock(state.blockPos) && state.aspect != null) {
            poseStack.pushPose();
            poseStack.translate(state.blockPos.getX() + 0.5F, state.blockPos.getY() + 1.5F, state.blockPos.getZ() + 0.5F);
            float distance = (float)(camera.pos.distanceToSqr(state.blockPos.above().getCenter()));
            submitNametag(poseStack, submitNodeCollector, state, camera, state.aspect.name(), distance);
            submitNametag(poseStack, submitNodeCollector, state, camera, Component.literal(((int)(250 * state.fillPercent)) + " / 250 [" + String.format("%.0f", state.fillPercent * 100) + "%]"), distance);
            poseStack.popPose();
        }

        if(state.aspect != null)
            renderEssentia(poseStack, submitNodeCollector, state);
        if(state.label != null)
            renderLabel(poseStack, submitNodeCollector, state);
    }

    private void renderEssentia(PoseStack pPoseStack, SubmitNodeCollector nodeCollector, RenderState state) {
        pPoseStack.pushPose();
        pPoseStack.translate(MathUtils.px(4), MathUtils.px(1), MathUtils.px(4));

        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasManager().get(new SpriteId(TextureAtlas.LOCATION_BLOCKS, FILLED_TEXTURE));
        nodeCollector.submitCustomGeometry(pPoseStack, Sheets.cutoutBlockSheet(), (pose, buffer) -> {
            new CuboidRenderer(FLUID_WIDTH, FLUID_HEIGHT * state.fillPercent, FLUID_WIDTH, 16, 16, sprite)
                    .setAxisUVs(Direction.Axis.X, 4, 0, 12, 10 * state.fillPercent)
                    .setAxisUVs(Direction.Axis.Z, 4, 0, 12, 10 * state.fillPercent)
                    .setAxisUVs(Direction.Axis.Y, 4, 4, 12, 12)
                    .draw(buffer, pose, state.aspect.color(), true, state.lightCoords, true, OverlayTexture.NO_OVERLAY);
        });

        pPoseStack.popPose();
    }

    private void renderLabel(PoseStack pPoseStack, SubmitNodeCollector  nodeCollector, RenderState state) {
        pPoseStack.pushPose();

        pPoseStack.translate(MathUtils.px(8), MathUtils.px(0), MathUtils.px(8));
        pPoseStack.mulPose(state.labelDirection);
        pPoseStack.translate(MathUtils.px(0), MathUtils.px(6F), MathUtils.px(5.1F));
        float randomRotation = (state.label.texture().hashCode() + state.blockPos.getX() + state.labelDirection.y()) % 4 - 2;
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(randomRotation));

        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasManager().get(new SpriteId(TextureAtlas.LOCATION_BLOCKS, LABEL));
        nodeCollector.submitCustomGeometry(pPoseStack, RenderTypes.entityCutout(TextureAtlas.LOCATION_BLOCKS), (pose, buffer) -> {
            RenderHelper.drawQuadCentered(
                    buffer, pose,
                    new Vector2f(0, 0), MathUtils.px(7), MathUtils.px(7),
                    0xFFFFFFFF, sprite.getU(0), sprite.getV(0), sprite.getU(1), sprite.getV(1), true, state.lightCoords, true, OverlayTexture.NO_OVERLAY);
        });

        pPoseStack.scale(0.67F, 0.67F, 1);
        pPoseStack.translate(0, 0, MathUtils.px(0.01F));

        nodeCollector.submitCustomGeometry(pPoseStack, RenderTypes.entityTranslucent(state.label.texture()), (pose, buffer) -> {
            RenderHelper.drawQuadCentered(
                    buffer, pose,
                    new Vector2f(0, 0), MathUtils.px(7), MathUtils.px(7),
                    LABEL_COLOUR.argb32(true), 0, 0, 1, 1, true, state.lightCoords, true, OverlayTexture.NO_OVERLAY);
        });

        pPoseStack.popPose();
    }

    private Quaternionf getRotation(Direction direction) {
        int angle = switch (direction) {
            case NORTH -> 180;
            case WEST -> 270;
            case EAST -> 90;
            default -> 0;
        };
        return Axis.YP.rotationDegrees(angle);
    }

    public static final class RenderState extends BlockEntityRenderState {
        private AspectRenderer.RenderData aspect, label;
        private float fillPercent;
        private Quaternionf labelDirection;
    }
}
