package art.arcane.thaumcraft.client;

import lombok.Setter;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.*;

public class ThaumcraftClientRecipes {

	@Setter
	private static RecipeMap recipeMap;

	public static <I extends RecipeInput, T extends Recipe<I>> RecipeHolder<T> getRecipe(RecipeType<T> recipeType, Identifier id) {
		return recipeMap.byType(recipeType).stream().filter(r -> r.id().identifier().equals(id)).findFirst().orElse(null);
	}
}
