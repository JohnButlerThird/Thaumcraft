package art.arcane.thaumcraft.blocks.entities;

import art.arcane.thaumcraft.Thaumcraft;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import art.arcane.thaumcraft.data.attachments.AuraAttachment;
import art.arcane.thaumcraft.data.aura.AuraHelper;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.util.simple.SimpleBlockEntity;
import art.arcane.thaumcraft.util.simple.TickableBlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Arrays;

public class DioptraBlockEntity extends SimpleBlockEntity implements TickableBlockEntity {

    public static final int GRID_SIZE = 13;
    public static final int GRID_RADIUS = 6;

    @Getter
    private final byte[][] visData, fluxData;

    private int tickCounter;

    public DioptraBlockEntity(BlockPos pos, BlockState state) {
        super(ConfigBlockEntities.DIOPTRA.entityType(), pos, state);
        visData = new byte[GRID_SIZE][GRID_SIZE];
        fluxData = new byte[GRID_SIZE][GRID_SIZE];
    }

    @Override
    public TickSetting getTickSetting() {
        return TickSetting.SERVER;
    }

    @Override
    public void onServerTick() {
        tickCounter++;
        if (tickCounter >= 20) {
            tickCounter = 0;
            scanAura();
            sync();
        }
    }

    private void scanAura() {
        if (level == null)
            return;

        ChunkPos centerChunk = ChunkPos.containing(getBlockPos());

        for (int x = 0; x < GRID_SIZE; x++) {
            for (int z = 0; z < GRID_SIZE; z++) {
                int chunkX = centerChunk.x() + (x - GRID_RADIUS);
                int chunkZ = centerChunk.z() + (z - GRID_RADIUS);
                ChunkPos targetChunk = new ChunkPos(chunkX, chunkZ);

                var auraOpt = AuraHelper.getAura(level, targetChunk);
                if (auraOpt.isPresent()) {
                    AuraAttachment aura = auraOpt.get();
                    visData[x][z] = (byte) Math.min(64, (int) (aura.getVis() / AuraAttachment.MAX_AURA * 64));
                    fluxData[x][z] = (byte) Math.min(64, (int) (aura.getFlux() / AuraAttachment.MAX_AURA * 64));
                } else {
                    visData[x][z] = fluxData[x][z] = 0;
                }
            }
        }
    }

    public float getFillData(int x, int z, boolean isVis) {
        byte value = isVis ? visData[x][z] : fluxData[x][z];
        return value / 64F;
    }

    public byte getCenterData(boolean vis) {
        return vis ? visData[GRID_RADIUS][GRID_RADIUS] : fluxData[GRID_RADIUS][GRID_RADIUS];
    }


    @Override
    public void writeDataPacket(ValueOutput output) {
        int[] vis = new int[GRID_SIZE * GRID_SIZE];
        int[] flux = new int[GRID_SIZE * GRID_SIZE];
        for(int z = 0; z < GRID_SIZE; z++)
            for(int x = 0; x < GRID_SIZE; x++) {
                vis[x + (z * GRID_SIZE)] = this.visData[x][z];
                flux[x + (z * GRID_SIZE)] = this.fluxData[x][z];
            }

        output.putIntArray("vis", vis);
        output.putIntArray("flux", flux);
    }

    @Override
    public void onDataPacket(Connection net, ValueInput input) {
        int[] vis = input.getIntArray("vis").get();
        int[] flux = input.getIntArray("flux").get();

        for(int z = 0; z < GRID_SIZE; z++)
            for(int x = 0; x < GRID_SIZE; x++) {
                this.visData[x][z] = (byte)vis[x + (z * GRID_SIZE)];
                this.fluxData[x][z] = (byte)flux[x + (z * GRID_SIZE)];
            }
    }
}
