package art.arcane.thaumcraft.blocks.entities;

import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Objects;

public class TubeFilterBlockEntity extends TubeBlockEntity {

    private ResourceKey<Aspect> filterAspect;

    public TubeFilterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ConfigBlockEntities.TUBE_FILTER.entityType(), pPos, pBlockState);
    }

    public ResourceKey<Aspect> getFilterAspect() {
        return filterAspect;
    }

    public boolean setFilterAspect(ResourceKey<Aspect> aspect) {
        if (Objects.equals(this.filterAspect, aspect)) {
            return false;
        }
        this.filterAspect = aspect;
        sync();
        return true;
    }

    public boolean clearFilterAspect() {
        if (this.filterAspect == null) {
            return false;
        }
        this.filterAspect = null;
        sync();
        return true;
    }

    @Override
    protected ResourceKey<Aspect> getAspectFilter() {
        return this.filterAspect;
    }

    @Override
    public int fillAspect(ResourceKey<Aspect> aspect, int amount, Direction dir) {
        if (this.filterAspect != null && aspect != null && !Objects.equals(this.filterAspect, aspect)) {
            return 0;
        }
        return super.fillAspect(aspect, amount, dir);
    }

    @Override
    public boolean canFit(ResourceKey<Aspect> aspect, int amount, Direction dir) {
        if (this.filterAspect != null && aspect != null && !Objects.equals(this.filterAspect, aspect)) {
            return false;
        }
        return super.canFit(aspect, amount, dir);
    }

    @Override
    public boolean compliesToAspect(ResourceKey<Aspect> aspect, Direction dir) {
        if (this.filterAspect != null && aspect != null && !Objects.equals(this.filterAspect, aspect)) {
            return false;
        }
        return super.compliesToAspect(aspect, dir);
    }

    @Override
    protected void loadData(ValueInput input) {
        super.loadData(input);
		input.getString("filterAspect").ifPresent(aspect -> {
			Identifier location = Identifier.tryParse(aspect);
			if (location != null) {
				this.filterAspect = ResourceKey.create(ThaumcraftData.Registries.ASPECT, location);
			} else {
				this.filterAspect = null;
			}
		});
    }

    @Override
    protected void saveData(ValueOutput output) {
        super.saveData(output);
        if (this.filterAspect != null) {
            output.putString("filter_aspect", this.filterAspect.identifier().toString());
        }
    }
}
