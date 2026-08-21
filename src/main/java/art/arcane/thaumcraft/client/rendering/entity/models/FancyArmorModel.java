package art.arcane.thaumcraft.client.rendering.entity.models;

import art.arcane.thaumcraft.api.ThaumcraftMaterials;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lombok.Getter;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.equipment.ArmorMaterial;

public abstract class FancyArmorModel<S extends HumanoidRenderState> extends HumanoidModel<S> {

	public final ModelPart leggings;

	@Getter
	private final Identifier texture, overlay;
	@Getter
	private final boolean hasOverlay;

	public FancyArmorModel(ModelPart root, ArmorMaterial material, boolean hasOverlay) {
		super(root);
		this.leggings = root.getChild("leggings");
		this.texture = material.assetId().identifier().withPrefix("textures/entity/equipment/humanoid/").withSuffix(".png");
		this.overlay = material.assetId().identifier().withPrefix("textures/entity/equipment/humanoid/").withSuffix("_overlay.png");
		this.hasOverlay = hasOverlay;
	}

	public void setAllVisible(boolean visible) {
		this.head.visible = this.hat.visible = visible;
		this.body.visible = visible;
		this.leggings.visible = visible;
		this.leftArm.visible = this.rightArm.visible = visible;
		this.leftLeg.visible = this.rightLeg.visible = visible;
	}

	public void setVisible(EquipmentSlot slot, S state) {
		switch(slot) {
			case HEAD -> {
				this.head.visible = true;
				this.hat.visible = true;
				enableHead(state);
			}
			case CHEST -> {
				this.body.visible = true;
				this.leftArm.visible = true;
				this.rightArm.visible = true;
				enableChest(state);
			}
			case LEGS -> {
				this.leftLeg.visible = true;
				this.rightLeg.visible = true;
				this.leggings.visible = true;
				enableLegs(state);
			}
		}
	}

	public void render(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay) {
		renderToBuffer(poseStack, buffer, packedLight, packedOverlay);
	}

	public void renderOverlay(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay) { }

	protected void enableHead(S state) { }
	protected void enableChest(S state) { }
	protected void enableLegs(S state) { }
}
