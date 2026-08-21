package art.arcane.thaumcraft.client.rendering.ber;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.util.simple.SimpleBER;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import art.arcane.thaumcraft.blocks.entities.CrucibleBlockEntity;
import art.arcane.thaumcraft.client.rendering.RenderHelper;
import art.arcane.thaumcraft.util.FluidHelper;
import org.joml.Vector4f;

public class CrucibleBER extends SimpleBER<CrucibleBlockEntity, CrucibleBER.RenderState> {

    private static final float FLUID_START = 1 / 16F * 4;
    private static final float FLUID_HEIGHT = 1 / 16F * 9;
    private static final float ASPECT_HEIGHT = 1 / 16F * 3;

    public CrucibleBER() {
        super(RenderState::new);
    }

    @Override
    public void extractRenderState(CrucibleBlockEntity blockEntity, RenderState state, float partialTicks, Vec3 cameraPosition) {
        state.fillHeight = blockEntity.getFluidPercentage();
        state.aspectPercentage = blockEntity.getAspectPercentage();
        state.fluid = blockEntity.getResource(0).getFluid();
        state.isHot = blockEntity.isCooking();
        if(Thaumcraft.isDev())
            state.debugString = FluidHelper.serializeTankStatus(blockEntity);
    }

    @Override
    public void submit(RenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if(RenderHelper.debugIsLookingAtBlock(renderState.blockPos)) {
            /*submitNametag(poseStack, submitNodeCollector, renderState, cameraRenderState, Component.literal(renderState.isHot ? "Hot" : "Cool"));
            submitNametag(poseStack, submitNodeCollector, renderState, cameraRenderState, Component.literal(renderState.debugString));*/
        }

        if(renderState.fillHeight > 0) {
            poseStack.pushPose();
            TextureAtlasSprite sprite = RenderHelper.getFluidSprite(renderState.fluid);
            poseStack.translate(0, FLUID_START + (FLUID_HEIGHT * renderState.fillHeight) + (ASPECT_HEIGHT * renderState.aspectPercentage) + (renderState.fillHeight + renderState.aspectPercentage >= 2 ? 0.0001 : 0), 0);
            submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucent(sprite.atlasLocation()), (pose, buffer) -> {
                RenderHelper.drawFace(Direction.UP, buffer, pose, new Vector3f(0, 0, 0), new Vector3f(1, 0, 1), RenderHelper.getFluidTint(renderState.fluid),
                        new Vector4f(sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1()), true, renderState.lightCoords, true, OverlayTexture.NO_OVERLAY);
            });
            poseStack.popPose();
        }
    }

    public static class RenderState extends BlockEntityRenderState {
        private float fillHeight, aspectPercentage;
        private Fluid fluid;
        private boolean isHot;
        private String debugString;
    }
}
