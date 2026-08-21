package art.arcane.thaumcraft.blocks.entities;

import art.arcane.thaumcraft.api.capabilities.IInfusionModifierCapability;
import art.arcane.thaumcraft.api.capabilities.IInfusionStabilizerCapability;
import art.arcane.thaumcraft.registries.ConfigBlocks;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import art.arcane.thaumcraft.api.capabilities.IInfusionPedestalCapability;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.util.simple.SimpleBlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PedestalBlockEntity extends SimpleBlockEntity implements IInfusionPedestalCapability, IInfusionStabilizerCapability {

    @Getter @Setter
    private ItemStack itemStack;

    public PedestalBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ConfigBlockEntities.PEDESTAL.entityType(), pPos, pBlockState);
        this.itemStack = ItemStack.EMPTY;
    }

    @Override
    protected void loadData(ValueInput input) {
        this.itemStack = input.read("item", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
    }

    @Override
    protected void saveData(ValueOutput output) {
        output.store("item", ItemStack.OPTIONAL_CODEC,  this.itemStack);
    }

	/* -------------------------------------------------------------------------------------------------------------- */
	/*                                         InfusionPedestal Capability Methods                                    */
	/* -------------------------------------------------------------------------------------------------------------- */

    @Override
    public ItemStack getItem() {
        return itemStack;
    }

    @Override
    public void consumeItem() {
        this.itemStack = this.itemStack.getCraftingRemainder().create();
        sync();
    }

	/* -------------------------------------------------------------------------------------------------------------- */
	/*                                         InfusionStabilizer Capability Methods                                  */
	/* -------------------------------------------------------------------------------------------------------------- */

	@Override
	public boolean isStabilizingInfusion() { return true; }

	@Override
	public float getStabilizationModifier(Level level, BlockPos runicMatrix, BlockPos block1, @Nullable BlockPos block2) {
		return getBlockState().is(ConfigBlocks.ELDRITCH_PEDESTAL.block()) ? .1F : 0;
	}

	@Override
	public boolean additionalSymmetryCheck(Level level, BlockPos runicMatrix, BlockPos block1, BlockPos block2) {
		Optional<PedestalBlockEntity> main =  level.getBlockEntity(block1, ConfigBlockEntities.PEDESTAL.entityType());
		Optional<PedestalBlockEntity> counterpart =  level.getBlockEntity(block2, ConfigBlockEntities.PEDESTAL.entityType());

		if(main.isEmpty() || counterpart.isEmpty())
			return true;

		return main.get().getItem().isEmpty() == counterpart.get().getItem().isEmpty();
	}

	@Override
	public float brokenSymmetryPenalty(Level level, BlockPos runicMatrix, BlockPos block1, BlockPos block2) {
		return -0.1F;
	}
}
