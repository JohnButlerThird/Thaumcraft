package art.arcane.thaumcraft.util.simple;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import lombok.AllArgsConstructor;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

@AllArgsConstructor
public abstract class SimpleBER<T extends BlockEntity, S extends BlockEntityRenderState> implements BlockEntityRenderer<T, S> {

	private final Supplier<S> stateFactory;

	@Override
	public S createRenderState() {
		return this.stateFactory.get();
	}

	@Override
	public void extractRenderState(T blockEntity, S state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		extractRenderState(blockEntity, state, partialTicks, cameraPosition);
	}

	protected abstract void extractRenderState(T blockEntity, S state, float partialTicks, Vec3 cameraPosition);

	protected void submitNametag(PoseStack stack, SubmitNodeCollector nodeCollection, S state, CameraRenderState camera, Component text, float distance) {
		nodeCollection.submitNameTag(stack, Vec3.ZERO, 0, text, true, state.lightCoords, distance, camera);
	}
}
