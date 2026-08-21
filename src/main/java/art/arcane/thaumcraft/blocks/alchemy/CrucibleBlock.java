package art.arcane.thaumcraft.blocks.alchemy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidStack;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.blocks.entities.CrucibleBlockEntity;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.util.FluidHelper;
import art.arcane.thaumcraft.util.simple.SimpleBlockMaterials;
import art.arcane.thaumcraft.util.simple.TickableEntityBlock;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;

import java.util.Optional;

@SuppressWarnings("deprecation")
public class CrucibleBlock extends TickableEntityBlock<CrucibleBlockEntity> {

    private static final int SIDE_THICKNESS = 2;
    private static final int LEG_WIDTH = 3;
    private static final int LEG_HEIGHT = 2;
    private static final int LEG_DEPTH = 3;
    private static final int FLOOR_LEVEL = 4;
    private static final VoxelShape INSIDE = box(SIDE_THICKNESS, FLOOR_LEVEL, SIDE_THICKNESS, SIDE_THICKNESS + 12, FLOOR_LEVEL + 12, SIDE_THICKNESS + 12);
    private static final VoxelShape SHAPE = Shapes.join(Shapes.block(), Shapes.or(
            box(0, 0, LEG_WIDTH, 16.0D, LEG_HEIGHT, 12.0D),
            box(LEG_WIDTH, 0.0D, 0.0D, 16 - LEG_WIDTH, LEG_HEIGHT, 16.0D),
            box(LEG_DEPTH, 0.0D, LEG_DEPTH, 16 - LEG_DEPTH, LEG_HEIGHT, 16 - LEG_DEPTH),
            INSIDE), BooleanOp.ONLY_FIRST);

    public CrucibleBlock(BlockBehaviour.Properties props) {
        super(SimpleBlockMaterials.metal(props).mapColor(MapColor.STONE), ConfigBlockEntities.CRUCIBLE.entityTypeObject());
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public VoxelShape getInteractionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return INSIDE;
    }

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
		if (!level.isClientSide()) {
			CrucibleBlockEntity be = getEntity(level, pos);
			if (!FluidHelper.isTankEmpty(be) && be.isCooking()) {
				if (entity instanceof ItemEntity e) {
					ItemStack stack = e.getItem().copy();

					if (be.processInput(stack, e.getOwner() instanceof Player p ? p : null, level.registryAccess(), false)) {
						if (stack.isEmpty()) {
							e.kill((ServerLevel) level);
						} else {
							e.setItem(stack);
						}
					}
				} else if (entity instanceof LivingEntity e && !e.isInvulnerable() && (e instanceof Player p && !p.isCreative())) {
					e.hurt(e.damageSources().inFire(), 1.0F);
					level.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.4F, 2.0F + level.getRandom().nextFloat() * 0.4F);
				}
			}
		}
	}

    @Override
    protected InteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (pLevel.isClientSide())
            return InteractionResult.SUCCESS;
		if(pStack.isEmpty())
			return InteractionResult.TRY_WITH_EMPTY_HAND;

        CrucibleBlockEntity be = getEntity(pLevel, pPos);
        FluidStack stack = FluidUtil.getFirstStackContained(pStack);
        if (stack != FluidStack.EMPTY && FluidStack.matches(stack, new FluidStack(Fluids.WATER, 1000))) {
            if (!FluidHelper.isTankFull(be) && FluidUtil.interactWithFluidHandler(pPlayer, pHand, pLevel, pPos, pHitResult.getDirection())) {
                float randomPitch = 1.0F + (pLevel.getRandom().nextFloat() - pLevel.getRandom().nextFloat()) * .3F;
                pLevel.playSound(null, pPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, .33F, randomPitch);
                be.sync();
            }
            return InteractionResult.SUCCESS;
        } else if (!FluidHelper.isTankEmpty(be) && be.isCooking() && !pPlayer.isCrouching() && pHitResult.getDirection() == Direction.UP) {
            be.processInput(pPlayer.getMainHandItem(), pPlayer, pLevel.registryAccess(), true);
            return InteractionResult.SUCCESS;
        }

        return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (pLevel.isClientSide())
            return InteractionResult.SUCCESS;

        CrucibleBlockEntity be = getEntity(pLevel, pPos);
        if (pPlayer.isCrouching()) {
            getEntity(pLevel, pPos).emptyCrucible();
            return InteractionResult.SUCCESS;
        } else {
            Thaumcraft.info("Aspect List: " + be.getAspects());
        }
        return super.useWithoutItem(pState, pLevel, pPos, pPlayer, pHitResult);
    }
}
