package art.arcane.thaumcraft.integrations.jei.aspect;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import art.arcane.thaumcraft.data.aspects.AspectList;
import art.arcane.thaumcraft.integrations.jei.ThaumcraftJEIPlugin;

public class AspectIngredientHelper implements IIngredientHelper<AspectList> {

    @Override
    public IIngredientType<AspectList> getIngredientType() {
        return ThaumcraftJEIPlugin.ASPECT_LIST;
    }

    @Override
    public String getDisplayName(AspectList ingredient) {
        return ingredient.aspectsPresent().get(0).identifier().toLanguageKey();
    }

    @Override
    public Object getUid(AspectList ingredient, UidContext context) {
        return ingredient.aspectsPresent().get(0).identifier().toString();
    }

    @Override
    public Identifier getIdentifier(AspectList ingredient) {
        return ingredient.aspectsPresent().get(0).identifier();
    }

    @Override
    public AspectList copyIngredient(AspectList ingredient) {
        return ingredient.clone();
    }

    @Override
    public String getErrorInfo(@Nullable AspectList ingredient) {
        return "null";
    }
}
