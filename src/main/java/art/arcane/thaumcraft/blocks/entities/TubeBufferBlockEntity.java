package art.arcane.thaumcraft.blocks.entities;

import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.api.capabilities.IEssentiaCapability;
import art.arcane.thaumcraft.data.aspects.AspectList;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.registries.ConfigCapabilities;
import art.arcane.thaumcraft.util.BitPacker;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

public class TubeBufferBlockEntity extends TubeBlockEntity {

    public static final int MAX_AMOUNT = 10;

    private AspectList aspects = new AspectList();
    private byte[] chokedSides = new byte[]{0, 0, 0, 0, 0, 0};

    public TubeBufferBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ConfigBlockEntities.TUBE_BUFFER.entityType(), pPos, pBlockState);
    }

    public boolean cycleChoke(Direction direction) {
        if (direction == null) {
            return false;
        }
        this.chokedSides[direction.ordinal()]++;
        if (this.chokedSides[direction.ordinal()] > 2) {
            this.chokedSides[direction.ordinal()] = 0;
        }
        sync();
        return true;
    }

    public byte getChoke(Direction direction) {
        if (direction == null) {
            return 0;
        }
        return this.chokedSides[direction.ordinal()];
    }

    public Map<ResourceKey<Aspect>, Integer> getContents() {
        Map<ResourceKey<Aspect>, Integer> contents = new HashMap<>();
        for (Map.Entry<ResourceKey<Aspect>, Short> entry : aspects.entrySet()) {
            contents.put(entry.getKey(), (int) entry.getValue());
        }
        return contents;
    }

    public int getComparatorLevel() {
        float ratio = (float) this.aspects.size() / (float) MAX_AMOUNT;
        return (int) Math.floor(ratio * 14.0F) + (this.aspects.size() > 0 ? 1 : 0);
    }

    @Override
    protected void loadData(ValueInput input) {
        super.loadData(input);
        this.aspects.clear();
		input.read("aspects", AspectList.CODEC.codec()).ifPresent(aspect -> this.aspects = aspect);
        Byte[] loaded = input.read("choke", Codec.BYTE.listOf()).get().toArray(new Byte[0]);
        if (loaded.length == 6) {
            this.chokedSides = ArrayUtils.toPrimitive(loaded);
        } else {
            this.chokedSides = new byte[]{0, 0, 0, 0, 0, 0};
        }
    }

    @Override
    protected void saveData(ValueOutput output) {
        super.saveData(output);
        output.store("aspects", AspectList.CODEC.codec(),  this.aspects);
        output.store("choke", Codec.BYTE.listOf(), List.of(ArrayUtils.toObject(chokedSides)));
    }

    @Override
    public void onServerTick() {
        if (this.level == null || this.level.isClientSide()) {
            return;
        }
        if (this.level.getGameTime() % 5 == 0 && this.aspects.size() < MAX_AMOUNT) {
            fillFromNeighbors();
        }
    }

    @Override
    public TickSetting getTickSetting() {
        return TickSetting.SERVER;
    }

    private IEssentiaCapability getNeighbor(Direction dir) {
        if (this.level == null || dir == null) {
            return null;
        }
        return this.level.getCapability(ConfigCapabilities.ESSENTIA, this.worldPosition.offset(dir.getUnitVec3i()), dir.getOpposite());
    }

    private void fillFromNeighbors() {
        for (Direction dir : Direction.values()) {
            if (!isConnectable(dir)) {
                continue;
            }
            IEssentiaCapability cap = getNeighbor(dir);
            if (cap == null || !cap.getSideStatus(dir.getOpposite()).isOutput()) {
                continue;
            }
            int mySuction = getSuction(dir);
            if (mySuction <= 0) {
                continue;
            }
            if (cap.getEssentia(dir.getOpposite()) > 0
                    && cap.getSuction(dir.getOpposite()) < mySuction
                    && mySuction >= cap.getMinimumSuction(dir.getOpposite())) {
                ResourceKey<Aspect> type = cap.getEssentiaType(dir.getOpposite());
                if (type == null) {
                    continue;
                }
                int drained = cap.drainAspect(type, 1, dir.getOpposite());
                if (drained <= 0) {
                    continue;
                }
                int inserted = fillAspect(type, drained, dir);
                if (inserted <= 0) {
                    cap.fillAspect(type, drained, dir.getOpposite());
                    continue;
                }
                playTransferSound();
                if (drained > inserted) {
                    cap.fillAspect(type, drained - inserted, dir.getOpposite());
                }
                return;
            }
        }
    }

    @Override
    public int getEssentia(Direction dir) {
        if (!isConnectable(dir)) {
            return 0;
        }
        return aspects.size();
    }

    @Override
    public ResourceKey<Aspect> getEssentiaType(Direction dir) {
        if (!isConnectable(dir) || this.aspects.isEmpty()) {
            return null;
        }
        List<ResourceKey<Aspect>> present = this.aspects.aspectsPresent();
        if (present.isEmpty()) {
            return null;
        }
        if (this.level == null) {
            return present.get(0);
        }
        return present.get(this.level.getRandom().nextInt(present.size()));
    }

    @Override
    public int getMinimumSuction(Direction dir) {
        return 0;
    }

    @Override
    public int getSuction(Direction dir) {
        if (!isConnectable(dir)) {
            return 0;
        }
        if (dir != null && this.chokedSides[dir.ordinal()] == 2) {
            return 0;
        }
        return 1;
    }

    @Override
    public ResourceKey<Aspect> getSuctionType(Direction dir) {
        return null;
    }

    @Override
    public int drainAspect(ResourceKey<Aspect> aspect, int amount, Direction dir) {
        if (!isConnectable(dir) || aspect == null || amount <= 0) {
            return 0;
        }
        if (this.aspects.amount(aspect) <= 0) {
            return 0;
        }
        int sourceSuction = 0;
        IEssentiaCapability source = getNeighbor(dir);
        if (source != null) {
            sourceSuction = source.getSuction(dir.getOpposite());
        }
        for (Direction other : Direction.values()) {
            if (!isConnectable(other) || other == dir) {
                continue;
            }
            IEssentiaCapability cap = getNeighbor(other);
            if (cap == null) {
                continue;
            }
            int suck = cap.getSuction(other.getOpposite());
            ResourceKey<Aspect> suctionType = cap.getSuctionType(other.getOpposite());
            if ((suctionType == null || Objects.equals(suctionType, aspect))
                    && sourceSuction < suck
                    && getSuction(other) < suck) {
                return 0;
            }
        }
        int drained = Math.min(amount, this.aspects.amount(aspect));
        this.aspects.remove(aspect, drained);
        setChanged();
        return drained;
    }

    @Override
    public int fillAspect(ResourceKey<Aspect> aspect, int amount, Direction dir) {
        if (!isConnectable(dir) || aspect == null || amount <= 0) {
            return 0;
        }
        if (this.aspects.size() >= MAX_AMOUNT) {
            return 0;
        }
        int added = Math.min(amount, MAX_AMOUNT - this.aspects.size());
        this.aspects.add(aspect, added);
        setChanged();
        return added;
    }

    @Override
    public boolean canFit(ResourceKey<Aspect> aspect, int amount, Direction dir) {
        if (!isConnectable(dir)) {
            return false;
        }
        if (amount <= 0) {
            return true;
        }
        return aspect != null && this.aspects.size() + amount <= MAX_AMOUNT;
    }

    @Override
    public boolean contains(ResourceKey<Aspect> aspect, int amount, Direction dir) {
        if (!isConnectable(dir)) {
            return false;
        }
        if (amount <= 0) {
            return true;
        }
        if (aspect == null) {
            return this.aspects.size() >= amount;
        }
        return this.aspects.amount(aspect) >= amount;
    }

    @Override
    public boolean compliesToAspect(ResourceKey<Aspect> aspect, Direction dir) {
        return isConnectable(dir);
    }

    @Override
    public boolean isContainer(Direction dir) {
        return true;
    }
}
