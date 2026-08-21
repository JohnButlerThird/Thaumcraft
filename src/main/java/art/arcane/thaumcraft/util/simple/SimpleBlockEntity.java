package art.arcane.thaumcraft.util.simple;

import art.arcane.thaumcraft.Thaumcraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public abstract class SimpleBlockEntity extends BlockEntity {

    public SimpleBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    protected void loadData(ValueInput input) { };
    protected void saveData(ValueOutput output) { };

    public void sync() {
        setChanged();
        if(getLevel() != null)
            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
    }

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		loadData(input);
	}


	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		saveData(output);
	}

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
		CompoundTag tag;
		try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(this.problemPath(), Thaumcraft.LOGGER)) {
			TagValueOutput output = TagValueOutput.createWithContext(reporter, pRegistries);
			writeDataPacket(output);
			tag = output.buildResult();
		}
		return tag.isEmpty() ? saveCustomOnly(pRegistries) : tag;
    }

	public void writeDataPacket(ValueOutput output) { }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
