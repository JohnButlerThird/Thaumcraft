package art.arcane.thaumcraft.client.rendering.ber;

import art.arcane.thaumcraft.util.simple.SimpleBER;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;
import art.arcane.thaumcraft.blocks.entities.RunicMatrixBlockEntity;
import art.arcane.thaumcraft.client.rendering.models.blocks.RunicMatrixModel;
import art.arcane.thaumcraft.registries.client.ConfigModelLayers;

public class RunicMatrixBER extends SimpleBER<RunicMatrixBlockEntity, RunicMatrixBER.RenderState> {

    private final RunicMatrixModel model;

    public RunicMatrixBER(BlockEntityRendererProvider.Context context) {
        super(RenderState::new);
        this.model = new RunicMatrixModel(context.bakeLayer(ConfigModelLayers.RUNIC_MATRIX));
    }

    @Override
    protected void extractRenderState(RunicMatrixBlockEntity blockEntity, RenderState state, float partialTicks, Vec3 cameraPosition) {
        state.activateTime = blockEntity.getAnimationHandler().getActivateAnimation(partialTicks);
        state.idleRotation = blockEntity.getAnimationHandler().getIdleRotation(partialTicks);
    }

    @Override
    public void submit(RenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();

        poseStack.translate(.5F, .5F, .5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.idleRotation));
        poseStack.mulPose(Axis.XP.rotationDegrees(35F * state.activateTime));
        poseStack.mulPose(Axis.ZP.rotationDegrees(45F * state.activateTime));
        poseStack.translate(-.5F, -.5F, -.5F);

        submitNodeCollector.submitModel(this.model, state, poseStack, RunicMatrixBlockEntity.AltarTier.ARCANE.getTexture(), state.lightCoords, OverlayTexture.NO_OVERLAY, 0, state.breakProgress);

        poseStack.popPose();
    }

    public static class RenderState extends BlockEntityRenderState {
        private float activateTime, idleRotation;
    }
}
