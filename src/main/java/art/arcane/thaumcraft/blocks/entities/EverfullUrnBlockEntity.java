package art.arcane.thaumcraft.blocks.entities;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import art.arcane.thaumcraft.data.aura.AuraHelper;
import art.arcane.thaumcraft.integrations.botania.BotaniaCompat;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.util.simple.SimpleBlockEntity;
import art.arcane.thaumcraft.util.simple.TickableBlockEntity;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

@Getter
public class EverfullUrnBlockEntity extends SimpleBlockEntity implements TickableBlockEntity {

    public static final int MAX_WATER = 1000;
    public static final int BUCKET_COST = 1000;
    public static final int BOTTLE_COST = 333;
    public static final int CAULDRON_COST = 333;
    public static final int FLUID_TRANSFER_AMOUNT = 25;
    public static final int APOTHECARY_COST = 1000;
    private static final int SCAN_RADIUS_XZ = 5;
    private static final int SCAN_RADIUS_Y_DOWN = 3;
    private static final int SCAN_RADIUS_Y_UP = 2;
    private static final int SCAN_SIZE_XZ = SCAN_RADIUS_XZ * 2 + 1;
    private static final int SCAN_SIZE_Y = SCAN_RADIUS_Y_DOWN + SCAN_RADIUS_Y_UP + 1;
    private static final int MAX_SCAN_INDEX = SCAN_SIZE_XZ * SCAN_SIZE_Y * SCAN_SIZE_XZ;
    private static final int SCANS_PER_TICK = 8;

    private int waterAmount;
    private int scanIndex;
    private final IntSet knownHandlers = new IntArraySet();

    public EverfullUrnBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ConfigBlockEntities.EVERFULL_URN.entityType(), pPos, pBlockState);
        this.waterAmount = 0;
        this.scanIndex = 0;
    }

    @Override
    public void onServerTick() {
        if (waterAmount < MAX_WATER) {
            float neededVis = (MAX_WATER - waterAmount) / 1000.0F;
            if (neededVis > 0.1F) {
                neededVis = 0.1F;
            }
            float drainedVis = AuraHelper.drainVis(getLevel(), getBlockPos(), neededVis, false);
            int waterGain = (int) (1000.0F * drainedVis);
            if (waterGain > 0) {
                waterAmount = Math.min(MAX_WATER, waterAmount + waterGain);
                setChanged();
                if (waterAmount >= MAX_WATER) {
                    sync();
                }
            }
        }

        tickContainerFilling();
    }

    private void tickContainerFilling() {
        Level level = getLevel();
        if (level == null) return;

        for (int i = 0; i < SCANS_PER_TICK; i++) {
            scanIndex = (scanIndex + 1) % MAX_SCAN_INDEX;
            BlockPos scanPos = indexToPos(scanIndex);

            if (scanPos.equals(getBlockPos())) continue;

            if (isValidHandler(level, scanPos)) {
                knownHandlers.add(scanIndex);
            } else {
                knownHandlers.remove(scanIndex);
            }
        }

        for (int idx : knownHandlers.toIntArray()) {
            if (waterAmount < FLUID_TRANSFER_AMOUNT) break;

            BlockPos pos = indexToPos(idx);
            if (!level.isLoaded(pos)) continue;

            if (tryFillFluidHandler(level, pos)) break;
            if (tryFillCauldron(level, pos)) break;
            if (tryFillBotaniaApothecary(level, pos)) break;
        }
    }

    private boolean isValidHandler(Level level, BlockPos pos) {
        if (!level.isLoaded(pos)) return false;

        BlockState state = level.getBlockState(pos);
        if (state.is(Blocks.CAULDRON) || state.is(Blocks.WATER_CAULDRON)) {
            return true;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity == null) return false;

        for (Direction dir : Direction.values()) {
            ResourceHandler<FluidResource> handler = level.getCapability(Capabilities.Fluid.BLOCK, pos, state, blockEntity, dir);
            if (handler != null) return true;
        }

        if (BotaniaCompat.isPetalApothecary(blockEntity)) {
            return true;
        }

        return false;
    }

    private boolean tryFillFluidHandler(Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity == null) return false;

        BlockState state = blockEntity.getBlockState();
        for (Direction dir : Direction.values()) {
			ResourceHandler<FluidResource> handler = level.getCapability(Capabilities.Fluid.BLOCK, pos, state, blockEntity, dir);
            if (handler == null) continue;

			try(Transaction transaction = Transaction.openRoot()) {
				if (handler.insert(FluidResource.of(Fluids.WATER), waterAmount, transaction) > 0) {
					transaction.commit();
					return true;
				}
			}
        }
        return false;
    }

    private boolean tryFillCauldron(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.is(Blocks.CAULDRON) && waterAmount >= CAULDRON_COST) {
            level.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 1));
            consumeWater(CAULDRON_COST);
            return true;
        }
        if (state.is(Blocks.WATER_CAULDRON)) {
            int cauldronLevel = state.getValue(LayeredCauldronBlock.LEVEL);
            if (cauldronLevel < 3 && waterAmount >= CAULDRON_COST) {
                level.setBlockAndUpdate(pos, state.setValue(LayeredCauldronBlock.LEVEL, cauldronLevel + 1));
                consumeWater(CAULDRON_COST);
                return true;
            }
        }
        return false;
    }

    private boolean tryFillBotaniaApothecary(Level level, BlockPos pos) {
        if (!BotaniaCompat.isLoaded() || waterAmount < APOTHECARY_COST) {
            return false;
        }
        if (BotaniaCompat.tryFillApothecary(level, pos)) {
            consumeWater(APOTHECARY_COST);
            return true;
        }
        return false;
    }

    private void consumeWater(int amount) {
        boolean wasFull = isFull();
        waterAmount -= amount;
        setChanged();
        if (wasFull) {
            sync();
        }
    }

    private BlockPos indexToPos(int index) {
        int x = index % SCAN_SIZE_XZ - SCAN_RADIUS_XZ;
        int y = (index / SCAN_SIZE_XZ) % SCAN_SIZE_Y - SCAN_RADIUS_Y_DOWN;
        int z = (index / (SCAN_SIZE_XZ * SCAN_SIZE_Y)) - SCAN_RADIUS_XZ;
        return getBlockPos().offset(x, y, z);
    }

    @Override
    public TickSetting getTickSetting() {
        return TickSetting.SERVER;
    }

    public boolean isFull() {
        return waterAmount >= MAX_WATER;
    }

    public boolean drainWater(int amount) {
        if (waterAmount >= amount) {
            boolean wasFull = isFull();
            waterAmount -= amount;
            setChanged();
            if (wasFull) {
                sync();
            }
            return true;
        }
        return false;
    }

    @Override
    protected void loadData(ValueInput input) {
        this.waterAmount = input.getIntOr("water", 0);
    }

    @Override
    protected void saveData(ValueOutput output) {
        output.putInt("water", this.waterAmount);
    }
}
