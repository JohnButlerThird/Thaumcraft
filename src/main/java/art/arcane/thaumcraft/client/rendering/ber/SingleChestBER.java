package art.arcane.thaumcraft.client.rendering.ber;

import art.arcane.thaumcraft.util.better.BetterChestBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class SingleChestBER implements BlockEntityRenderer<BetterChestBlockEntity, ChestRenderState> {

	private final SpriteGetter sprites;
	private final ChestModel singleModel;
	private final Identifier texture;

	public SingleChestBER(BlockEntityRendererProvider.Context context, Identifier texture) {
		this.sprites = context.sprites();
		this.singleModel = new ChestModel(context.bakeLayer(ModelLayers.CHEST));
		this.texture = texture;
	}

	@Override
	public ChestRenderState createRenderState() {
		return new ChestRenderState();
	}

	@Override
	public void extractRenderState(BetterChestBlockEntity blockEntity, ChestRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		state.facing = blockEntity.getBlockState().getValue(ChestBlock.FACING);
		state.open = blockEntity.getOpenNess(partialTicks);
	}

	@Override
	public void submit(ChestRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
		poseStack.pushPose();
		poseStack.mulPose(ChestRenderer.modelTransformation(state.facing));
		float open = state.open;
		open = 1.0F - open;
		open = 1.0F - open * open * open;
		SpriteId spriteId = Sheets.CHEST_MAPPER.apply(this.texture);
		RenderType renderType = spriteId.renderType(RenderTypes::entityCutoutCull);
		TextureAtlasSprite sprite = this.sprites.get(spriteId);

		submitNodeCollector.submitModel(this.singleModel, open, poseStack, renderType, state.lightCoords, OverlayTexture.NO_OVERLAY, -1, sprite, 0, state.breakProgress);
		poseStack.popPose();
	}

	@Override
	public AABB getRenderBoundingBox(BetterChestBlockEntity blockEntity) {
		BlockPos pos = blockEntity.getBlockPos();
		return net.minecraft.world.phys.AABB.encapsulatingFullBlocks(pos.offset(-1, 0, -1), pos.offset(1, 1, 1));
	}
}