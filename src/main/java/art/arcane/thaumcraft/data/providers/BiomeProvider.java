package art.arcane.thaumcraft.data.providers;

import art.arcane.thaumcraft.Thaumcraft;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.attribute.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public final class BiomeProvider {

    public static final ResourceKey<Biome> MAGICAL_FOREST = ResourceKey.create(Registries.BIOME, Thaumcraft.id("magical_forest"));

    public static void bootstrap(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        context.register(MAGICAL_FOREST, magicalForest(placedFeatures, carvers));
    }

    private static Biome magicalForest(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder mobSpawns = new MobSpawnSettings.Builder();
        mobSpawns.creatureGenerationProbability(0.1F);

        mobSpawns.addSpawn(MobCategory.CREATURE, 2, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 1, 3));
        mobSpawns.addSpawn(MobCategory.CREATURE, 2, new MobSpawnSettings.SpawnerData(EntityType.HORSE, 1, 3));

        mobSpawns.addSpawn(MobCategory.MONSTER, 100, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 4, 4));
        mobSpawns.addSpawn(MobCategory.MONSTER, 95, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 4, 4));
        mobSpawns.addSpawn(MobCategory.MONSTER, 100, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 4, 4));
        mobSpawns.addSpawn(MobCategory.MONSTER, 100, new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 4, 4));
        mobSpawns.addSpawn(MobCategory.MONSTER, 3, new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1));
        mobSpawns.addSpawn(MobCategory.MONSTER, 3, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 1));
        mobSpawns.addSpawn(MobCategory.MONSTER, 1, new MobSpawnSettings.SpawnerData(EntityType.VEX,1, 1));

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);

        generation.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, WorldgenProvider.PlacedFeatures.MAGICAL_FOREST_MOSSY_ROCK);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WorldgenProvider.PlacedFeatures.MAGICAL_FOREST_TREES);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WorldgenProvider.PlacedFeatures.MAGICAL_FOREST_GRASS);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WorldgenProvider.PlacedFeatures.MAGIC_FOREST_VISHROOM);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WorldgenProvider.PlacedFeatures.MAGICAL_FOREST_FLOWERS);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WorldgenProvider.PlacedFeatures.MAGICAL_FOREST_LILY_PAD);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WorldgenProvider.PlacedFeatures.SHIMMERLEAF);


        BiomeSpecialEffects.Builder effects = new BiomeSpecialEffects.Builder()
            .waterColor(30702)
            .grassColorOverride(5635969)
            .foliageColorOverride(6750149);

        return new Biome.BiomeBuilder()
            .putAttributes(EnvironmentAttributeMap.builder()
                    .set(EnvironmentAttributes.FOG_COLOR, 12638463)
                    .set(EnvironmentAttributes.WATER_FOG_COLOR, 30702)
                    .set(EnvironmentAttributes.SKY_COLOR, 7972607)
                    .set(EnvironmentAttributes.AMBIENT_PARTICLES, AmbientParticle.of(ParticleTypes.END_ROD, 0.000354F))
                    .set(EnvironmentAttributes.AMBIENT_SOUNDS, AmbientSounds.LEGACY_CAVE_SETTINGS)
                    .build())
            .hasPrecipitation(true)
            .temperature(0.8F)
            .downfall(0.4F)
            .specialEffects(effects.build())
            .mobSpawnSettings(mobSpawns.build())
            .generationSettings(generation.build())
            .build();
    }
}
