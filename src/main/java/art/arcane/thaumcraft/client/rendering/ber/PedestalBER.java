package art.arcane.thaumcraft.client.rendering.ber;

import art.arcane.thaumcraft.registries.ConfigBlocks;
import art.arcane.thaumcraft.util.simple.SimpleBER;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import art.arcane.thaumcraft.blocks.entities.PedestalBlockEntity;
import net.minecraft.world.phys.Vec3;

public class PedestalBER extends SimpleBER<PedestalBlockEntity, PedestalBER.RenderState> {

    private final BlockEntityRendererProvider.Context context;

    public PedestalBER(BlockEntityRendererProvider.Context context) {
        super(RenderState::new);
        this.context = context;
    }

    @Override
    protected void extractRenderState(PedestalBlockEntity blockEntity, RenderState state, float partialTicks, Vec3 cameraPosition) {
        state.update(context.itemModelResolver(), blockEntity.getItemStack(), blockEntity.getLevel(),
                blockEntity.getBlockState().getBlock() == ConfigBlocks.ARCANE_PEDESTAL.block(),
                Mth.rotLerp(partialTicks, (blockEntity.getLevel().getGameTime() + partialTicks - 1) % 360, blockEntity.getLevel().getGameTime() % 360));
    }

    @Override
    public void submit(RenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if(state.itemStack != null) {
            poseStack.pushPose();

            poseStack.translate(.5F, state.isTall ? 1.25F : 1F, .5F);
            poseStack.mulPose(Axis.YP.rotationDegrees(state.rotation));

            state.itemStack.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

            poseStack.popPose();
        }
    }

    @Override
    public AABB getRenderBoundingBox(PedestalBlockEntity blockEntity) {
        return new AABB(blockEntity.getBlockPos().above()).inflate(.25).move(0, .25F, 0);
    }

    public static class RenderState extends BlockEntityRenderState {
        private ItemStackRenderState itemStack = new ItemStackRenderState();
        private boolean isTall;
        private float rotation;

        public void update(ItemModelResolver resolver, ItemStack stack, Level level, boolean isTall, float rotation) {
            resolver.updateForTopItem(this.itemStack, stack, ItemDisplayContext.GROUND, level, null, (int)this.blockPos.asLong());
            this.isTall = isTall;
            this.rotation = rotation;
        }
    }
}
