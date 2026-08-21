package art.arcane.thaumcraft.client.rendering.ber;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.client.rendering.RenderHelper;
import art.arcane.thaumcraft.util.Colour;
import art.arcane.thaumcraft.util.simple.SimpleBER;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import art.arcane.thaumcraft.blocks.DioptraBlock;
import art.arcane.thaumcraft.blocks.entities.DioptraBlockEntity;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Arrays;

public class DioptraBER extends SimpleBER<DioptraBlockEntity, DioptraBER.RenderState> {

    private static final int GRID_SIZE = DioptraBlockEntity.GRID_SIZE;
    private static final int SQUARE_COUNT = 12;
    private static final float SQUARE_LENGTH = 0.975F / 12;
    private static final float POINT_MAX_HEIGHT = 10F / 16; // 10px

    private static final int COLOUR_VIS = Colour.fromRGB(80, 140, 255).argb32(true);
    private static final int COLOUR_FLUX = Colour.fromRGB(200, 60, 220).argb32(true);

    private static final Identifier SQUARE_TEXTURE = Thaumcraft.id("textures/block/dioptra_grid.png");
    private static final Identifier FRAME_TEXTURE = Thaumcraft.id("textures/block/dioptra_frame.png");

    public DioptraBER() {
        super(RenderState::new);
    }

    @Override
    protected void extractRenderState(DioptraBlockEntity blockEntity, RenderState state, float partialTicks, Vec3 cameraPosition) {
        state.isVisView = blockEntity.getBlockState().getValue(DioptraBlock.DISPLAY_VIS);
        state.pointHeight = new float[GRID_SIZE][GRID_SIZE];
        for(int x = 0; x < GRID_SIZE; x++)
            for(int z = 0; z < GRID_SIZE; z++) {
                state.pointHeight[x][z] = blockEntity.getFillData(x, z, state.isVisView);
            }

    }

    @Override
    public void submit(RenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(.0125F + SQUARE_LENGTH / 2, 1.0125F, .0125F + SQUARE_LENGTH / 2);
        renderHeightMap(poseStack, submitNodeCollector, state);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(.5F, 1, .5F);
        renderFrame(poseStack, submitNodeCollector, state);
        poseStack.popPose();
    }

    private void renderHeightMap(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, RenderState state) {
        for(int x = 0; x < SQUARE_COUNT; x++) {
            for (int z = 0; z < SQUARE_COUNT; z++) {
                poseStack.pushPose();
                poseStack.translate(x * SQUARE_LENGTH, 0,  z * SQUARE_LENGTH);
                renderSquareFace(poseStack, submitNodeCollector, state,
                        state.pointHeight[x][z],
                        state.pointHeight[x + 1][z],
                        state.pointHeight[x][z + 1],
                        state.pointHeight[x + 1][z + 1]);
                poseStack.popPose();
            }
        }
    }

    private void renderSquareFace(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, RenderState state, float bl, float br, float tl, float tr) {
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucentEmissive(SQUARE_TEXTURE), ((pose, buffer) ->{
            float offest = SQUARE_LENGTH / 2;
            buffer.addVertex(pose, -offest, POINT_MAX_HEIGHT * tl, offest).setColor(state.isVisView ? COLOUR_VIS : COLOUR_FLUX).setUv(0, 1).setLight(state.lightCoords).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0, 1, 0);
            buffer.addVertex(pose, -offest, POINT_MAX_HEIGHT * bl, -offest).setColor(state.isVisView ? COLOUR_VIS : COLOUR_FLUX).setUv(0, 0).setLight(state.lightCoords).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0, 1, 0);
            buffer.addVertex(pose, offest, POINT_MAX_HEIGHT * br, -offest).setColor(state.isVisView ? COLOUR_VIS : COLOUR_FLUX).setUv(1, 0).setLight(state.lightCoords).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0, 1, 0);
            buffer.addVertex(pose, offest, POINT_MAX_HEIGHT * tr, offest).setColor(state.isVisView ? COLOUR_VIS : COLOUR_FLUX).setUv(1, 1).setLight(state.lightCoords).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0, 1, 0);
        }));
    }

    private void renderFrame(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, RenderState state) {
        submitNodeCollector.submitCustomGeometry(poseStack,  RenderTypes.entityTranslucentEmissive(FRAME_TEXTURE), ((pose, buffer) -> {
            RenderHelper.drawFace(Direction.NORTH, buffer, pose, new Vector3f(-.5F, 0, .5F), new Vector3f(.5F, 1, .5F), state.isVisView ? COLOUR_VIS : COLOUR_FLUX, new Vector4f(0, 0, 1, 1), true, state.lightCoords, true, OverlayTexture.NO_OVERLAY);
            RenderHelper.drawFace(Direction.SOUTH, buffer, pose, new Vector3f(-.5F, 0, -.5F), new Vector3f(.5F, 1, -.5F), state.isVisView ? COLOUR_VIS : COLOUR_FLUX, new Vector4f(0, 0, 1, 1), true, state.lightCoords, true, OverlayTexture.NO_OVERLAY);
            RenderHelper.drawFace(Direction.EAST, buffer, pose, new Vector3f(.5F, 0, -.5F), new Vector3f(.5F, 1, .5F), state.isVisView ? COLOUR_VIS : COLOUR_FLUX, new Vector4f(0, 0, 1, 1), true, state.lightCoords, true, OverlayTexture.NO_OVERLAY);
            RenderHelper.drawFace(Direction.WEST, buffer, pose, new Vector3f(-.5F, 0, -.5F), new Vector3f(-.5F, 1, .5F), state.isVisView ? COLOUR_VIS : COLOUR_FLUX, new Vector4f(0, 0, 1, 1), true, state.lightCoords, true, OverlayTexture.NO_OVERLAY);
        }));
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

    public static final class RenderState extends BlockEntityRenderState {
        private float[][] pointHeight;
        private boolean isVisView;
    }
}
