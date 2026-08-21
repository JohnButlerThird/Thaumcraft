package art.arcane.thaumcraft.util.better;

import art.arcane.thaumcraft.extensions.ContainerOpenersCounterExt;
import art.arcane.thaumcraft.util.simple.SimpleChestBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class BetterChestBlockEntity extends RandomizableContainerBlockEntity implements BetterLidBlockEntity {

	private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);

	private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
		@Override
		protected void onOpen(Level level, BlockPos pos, BlockState state) {
			BetterChestBlockEntity.playSound(level, pos, state, SoundEvents.CHEST_OPEN);
		}

		@Override
		protected void onClose(Level level, BlockPos pos, BlockState state) {
			BetterChestBlockEntity.playSound(level, pos, state, SoundEvents.CHEST_CLOSE);
		}

		@Override
		protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int eventId, int eventParam) {
			BetterChestBlockEntity.this.signalOpenCount(level, pos, state, eventId, eventParam);
		}

		@Override
		public boolean isOwnContainer(Player p_155355_) {
			if (!(p_155355_.containerMenu instanceof ChestMenu)) {
				return false;
			} else {
				Container container = ((ChestMenu)p_155355_.containerMenu).getContainer();
				return container == BetterChestBlockEntity.this
						|| container instanceof CompoundContainer && ((CompoundContainer)container).contains(BetterChestBlockEntity.this);
			}
		}
	};

	private final ChestLidController chestLidController = new ChestLidController();

	public BetterChestBlockEntity(BlockEntityType<?> p_155327_, BlockPos p_155328_, BlockState p_155329_) {
		super(p_155327_, p_155328_, p_155329_);
	}

	@Override
	public int getContainerSize() {
		return 27;
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.chest");
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		if (!this.tryLoadLootTable(input)) {
			ContainerHelper.loadAllItems(input, this.items);
		}
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		if (!this.trySaveLootTable(output)) {
			ContainerHelper.saveAllItems(output, this.items);
		}
	}

	@Override
	public void lidAnimateTick(Level level, BlockPos pos, BlockState state) {
		((BetterChestBlockEntity)level.getBlockEntity(pos)).chestLidController.tickLid();
	}

	public static void playSound(Level level, BlockPos pos, BlockState state, SoundEvent sound) {
		level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, sound, SoundSource.BLOCKS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
	}

	@Override
	public boolean triggerEvent(int id, int type) {
		if (id == 1) {
			this.chestLidController.shouldBeOpen(type > 0);
			return true;
		} else {
			return super.triggerEvent(id, type);
		}
	}

	@Override
	public void startOpen(ContainerUser containerUser) {
		if (!this.remove && !containerUser.getLivingEntity().isSpectator()) {
			this.openersCounter.incrementOpeners(containerUser.getLivingEntity(), this.getLevel(), this.getBlockPos(), this.getBlockState(), containerUser.getContainerInteractionRange());
		}
	}

	@Override
	public void stopOpen(ContainerUser containerUser) {
		if (!this.remove && !containerUser.getLivingEntity().isSpectator()) {
			this.openersCounter.decrementOpeners(containerUser.getLivingEntity(), this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}

	public void forceOpen(boolean skipSounds) {
		if (!this.remove) {
			((ContainerOpenersCounterExt)this.openersCounter).thaumcraft$backgroundIncrementOpener(this.getLevel(), this.getBlockPos(), this.getBlockState(), skipSounds);
		}
	}

	public void forceClose(boolean skipSounds) {
		if (!this.remove) {
			((ContainerOpenersCounterExt)this.openersCounter).thaumcraft$backgroundDecrementOpener(this.getLevel(), this.getBlockPos(), this.getBlockState(), skipSounds);
		}
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.items;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> items) {
		this.items = items;
	}

	@Override
	public float getOpenNess(float partialTicks) {
		return this.chestLidController.getOpenness(partialTicks);
	}

	public static int getOpenCount(BlockGetter level, BlockPos pos) {
		BlockState blockstate = level.getBlockState(pos);
		if (blockstate.hasBlockEntity()) {
			BlockEntity blockentity = level.getBlockEntity(pos);
			if (blockentity instanceof BetterChestBlockEntity be) {
				return be.openersCounter.getOpenerCount();
			}
		}

		return 0;
	}

	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory player) {
		return ChestMenu.threeRows(id, player, this);
	}

	@Override
	public void setBlockState(BlockState state) {
		var oldState = getBlockState();
		super.setBlockState(state);
		if ( oldState.getValue(SimpleChestBlock.FACING) != state.getValue(SimpleChestBlock.FACING)) {
			this.invalidateCapabilities();
		}
	}

	public void recheckOpen() {
		if (!this.remove) {
			this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}

	protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int eventId, int eventParam) {
		Block block = state.getBlock();
		level.blockEvent(pos, block, 1, eventParam);
	}
}
