package art.arcane.thaumcraft.api.helpers;

import art.arcane.thaumcraft.registries.ConfigDataAttachments;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public final class ResearchHelper {

	public static boolean grantResearchTag(Player player, Identifier tag) {
		return player.getData(ConfigDataAttachments.PLAYER_RESEARCH.get()).grantResearchTag(tag);
	}
}
