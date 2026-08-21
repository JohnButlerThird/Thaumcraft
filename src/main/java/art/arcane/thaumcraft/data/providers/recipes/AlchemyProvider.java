package art.arcane.thaumcraft.data.providers.recipes;

import com.google.gson.JsonElement;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import art.arcane.thaumcraft.registries.ConfigItems;
import art.arcane.thaumcraft.util.FallbackHolder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.registries.ConfigBlocks;
import art.arcane.thaumcraft.data.aspects.AspectList;
import art.arcane.thaumcraft.data.recipes.AlchemyRecipe;
import art.arcane.thaumcraft.util.codec.CodecDataProvider;

import java.util.concurrent.CompletableFuture;

import static art.arcane.thaumcraft.api.ThaumcraftData.Recipes;

public class AlchemyProvider extends CodecDataProvider<AlchemyRecipe> {

    private HolderGetter<Item> items;

    public AlchemyProvider(PackOutput generator, CompletableFuture<HolderLookup.Provider> combinedLookup) {
        super(generator, "AlchemyRecipes", "recipe/alchemy", AlchemyRecipe.CODEC, combinedLookup);
    }

    @Override
    protected void createEntries(HolderLookup.Provider registries) {
        this.items = registries.lookupOrThrow(Registries.ITEM);
        recipe(Recipes.Alchemy.DEBUG.identifier(),
                Ingredient.of(Items.STICK),
                new AspectList().add(ThaumcraftData.Aspects.WATER, 5).add(ThaumcraftData.Aspects.EARTH, 5),
                new ItemStackTemplate(Items.DIAMOND));

        recipe(Recipes.Alchemy.ALUMENTUM.identifier(),
                Ingredient.of(Items.COAL),
                new AspectList().add(ThaumcraftData.Aspects.POWER, 10).add(ThaumcraftData.Aspects.FIRE, 10).add(ThaumcraftData.Aspects.CHAOS, 5),
                new ItemStackTemplate(ConfigItems.ALUMENTUM.get()));

        recipe(Recipes.Alchemy.BRASSINGOT.identifier(),
                Ingredient.of(Items.IRON_INGOT),
                new AspectList().add(ThaumcraftData.Aspects.TOOL, 5),
                new ItemStackTemplate(ConfigItems.INGOT_BRASS.get()));

        recipe(Recipes.Alchemy.THAUMIUMINGOT.identifier(),
                Ingredient.of(Items.IRON_INGOT),
                new AspectList().add(ThaumcraftData.Aspects.MAGIC, 5).add(ThaumcraftData.Aspects.EARTH, 5),
                new ItemStackTemplate(ConfigItems.INGOT_THAUMIUM.get()));

        recipe(Recipes.Alchemy.HEDGE_TALLOW.identifier(),
                Ingredient.of(Items.ROTTEN_FLESH),
                new AspectList().add(ThaumcraftData.Aspects.FIRE, 1),
                new ItemStackTemplate(ConfigItems.TALLOW.get()));

        recipe(Recipes.Alchemy.HEDGE_LEATHER.identifier(),
                Ingredient.of(Items.ROTTEN_FLESH),
                new AspectList().add(ThaumcraftData.Aspects.AIR, 3).add(ThaumcraftData.Aspects.CREATURE, 3),
                new ItemStackTemplate(Items.LEATHER));

        recipe(Recipes.Alchemy.HEDGE_GUNPOWDER.identifier(),
                Ingredient.of(Items.GUNPOWDER),
                new AspectList().add(ThaumcraftData.Aspects.FIRE, 2).add(ThaumcraftData.Aspects.CHAOS, 2),
                new ItemStackTemplate(Items.GUNPOWDER, 2));

        recipe(Recipes.Alchemy.HEDGE_SLIME.identifier(),
                Ingredient.of(Items.SLIME_BALL),
                new AspectList().add(ThaumcraftData.Aspects.WATER, 2).add(ThaumcraftData.Aspects.LIFE, 2),
                new ItemStackTemplate(Items.SLIME_BALL, 2));

        recipe(Recipes.Alchemy.HEDGE_GLOWSTONE.identifier(),
                Ingredient.of(Items.GLOWSTONE_DUST),
                new AspectList().add(ThaumcraftData.Aspects.LIGHT, 2).add(ThaumcraftData.Aspects.SENSE, 2),
                new ItemStackTemplate(Items.GLOWSTONE_DUST, 2));

        recipe(Recipes.Alchemy.HEDGE_DYE.identifier(),
                Ingredient.of(Items.BLACK_DYE),
                new AspectList().add(ThaumcraftData.Aspects.SENSE, 2),
                new ItemStackTemplate(Items.BLACK_DYE, 2));

        recipe(Recipes.Alchemy.HEDGE_CLAY.identifier(),
                Ingredient.of(Items.DIRT),
                new AspectList().add(ThaumcraftData.Aspects.WATER, 3).add(ThaumcraftData.Aspects.EARTH, 1),
                new ItemStackTemplate(Items.CLAY_BALL));

        recipe(Recipes.Alchemy.HEDGE_STRING.identifier(),
                Ingredient.of(Items.WHEAT),
                new AspectList().add(ThaumcraftData.Aspects.CRAFT, 2).add(ThaumcraftData.Aspects.CREATURE, 1),
                new ItemStackTemplate(Items.STRING));

        recipe(Recipes.Alchemy.HEDGE_WEB.identifier(),
                Ingredient.of(Items.STRING),
                new AspectList().add(ThaumcraftData.Aspects.TRAP, 2).add(ThaumcraftData.Aspects.CREATURE, 1),
                new ItemStackTemplate(Items.COBWEB));

        recipe(Recipes.Alchemy.HEDGE_LAVA.identifier(),
                Ingredient.of(Items.BUCKET),
                new AspectList().add(ThaumcraftData.Aspects.FIRE, 15).add(ThaumcraftData.Aspects.EARTH, 5),
                new ItemStackTemplate(Items.LAVA_BUCKET));

        ItemStackTemplate nitorStack = new ItemStackTemplate(ConfigBlocks.NITOR.item(),
                DataComponentPatch.builder().set(DataComponents.BASE_COLOR, DyeColor.YELLOW).build());
        recipe(Recipes.Alchemy.NITOR.identifier(),
                Ingredient.of(Items.GLOWSTONE_DUST),
                new AspectList().add(ThaumcraftData.Aspects.POWER, 10).add(ThaumcraftData.Aspects.FIRE, 10).add(ThaumcraftData.Aspects.LIGHT, 10),
                nitorStack);

        for (DyeColor color : DyeColor.values()) {
            ItemStackTemplate dyedNitor = new ItemStackTemplate(ConfigBlocks.NITOR.item(),
                    DataComponentPatch.builder().set(DataComponents.BASE_COLOR, color).build());
            recipe(Thaumcraft.id("nitor_dye_" + color.getSerializedName()),
                    Ingredient.of(items.get(color.getTag()).get()),
                    new AspectList().add(ThaumcraftData.Aspects.SENSE, 5).add(ThaumcraftData.Aspects.LIGHT, 5),
                    dyedNitor);
        }

        visCrystalRecipe(ThaumcraftData.Aspects.AIR);
        visCrystalRecipe(ThaumcraftData.Aspects.EARTH);
        visCrystalRecipe(ThaumcraftData.Aspects.FIRE);
        visCrystalRecipe(ThaumcraftData.Aspects.WATER);
        visCrystalRecipe(ThaumcraftData.Aspects.ORDER);
        visCrystalRecipe(ThaumcraftData.Aspects.CHAOS);

        visCrystalRecipe(ThaumcraftData.Aspects.EMPTY);
        visCrystalRecipe(ThaumcraftData.Aspects.LIGHT);
        visCrystalRecipe(ThaumcraftData.Aspects.MOVEMENT);
        visCrystalRecipe(ThaumcraftData.Aspects.ICE);
        visCrystalRecipe(ThaumcraftData.Aspects.CRYSTAL);
        visCrystalRecipe(ThaumcraftData.Aspects.METAL);
        visCrystalRecipe(ThaumcraftData.Aspects.LIFE);
        visCrystalRecipe(ThaumcraftData.Aspects.DEATH);
        visCrystalRecipe(ThaumcraftData.Aspects.POWER);
        visCrystalRecipe(ThaumcraftData.Aspects.CHANGE);

        visCrystalRecipe(ThaumcraftData.Aspects.MAGIC);
        visCrystalRecipe(ThaumcraftData.Aspects.AURA);
        visCrystalRecipe(ThaumcraftData.Aspects.ALCHEMY);
        visCrystalRecipe(ThaumcraftData.Aspects.TAINT);

        visCrystalRecipe(ThaumcraftData.Aspects.DARKNESS);
        visCrystalRecipe(ThaumcraftData.Aspects.ALIEN);
        visCrystalRecipe(ThaumcraftData.Aspects.FLIGHT);
        visCrystalRecipe(ThaumcraftData.Aspects.PLANT);

        visCrystalRecipe(ThaumcraftData.Aspects.TOOL);
        visCrystalRecipe(ThaumcraftData.Aspects.CRAFT);
        visCrystalRecipe(ThaumcraftData.Aspects.MACHINE);
        visCrystalRecipe(ThaumcraftData.Aspects.TRAP);

        visCrystalRecipe(ThaumcraftData.Aspects.SPIRIT);
        visCrystalRecipe(ThaumcraftData.Aspects.MIND);
        visCrystalRecipe(ThaumcraftData.Aspects.SENSE);
        visCrystalRecipe(ThaumcraftData.Aspects.AVERSION);
        visCrystalRecipe(ThaumcraftData.Aspects.ARMOR);
        visCrystalRecipe(ThaumcraftData.Aspects.DESIRE);

        visCrystalRecipe(ThaumcraftData.Aspects.UNDEAD);
        visCrystalRecipe(ThaumcraftData.Aspects.CREATURE);
        visCrystalRecipe(ThaumcraftData.Aspects.HUMAN);
    }

    private void visCrystalRecipe(ResourceKey<Aspect> aspectKey) {
        Holder<Aspect> aspectHolder = new FallbackHolder<>(aspectKey, Aspect.UNKNOWN);
        ItemStackTemplate crystalStack = new ItemStackTemplate(ConfigItems.VIS_CRYSTAL.get(),
                DataComponentPatch.builder().set(ConfigItemComponents.ASPECT_HOLDER.value(), aspectHolder).build());
        Identifier recipeId = Thaumcraft.id("vis_crystal_" + aspectKey.identifier().getPath());
        recipe(recipeId,
                Ingredient.of(ConfigItems.NUGGET_QUARTZ.get()),
                new AspectList().add(aspectKey, 2),
                crystalStack);
    }

    @Override
    protected void processJson(JsonElement element) {
        element.getAsJsonObject().addProperty("type", Recipes.Types.ALCHEMY.identifier().toString());
    }

    private void recipe(Identifier id, Ingredient catalyst, AspectList aspects, ItemStackTemplate result) {
        register(id, new AlchemyRecipe(catalyst, aspects, result));
    }
}
