package art.arcane.thaumcraft.commands;

import art.arcane.thaumcraft.items.VisChargeItem;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class VisChargeCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("thaumcraft")
				.requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_OWNER) && source.isPlayer())
				.then(Commands.literal("charge")
						.executes(ctx -> chargeAll(ctx.getSource().getPlayer(), -1))
						.then(Commands.argument("amount", IntegerArgumentType.integer(1))
								.executes(ctx -> chargeAll(ctx.getSource().getPlayer(), IntegerArgumentType.getInteger(ctx, "amount"))))
				)
		);
	}

	private static int chargeAll(Player player, int amount) {
		player.getInventory().getNonEquipmentItems().stream().filter(itemStack -> itemStack.getItem() instanceof VisChargeItem).forEach(i -> chargeItem(i, amount));
		for (EquipmentSlot value : EquipmentSlot.values()) {
			if(value.equals(EquipmentSlot.BODY))
				continue;
			ItemStack item = player.getItemBySlot(value);
			if(!item.isEmpty() && item.getItem() instanceof VisChargeItem)
				chargeItem(item, amount);
		}
		return 0;
	}

	private static void chargeItem(ItemStack itemStack, int amount) {
		if(itemStack.getItem() instanceof VisChargeItem vis) {
			vis.setCharge(itemStack, amount == -1 ? 9999 : amount);
		}
	}
}
