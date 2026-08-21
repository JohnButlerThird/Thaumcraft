package art.arcane.thaumcraft.data.providers.recipes;

import com.google.gson.JsonElement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.recipes.SalisMundusMultiblockRecipe;
import art.arcane.thaumcraft.util.codec.CodecDataProvider;

public class SalisMundusMultiblockRecipeProvider extends CodecDataProvider<SalisMundusMultiblockRecipe> {

    public SalisMundusMultiblockRecipeProvider(PackOutput output) {
        super(output, "Salis Mundus Multiblock Recipes", "recipe/salis_mundus_multiblock", SalisMundusMultiblockRecipe.CODEC);
    }

    @Override
    protected void createEntries(HolderLookup.Provider registries) {

    }

    @Override
    protected void processJson(JsonElement element) {
        element.getAsJsonObject().addProperty("type", ThaumcraftData.Recipes.Types.SALIS_MUNDUS_MULTIBLOCK.identifier().toString());
        super.processJson(element);
    }
}
