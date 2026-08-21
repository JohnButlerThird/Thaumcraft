package art.arcane.thaumcraft.events.client.handlers;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.client.rendering.FancyArmorLayer;
import art.arcane.thaumcraft.client.rendering.ber.*;
import art.arcane.thaumcraft.client.rendering.entity.models.ArmorCrimsonLeader;
import art.arcane.thaumcraft.client.rendering.entity.models.ArmorCrimsonPlate;
import art.arcane.thaumcraft.client.rendering.entity.models.ArmorFortress;
import art.arcane.thaumcraft.client.rendering.entity.models.ArmorRobe;
import art.arcane.thaumcraft.client.rendering.ui.AspectTooltip;
import art.arcane.thaumcraft.client.screens.ArcaneWorkbenchScreen;
import art.arcane.thaumcraft.client.tints.AspectItemTintSource;
import art.arcane.thaumcraft.client.tints.DyeItemTintSource;
import art.arcane.thaumcraft.registries.*;
import art.arcane.thaumcraft.util.RegistryUtils;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;

import java.util.List;
import java.util.function.Function;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, value = Dist.CLIENT)
public class RegistrationEvents {

	@SubscribeEvent
	public static void onMenuScreenRegister(RegisterMenuScreensEvent e) {
		e.register(ConfigMenus.ARCANE_WORKBENCH.get(), ArcaneWorkbenchScreen::new);
	}

	@SubscribeEvent
	public static void onTooltipComponentRegister(RegisterClientTooltipComponentFactoriesEvent e) {
		e.register(AspectTooltip.Data.class, d -> new AspectTooltip(d.aspects()));
	}

	@SubscribeEvent
	public static void onBlockEntityRendererRegister(EntityRenderersEvent.RegisterRenderers e) {
		e.registerBlockEntityRenderer(ConfigBlockEntities.CRUCIBLE.entityType(), ctx -> new CrucibleBER());
		e.registerBlockEntityRenderer(ConfigBlockEntities.RUNIC_MATRIX.entityType(), RunicMatrixBER::new);
		e.registerBlockEntityRenderer(ConfigBlockEntities.PEDESTAL.entityType(), PedestalBER::new);
		e.registerBlockEntityRenderer(ConfigBlockEntities.CREATIVE_ASPECT_SOURCE.entityType(), ctx -> new CreativeAspectSourceBER());
		e.registerBlockEntityRenderer(ConfigBlockEntities.JAR.entityType(), ctx -> new JarBER());
		e.registerBlockEntityRenderer(ConfigBlockEntities.DIOPTRA.entityType(), ctx -> new DioptraBER());
		e.registerBlockEntityRenderer(ConfigBlockEntities.HUNGRY_CHEST.entityType(), ctx -> new SingleChestBER(ctx, ThaumcraftData.Blocks.HUNGRY_CHEST));

		//e.registerEntityRenderer(ConfigEntities.LIVING_TRUNK.entityType(), TrunkEntityRenderer::new);
		e.registerEntityRenderer(ConfigEntities.MOVING_ITEM.entityType(), ItemEntityRenderer::new);
	}

	@SubscribeEvent
	public static void onBlockColorTintingRegister(RegisterColorHandlersEvent.BlockTintSources e) {
		ConfigBlocks.CRYSTAL_COLONY.forEach((aspect, block) -> {
			e.register(List.of(state -> ConfigDataRegistries.ASPECTS.get(RegistryUtils.access(), aspect.getId()).colour().argb32(true)), block.block());
		});

		e.register(List.of(new BlockTintSource() {
			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return BiomeColors.getAverageFoliageColor(level, pos);
			}

			@Override
			public int color(BlockState state) {
				return 0x48B518;
			}
		}), ConfigBlocks.GREATWOOD_LEAVES.block());
	}

	@SubscribeEvent
	public static void onItemColorTintingRegister(RegisterColorHandlersEvent.ItemTintSources e) {
		e.register(ThaumcraftData.TintSources.ASPECT_ITEM, AspectItemTintSource.CODEC);
		e.register(ThaumcraftData.TintSources.DYED_ITEM, DyeItemTintSource.CODEC);
	}

	/*@SubscribeEvent
	public static void onParticleProvidersRegister(RegisterParticleProvidersEvent e) {
		e.registerSpriteSet(ConfigParticles.SMOKE_SPIRAL.get(), SmokeSpiralParticle.Provider::new);
	}*/ //TODO: Rendering - Particles

	@SubscribeEvent
	public static void onEntityLayerRegister(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ArmorCrimsonLeader.LAYER_LOCATION, ArmorCrimsonLeader::createBodyLayer);
		event.registerLayerDefinition(ArmorCrimsonPlate.LAYER_LOCATION, ArmorCrimsonPlate::createBodyLayer);
		event.registerLayerDefinition(ArmorRobe.LAYER_LOCATION, ArmorRobe::createBodyLayer);
		event.registerLayerDefinition(ArmorFortress.LAYER_LOCATION, ArmorFortress::createBodyLayer);
	}

	@SubscribeEvent
	public static void onEntityLayerRendererRegister(EntityRenderersEvent.AddLayers event) {
		addLayerToPlayerSkin(event, r -> new FancyArmorLayer<>(r, event.getEntityModels()));

		addLayerToHumanoid(event, EntityType.ARMOR_STAND, r -> new FancyArmorLayer<>(r, event.getEntityModels()));
		addLayerToHumanoid(event, EntityType.ZOMBIE, r -> new FancyArmorLayer<>(r, event.getEntityModels()));
		addLayerToHumanoid(event, EntityType.SKELETON, r -> new FancyArmorLayer<>(r, event.getEntityModels()));
		addLayerToHumanoid(event, EntityType.HUSK, r -> new FancyArmorLayer<>(r, event.getEntityModels()));
		addLayerToHumanoid(event, EntityType.DROWNED, r -> new FancyArmorLayer<>(r, event.getEntityModels()));
		addLayerToHumanoid(event, EntityType.STRAY, r -> new FancyArmorLayer<>(r, event.getEntityModels()));
	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	private static <E extends Player, S extends HumanoidRenderState, M extends HumanoidModel<S>> void addLayerToPlayerSkin(EntityRenderersEvent.AddLayers event, Function<LivingEntityRenderer<E, S, M>, ? extends RenderLayer<S, M>> factory) {
		EntityRenderer wide = event.getPlayerRenderer(PlayerModelType.WIDE);
		if (wide instanceof LivingEntityRenderer ler)
			ler.addLayer(factory.apply(ler));

		EntityRenderer slim = event.getPlayerRenderer(PlayerModelType.SLIM);
		if (slim instanceof LivingEntityRenderer ler)
			ler.addLayer(factory.apply(ler));
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private static <E extends LivingEntity, S extends HumanoidRenderState, M extends HumanoidModel<S>> void addLayerToHumanoid(EntityRenderersEvent.AddLayers event, EntityType<E> entityType, Function<LivingEntityRenderer<E, S, M>, ? extends RenderLayer<S, M>> factory) {
		EntityRenderer<E, S> renderer = event.getRenderer(entityType);
		if (renderer instanceof LivingEntityRenderer ler)
			ler.addLayer(factory.apply(ler));
	}
}
