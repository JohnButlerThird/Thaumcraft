package art.arcane.thaumcraft.client.rendering;

import art.arcane.thaumcraft.client.rendering.entity.models.*;
import art.arcane.thaumcraft.items.FancyArmorItem;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import art.arcane.thaumcraft.registries.ConfigItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

import java.util.HashMap;
import java.util.Map;

public class FancyArmorLayer<S extends HumanoidRenderState, M extends HumanoidModel<S>> extends RenderLayer<S, M> {

	private final Map<FancyArmorItem.ArmorSet, FancyArmorModel<S>> models;

	public FancyArmorLayer(RenderLayerParent<S, M> renderer, EntityModelSet modelSet) {
		super(renderer);
		models = new HashMap<>();
		models.put(FancyArmorItem.ArmorSet.CRIMSON_LEADER, new ArmorCrimsonLeader<>(modelSet.bakeLayer(ArmorCrimsonLeader.LAYER_LOCATION)));
		models.put(FancyArmorItem.ArmorSet.CRIMSON_PLATE, new ArmorCrimsonPlate<>(modelSet.bakeLayer(ArmorCrimsonPlate.LAYER_LOCATION)));
		models.put(FancyArmorItem.ArmorSet.CRIMSON_ROBE, new ArmorRobe<>(modelSet.bakeLayer(ArmorRobe.LAYER_LOCATION), false));
		models.put(FancyArmorItem.ArmorSet.VOID_ROBE, new ArmorRobe<>(modelSet.bakeLayer(ArmorRobe.LAYER_LOCATION), true));
		models.put(FancyArmorItem.ArmorSet.FORTRESS, new ArmorFortress<>(modelSet.bakeLayer(ArmorFortress.LAYER_LOCATION)));
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
		updateArmorState(state);
		models.values().forEach(model -> submitArmor(model, poseStack, submitNodeCollector, lightCoords));
	}

	private void submitArmor(FancyArmorModel<S> armor, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
		poseStack.pushPose();
		Identifier texture = armor.getTexture();
		submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.armorCutoutNoCull(texture), (pose, buffer) -> {
			armor.render(poseStack, buffer, lightCoords, OverlayTexture.NO_OVERLAY);
		});
		if(armor.isHasOverlay()) {
			Identifier textureOverlay = armor.getOverlay();
			submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.armorCutoutNoCull(textureOverlay), (pose, buffer) -> {
				armor.renderOverlay(poseStack, buffer, lightCoords, OverlayTexture.NO_OVERLAY);
			});
		}
		poseStack.popPose();
	}

	private void updateArmorState(S renderState) {
		models.values().forEach(model -> model.setAllVisible(false));
		checkArmor(EquipmentSlot.HEAD, renderState);
		checkArmor(EquipmentSlot.CHEST, renderState);
		checkArmor(EquipmentSlot.LEGS, renderState);
		models.values().forEach(model -> model.setupAnim(renderState));
	}

	private void checkArmor(EquipmentSlot type, S renderState) {
		ItemStack itemStack;
		switch(type) {
			case HEAD: itemStack = renderState.headEquipment; break;
			case CHEST: itemStack = renderState.chestEquipment; break;
			case LEGS: itemStack = renderState.legsEquipment; break;
			default: return;
		}
		if(itemStack.getItem() instanceof FancyArmorItem f) {
			models.get(f.getSet()).setVisible(type, renderState);
		}
	}
}

