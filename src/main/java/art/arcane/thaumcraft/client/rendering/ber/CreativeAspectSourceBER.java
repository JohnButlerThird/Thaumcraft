package art.arcane.thaumcraft.client.rendering.ber;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.aspects.AspectRegistry;
import art.arcane.thaumcraft.util.simple.SimpleBER;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.blocks.entities.CreativeAspectSourceBlockEntity;
import art.arcane.thaumcraft.client.rendering.AspectRenderer;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;

public class CreativeAspectSourceBER extends SimpleBER<CreativeAspectSourceBlockEntity, CreativeAspectSourceBER.RenderState> {

    public CreativeAspectSourceBER() {
        super(RenderState::new);
    }

    @Override
    protected void extractRenderState(CreativeAspectSourceBlockEntity blockEntity, RenderState state, float partialTicks, Vec3 cameraPosition) {
        if(blockEntity.getAspect() != ThaumcraftData.Aspects.UNKNOWN) {
           AspectRenderer.RenderData data = AspectRenderer.getRenderData(ConfigDataRegistries.ASPECTS.getHolder(blockEntity.getLevel().registryAccess(), blockEntity.getAspect()), false, false);
            state.aspectTexture = data.texture();
            state.aspectColour = data.color();
        } else {
            state.aspectTexture = null;
            state.aspectColour = 0xFFFFFFFF;
        }
    }

    @Override
    public void submit(RenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if(state.aspectTexture != null) {
            poseStack.pushPose();
            poseStack.translate(.5F, .5F, .5F);
            for(Direction dir : Direction.values()) {
                if(dir.getAxis() == Direction.Axis.Y) {
                    continue;
                }
                int axis = dir.getAxisDirection().getStep();
                poseStack.pushPose();
                if(dir.getAxis() == Direction.Axis.X) {
                    poseStack.translate((.5F + (1 / 16F / 8)) * (axis * -1), 0, 0);
                    poseStack.mulPose(new Quaternionf().fromAxisAngleDeg(0, 1, 0, 90 * axis));
                } else {
                    poseStack.translate(0, 0, (.5F + (1 / 16F / 8)) * axis);
                    poseStack.mulPose(new Quaternionf().fromAxisAngleDeg(0, 1, 0, dir == Direction.NORTH ? 0 : 180));
                }
                poseStack.scale(.5F, .5F, .5F);
                submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucentEmissive(state.aspectTexture), (pose, buffer) -> {
                    buffer.addVertex(pose, (float)0.5, (float)-0.5, (float) 0).setColor(state.aspectColour).setUv(0, 1).setLight(state.lightCoords).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(pose, dir.getUnitVec3f());
                    buffer.addVertex(pose, (float)-0.5, (float)-0.5, (float) 0).setColor(state.aspectColour).setUv(1, 1).setLight(state.lightCoords).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(pose, dir.getUnitVec3f());
                    buffer.addVertex(pose, (float)-0.5, (float)0.5, (float) 0).setColor(state.aspectColour).setUv(1, 0).setLight(state.lightCoords).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(pose, dir.getUnitVec3f());
                    buffer.addVertex(pose, (float)0.5, (float)0.5, (float) 0).setColor(state.aspectColour).setUv(0, 0).setLight(state.lightCoords).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(pose, dir.getUnitVec3f());
                });
                poseStack.popPose();
            }
            poseStack.popPose();
        }
    }

    public static final class RenderState extends BlockEntityRenderState {
        private Identifier aspectTexture;
        private int aspectColour;
    }
}
