package art.arcane.thaumcraft.items.equipment;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftMaterials;
import art.arcane.thaumcraft.client.rendering.CuboidRenderer;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.Level;
import tld.unknown.baubles.api.BaubleRenderContext;
import tld.unknown.baubles.api.BaubleType;
import tld.unknown.baubles.api.Baubles;
import tld.unknown.baubles.api.IBaubleRenderer;

import java.util.List;

public class GogglesItem extends Item implements IBaubleRenderer<BaubleRenderContext> {

	private static final float HEAD_SIZE = IBaubleRenderer.Helper.pixelToUnit(8);
	private static final Identifier TEXTURE = Thaumcraft.id("textures/entity/equipment/humanoid/goggles.png");

	private static final CuboidRenderer RENDERER = new CuboidRenderer(HEAD_SIZE, HEAD_SIZE, HEAD_SIZE, 64, 32, null).setCubeUVs(0, 0);

	public GogglesItem(Properties properties) {
		super(properties
				.humanoidArmor(ThaumcraftMaterials.Armor.GOGGLE, ArmorType.HELMET)
				.rarity(Rarity.RARE)
				.component(ConfigItemComponents.GOGGLE_SIGHT.value(), Unit.INSTANCE)
				.component(Baubles.COMPONENT_BAUBLE, List.of(BaubleType.HEAD))
				.component(ConfigItemComponents.VIS_COST_MODIFIER.value(), -0.05F));
	}

	@Override
	public BaubleRenderContext prepareRenderState(ItemStack itemStack, BaubleType baubleType, Avatar avatar, Level level) {
		return BaubleRenderContext.create(itemStack, baubleType);
	}

	@Override
	public void renderHead(PoseStack pose, SubmitNodeCollector nodeCollector, AvatarRenderState avatarState, BaubleRenderContext context) {
		pose.pushPose();

		pose.scale(1.1F, -1.1F, 1.1F);
		pose.translate(-HEAD_SIZE / 2, 0, -HEAD_SIZE / 2);
		nodeCollector.submitCustomGeometry(pose, RenderTypes.entityCutout(TEXTURE), (matrix, consumer) -> {
			RENDERER.draw(consumer, matrix, 0xFFFFFFFF, true, avatarState.lightCoords, true, avatarState.hasRedOverlay ? OverlayTexture.RED_OVERLAY_V : OverlayTexture.NO_OVERLAY);
		});

		pose.popPose();
	}
}
