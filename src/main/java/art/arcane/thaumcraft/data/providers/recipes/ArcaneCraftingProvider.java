package art.arcane.thaumcraft.data.providers.recipes;

import art.arcane.thaumcraft.registries.ConfigItems;
import com.google.gson.JsonElement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.data.recipes.ArcaneCraftingRecipe;
import art.arcane.thaumcraft.registries.ConfigBlocks;
import art.arcane.thaumcraft.util.codec.CodecDataProvider;

import java.util.Map;

public class ArcaneCraftingProvider extends CodecDataProvider<ArcaneCraftingRecipe> {

    public ArcaneCraftingProvider(PackOutput output) {
        super(output, "Arcane Crafting Recipes", "recipe/arcane_crafting", ArcaneCraftingRecipe.CODEC);
    }

    @Override
    protected void createEntries(HolderLookup.Provider registries) {
        register(ThaumcraftData.Recipes.ArcaneCrafting.DEBUG.identifier(), new ArcaneCraftingRecipe.Builder(new ItemStackTemplate(Items.DIAMOND, 2))
                .setPattern(
                        "#+#",
                        "+ +",
                        "#+#",
                        Map.of('#', Ingredient.of(Items.STICK), '+', Ingredient.of(Items.GOLD_INGOT)))
                .setCrystalCost(Map.of(Aspect.Primal.AIR, 1))
                .setVisCost(20).build());

        register(ThaumcraftData.Recipes.ArcaneCrafting.TUBE.identifier(), new ArcaneCraftingRecipe.Builder(new ItemStackTemplate(ConfigBlocks.TUBE.item(), 4))
                .setPattern(
                        "###",
                        "+++",
                        "###",
                        Map.of('#', Ingredient.of(Items.STONE), '+', Ingredient.of(Items.GLASS)))
                .setCrystalCost(Map.of(Aspect.Primal.ORDER, 2))
                .setVisCost(20).build());
    }

    @Override
    protected void processJson(JsonElement element) {
        element.getAsJsonObject().addProperty("type", ThaumcraftData.Recipes.Types.ARCANE_CRAFTING.identifier().toString());
        super.processJson(element);
    }
}
