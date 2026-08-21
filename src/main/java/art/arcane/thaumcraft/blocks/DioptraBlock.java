package art.arcane.thaumcraft.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import art.arcane.thaumcraft.blocks.entities.DioptraBlockEntity;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.util.simple.SimpleBlockMaterials;
import art.arcane.thaumcraft.util.simple.TickableEntityBlock;

public class DioptraBlock extends TickableEntityBlock<DioptraBlockEntity> {

    public static final BooleanProperty DISPLAY_VIS = BooleanProperty.create("display_vis");

    private static final VoxelShape SHAPE = Shapes.join(Shapes.block(), Shapes.box(2, 14, 2, 14, 16, 14), BooleanOp.ONLY_FIRST);

    public DioptraBlock(BlockBehaviour.Properties props) {
        super(SimpleBlockMaterials.metal(props), ConfigBlockEntities.DIOPTRA.entityTypeObject());
        registerDefaultState(getStateDefinition().any().setValue(DISPLAY_VIS, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DISPLAY_VIS);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            boolean newValue = !state.getValue(DISPLAY_VIS);
            level.setBlock(pos, state.setValue(DISPLAY_VIS, newValue), 3);
            getEntity(level, pos).sync();
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

	@Override
	protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
		DioptraBlockEntity be = getEntity(level, pos);
		return Math.min(15, (int) (be.getCenterData(state.getValue(DISPLAY_VIS)) / 500F * 14F) + 1);
	}
}
