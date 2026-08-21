package art.arcane.thaumcraft.client.rendering.entity.models;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftMaterials;
import art.arcane.thaumcraft.util.Colour;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.DyedItemColor;

public class ArmorRobe<S extends HumanoidRenderState> extends FancyArmorModel<S> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Thaumcraft.id("armor_robe"), "main");

	public static final int VOID_DEFAULT_COLOUR = Colour.fromHex("#6A3880").argb32(true);

	private final ModelPart shoulderplate_r;
	private final ModelPart shoulderplate_l;
	private final ModelPart cloth_front_l;
	private final ModelPart cloth_side_l;
	private final ModelPart cloth_back_l;
	private final ModelPart legplate_l;
	private final ModelPart cloth_front_r;
	private final ModelPart cloth_side_r;
	private final ModelPart cloth_back_r;
	private final ModelPart legplate_r;

	private int headColour, chestColour, legsColour;

	public ArmorRobe(ModelPart root, boolean isVoid) {
		super(root, isVoid ? ThaumcraftMaterials.Armor.VOID_ROBE : ThaumcraftMaterials.Armor.CRIMSON_ROBE, isVoid);
		this.shoulderplate_r = this.leftArm.getChild("shoulderplate_r");
		this.shoulderplate_l = this.rightArm.getChild("shoulderplate_l");
		this.cloth_front_l = this.leftLeg.getChild("cloth_front_l");
		this.cloth_side_l = this.leftLeg.getChild("cloth_side_l");
		this.cloth_back_l = this.leftLeg.getChild("cloth_back_l");
		this.legplate_l = this.leftLeg.getChild("legplate_l");
		this.cloth_front_r = this.rightLeg.getChild("cloth_front_r");
		this.cloth_side_r = this.rightLeg.getChild("cloth_side_r");
		this.cloth_back_r = this.rightLeg.getChild("cloth_back_r");
		this.legplate_r = this.rightLeg.getChild("legplate_r");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(16, 7).addBox(-4.0F, -9.0F, -5.0F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.0F, 0.5F));

		PartDefinition hood_4_r1 = hat.addOrReplaceChild("hood_4_r1", CubeListBuilder.create().texOffs(52, 15).addBox(-3.0F, -2.5F, -4.5F, 6.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -2.4019F, 10.866F, -0.5236F, 0.0F, 0.0F));

		PartDefinition hood_3_r1 = hat.addOrReplaceChild("hood_3_r1", CubeListBuilder.create().texOffs(52, 14).addBox(-3.5F, -2.0F, -3.0F, 7.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -5.0F, 8.5F, -0.3491F, 0.0F, 0.0F));

		PartDefinition hood_2_r1 = hat.addOrReplaceChild("hood_2_r1", CubeListBuilder.create().texOffs(52, 13).addBox(-4.0F, -4.0F, -2.75F, 8.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -4.5F, 5.5F, -0.2182F, 0.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 25).addBox(-4.0F, 1.0F, -3.0F, 8.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(36, 45).addBox(-4.0F, 1.0F, 2.0F, 8.0F, 11.0F, 2.0F, new CubeDeformation(0.02F))
		.texOffs(56, 50).addBox(-2.5F, 1.0F, -4.0F, 5.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(108, 38).mirror().addBox(2.1F, 0.5F, -3.5F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(118, 16).addBox(-4.1F, 0.5F, -3.5F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 55).addBox(-4.0F, 7.0F, -3.0F, 8.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 36).addBox(4.0F, 8.0F, -3.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(16, 36).addBox(-5.0F, 8.0F, -3.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition book_r1 = body.addOrReplaceChild("book_r1", CubeListBuilder.create().texOffs(81, 16).addBox(-2.5F, -3.5F, -1.0F, 5.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 5.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition scroll_r1 = body.addOrReplaceChild("scroll_r1", CubeListBuilder.create().texOffs(78, 25).addBox(-4.0F, -1.5F, 0.0F, 8.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.25F, 4.0F, 0.0F, 0.0F, 0.2182F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(16, 45).addBox(-1.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(88, 39).addBox(-1.5F, 2.5F, -2.5F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(76, 32).addBox(-1.0F, 5.5F, 2.5F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(88, 32).addBox(-0.5F, 3.5F, 2.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition shoulderplate_r = left_arm.addOrReplaceChild("shoulderplate_r", CubeListBuilder.create().texOffs(56, 25).addBox(0.0F, -1.0F, -3.5F, 2.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(56, 33).addBox(0.0F, 0.0F, -3.5F, 1.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(40, 33).addBox(-1.0F, 3.0F, -3.5F, 1.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(40, 33).addBox(-2.0F, 5.0F, -3.5F, 1.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -2.5F, 0.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(16, 45).mirror().addBox(-3.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(88, 39).mirror().addBox(-3.5F, 2.5F, -2.5F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(76, 32).addBox(-3.0F, 5.5F, 2.5F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(88, 32).addBox(-2.5F, 3.5F, 2.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition shoulderplate_l = right_arm.addOrReplaceChild("shoulderplate_l", CubeListBuilder.create().texOffs(56, 25).addBox(-2.0F, -1.0F, -3.5F, 2.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(56, 33).addBox(-1.0F, 0.0F, -3.5F, 1.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(40, 33).addBox(0.0F, 3.0F, -3.5F, 1.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(40, 33).addBox(1.0F, 5.0F, -3.5F, 1.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, -2.5F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition leggings = partdefinition.addOrReplaceChild("leggings", CubeListBuilder.create().texOffs(16, 55).addBox(-4.0F, 7.0F, -3.0F, 8.0F, 5.0F, 1.0F, new CubeDeformation(0.02F))
		.texOffs(16, 36).addBox(4.0F, 8.0F, -3.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.02F))
		.texOffs(16, 36).addBox(-5.0F, 8.0F, -3.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition belt_back_r1 = leggings.addOrReplaceChild("belt_back_r1", CubeListBuilder.create().texOffs(16, 55).addBox(-4.0F, -2.5F, -0.5F, 8.0F, 5.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 9.5F, 3.5F, 0.0F, 3.1416F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, 12.0F, 0.0F));

		PartDefinition cloth_front_l = left_leg.addOrReplaceChild("cloth_front_l", CubeListBuilder.create(), PartPose.offset(1.0F, 0.0F, -3.9F));

		PartDefinition cloth_front_l2_r1 = cloth_front_l.addOrReplaceChild("cloth_front_l2_r1", CubeListBuilder.create().texOffs(108, 47).mirror().addBox(0.0F, -5.0F, -3.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 13.3387F, 1.2331F, -0.3491F, 0.0F, 0.0F));

		PartDefinition cloth_front_l1_r1 = cloth_front_l.addOrReplaceChild("cloth_front_l1_r1", CubeListBuilder.create().texOffs(108, 38).mirror().addBox(0.0F, -13.0F, -3.0F, 3.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 13.1378F, 2.5302F, -0.1047F, 0.0F, 0.0F));

		PartDefinition cloth_side_l = left_leg.addOrReplaceChild("cloth_side_l", CubeListBuilder.create(), PartPose.offset(10.5F, 3.0F, 1.0F));

		PartDefinition foci_pouch_r1 = cloth_side_l.addOrReplaceChild("foci_pouch_r1", CubeListBuilder.create().texOffs(100, 20).addBox(-0.5F, -2.2F, -2.5F, 3.0F, 6.0F, 5.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(-6.153F, -0.327F, -0.5F, 0.0F, 0.0F, -0.1222F));

		PartDefinition cloth_side_l3_r1 = cloth_side_l.addOrReplaceChild("cloth_side_l3_r1", CubeListBuilder.create().texOffs(116, 34).addBox(-0.5F, -1.5F, -2.5F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.3305F, 5.8807F, -0.5F, 0.0F, 0.0F, -0.5236F));

		PartDefinition cloth_side_l2_r1 = cloth_side_l.addOrReplaceChild("cloth_side_l2_r1", CubeListBuilder.create().texOffs(116, 34).addBox(-0.5F, -1.5F, -2.5F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.4739F, 3.251F, -0.5F, 0.0F, 0.0F, -0.2967F));

		PartDefinition cloth_side_l1_r1 = cloth_side_l.addOrReplaceChild("cloth_side_l1_r1", CubeListBuilder.create().texOffs(116, 42).addBox(-0.5F, -2.5F, -2.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.1991F, -0.5796F, -0.5F, 0.0F, 0.0F, -0.1222F));

		PartDefinition sideplate_l_r1 = cloth_side_l.addOrReplaceChild("sideplate_l_r1", CubeListBuilder.create().texOffs(116, 25).addBox(-0.5F, -2.0F, -1.5F, 1.0F, 4.0F, 5.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-7.7016F, -1.3987F, -1.5F, 0.0F, 0.0F, -0.4363F));

		PartDefinition cloth_back_l = left_leg.addOrReplaceChild("cloth_back_l", CubeListBuilder.create(), PartPose.offset(5.0F, -0.5F, 11.9F));

		PartDefinition cloth_back_l3_r1 = cloth_back_l.addOrReplaceChild("cloth_back_l3_r1", CubeListBuilder.create().texOffs(120, 12).mirror().addBox(-6.0F, -5.0F, 2.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 13.7779F, -8.8878F, 0.2269F, 0.0F, 0.0F));

		PartDefinition cloth_back_l2_r1 = cloth_back_l.addOrReplaceChild("cloth_back_l2_r1", CubeListBuilder.create().texOffs(123, 9).addBox(-7.0F, -5.0F, 2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 13.7779F, -8.8878F, 0.2269F, 0.0F, 0.0F));

		PartDefinition cloth_back_l1_r1 = cloth_back_l.addOrReplaceChild("cloth_back_l1_r1", CubeListBuilder.create().texOffs(118, 16).addBox(-7.0F, -13.0F, 2.0F, 4.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 13.6378F, -9.5302F, 0.1047F, 0.0F, 0.0F));

		PartDefinition legplate_l = left_leg.addOrReplaceChild("legplate_l", CubeListBuilder.create().texOffs(76, 38).mirror().addBox(0.0F, 18.004F, 3.9819F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(82, 38).mirror().addBox(0.0F, 20.004F, 4.9819F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(76, 42).mirror().addBox(0.0F, 22.004F, 5.9819F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, -20.0F, 0.75F, -0.4363F, 0.0F, 0.0F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, 12.0F, 0.0F));

		PartDefinition cloth_front_r = right_leg.addOrReplaceChild("cloth_front_r", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, -10.9F));

		PartDefinition cloth_front_r2_r1 = cloth_front_r.addOrReplaceChild("cloth_front_r2_r1", CubeListBuilder.create().texOffs(108, 47).addBox(-2.0F, -5.0F, -3.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 10.3387F, 8.2331F, -0.3491F, 0.0F, 0.0F));

		PartDefinition cloth_front_r1_r1 = cloth_front_r.addOrReplaceChild("cloth_front_r1_r1", CubeListBuilder.create().texOffs(108, 38).addBox(-2.0F, -13.0F, -3.0F, 3.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 10.1378F, 9.5302F, -0.1047F, 0.0F, 0.0F));

		PartDefinition cloth_side_r = right_leg.addOrReplaceChild("cloth_side_r", CubeListBuilder.create(), PartPose.offset(-3.5F, 3.0F, 1.0F));

		PartDefinition cloth_side_r3_r1 = cloth_side_r.addOrReplaceChild("cloth_side_r3_r1", CubeListBuilder.create().texOffs(116, 34).addBox(-0.5F, -1.5F, -2.5F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.6695F, 5.8807F, -0.5F, 0.0F, 0.0F, 0.5236F));

		PartDefinition cloth_side_r2_r1 = cloth_side_r.addOrReplaceChild("cloth_side_r2_r1", CubeListBuilder.create().texOffs(116, 34).addBox(-0.5F, -1.5F, -2.5F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4739F, 3.251F, -0.5F, 0.0F, 0.0F, 0.2967F));

		PartDefinition cloth_side_r1_r1 = cloth_side_r.addOrReplaceChild("cloth_side_r1_r1", CubeListBuilder.create().texOffs(116, 42).addBox(-0.5F, -2.5F, -2.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.1991F, -0.5796F, -0.5F, 0.0F, 0.0F, 0.1222F));

		PartDefinition sideplate_r_r1 = cloth_side_r.addOrReplaceChild("sideplate_r_r1", CubeListBuilder.create().texOffs(116, 25).mirror().addBox(-0.5F, -2.0F, -1.5F, 1.0F, 4.0F, 5.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(0.7016F, -1.3987F, -1.5F, 0.0F, 0.0F, 0.4363F));

		PartDefinition cloth_back_r = right_leg.addOrReplaceChild("cloth_back_r", CubeListBuilder.create(), PartPose.offset(2.0F, -0.5F, 11.9F));

		PartDefinition cloth_back_r3_r1 = cloth_back_r.addOrReplaceChild("cloth_back_r3_r1", CubeListBuilder.create().texOffs(120, 12).addBox(0.0F, -5.0F, 2.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(123, 9).mirror().addBox(3.0F, -5.0F, 2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 13.7779F, -8.8878F, 0.2269F, 0.0F, 0.0F));

		PartDefinition cloth_back_r1_r1 = cloth_back_r.addOrReplaceChild("cloth_back_r1_r1", CubeListBuilder.create().texOffs(118, 16).mirror().addBox(0.0F, -13.0F, 2.0F, 4.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 13.6378F, -9.5302F, 0.1047F, 0.0F, 0.0F));

		PartDefinition legplate_r = right_leg.addOrReplaceChild("legplate_r", CubeListBuilder.create().texOffs(76, 38).addBox(2.0F, 14.4844F, 2.0648F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(82, 38).addBox(2.0F, 16.4844F, 3.0648F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(76, 42).addBox(2.0F, 18.4844F, 4.0648F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -16.0F, 1.0F, -0.4363F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void renderOverlay(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay) {
		this.head.render(poseStack, buffer, packedLight, packedOverlay, headColour);
		// Chest
		this.body.render(poseStack, buffer, packedLight, packedOverlay, chestColour);
		this.leftArm.render(poseStack, buffer, packedLight, packedOverlay, chestColour);
		this.rightArm.render(poseStack, buffer, packedLight, packedOverlay, chestColour);
		// Legs
		this.leggings.render(poseStack, buffer, packedLight, packedOverlay, legsColour);
		this.leftLeg.render(poseStack, buffer, packedLight, packedOverlay, legsColour);
		this.rightLeg.render(poseStack, buffer, packedLight, packedOverlay, legsColour);
	}

	@Override
	protected void enableHead(S state) {
		if(state.headEquipment.has(DataComponents.DYED_COLOR))
			this.headColour = state.headEquipment.get(DataComponents.DYED_COLOR).rgb();
		else
			this.headColour = ArmorRobe.VOID_DEFAULT_COLOUR;
	}

	@Override
	protected void enableChest(S state) {
		if(state.chestEquipment.has(DataComponents.DYED_COLOR))
			this.chestColour = state.chestEquipment.get(DataComponents.DYED_COLOR).rgb();
		else
			this.chestColour = ArmorRobe.VOID_DEFAULT_COLOUR;
	}

	@Override
	protected void enableLegs(S state) {
		if(state.legsEquipment.has(DataComponents.DYED_COLOR))
			this.legsColour = state.legsEquipment.get(DataComponents.DYED_COLOR).rgb();
		else
			this.legsColour = ArmorRobe.VOID_DEFAULT_COLOUR;
	}
}