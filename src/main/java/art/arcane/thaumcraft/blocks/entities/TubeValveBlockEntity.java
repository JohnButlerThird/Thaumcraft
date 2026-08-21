package art.arcane.thaumcraft.blocks.entities;

import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class TubeValveBlockEntity extends TubeBlockEntity {

    private boolean allowFlow = true;
    private boolean wasPoweredLastTick = false;

    public TubeValveBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ConfigBlockEntities.TUBE_VALVE.entityType(), pPos, pBlockState);
    }

    public boolean isFlowAllowed() {
        return allowFlow;
    }

    public void toggleFlowAllowed() {
        setFlowAllowed(!allowFlow);
    }

    public void setFlowAllowed(boolean allowFlow) {
        if (this.allowFlow == allowFlow) {
            return;
        }
        this.allowFlow = allowFlow;
        sync();
    }

    @Override
    protected boolean canUpdateFlow() {
        return allowFlow;
    }

    @Override
    protected void loadData(ValueInput input) {
        super.loadData(input);
        this.allowFlow = input.getBooleanOr("flow_allowed", true);
        this.wasPoweredLastTick = input.getBooleanOr("flow_powered", false);
    }

    @Override
    protected void saveData(ValueOutput output) {
        super.saveData(output);
        output.putBoolean("flow_allowed", this.allowFlow);
        output.putBoolean("flow_powered", this.wasPoweredLastTick);
    }

    @Override
    public void onServerTick() {
        if (this.level != null && this.level.getGameTime() % 5 == 0) {
            boolean powered = this.level.hasNeighborSignal(this.worldPosition);
            if (this.wasPoweredLastTick && !powered && !this.allowFlow) {
                this.allowFlow = true;
                sync();
            } else if (!this.wasPoweredLastTick && powered && this.allowFlow) {
                this.allowFlow = false;
                sync();
            }
            this.wasPoweredLastTick = powered;
        }
        super.onServerTick();
    }
}
