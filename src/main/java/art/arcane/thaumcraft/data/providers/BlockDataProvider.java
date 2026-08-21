package art.arcane.thaumcraft.data.providers;

import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.client.tints.DyeItemTintSource;
import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.Var;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelDispatcher;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.renderer.block.dispatch.multipart.CombinedCondition;
import net.minecraft.client.renderer.block.dispatch.multipart.Condition;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.client.model.generators.loaders.ObjModelBuilder;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplate;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;
import net.neoforged.neoforge.registries.DeferredBlock;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.blocks.CrystalBlock;
import art.arcane.thaumcraft.blocks.InfusionPillarBlock;
import art.arcane.thaumcraft.blocks.alchemy.CreativeAspectSourceBlock;
import art.arcane.thaumcraft.blocks.alchemy.EssentiaInputBlock;
import art.arcane.thaumcraft.blocks.alchemy.EssentiaOutputBlock;
import art.arcane.thaumcraft.blocks.alchemy.JarBlock;
import art.arcane.thaumcraft.blocks.alchemy.TubeBlock;
import art.arcane.thaumcraft.blocks.DioptraBlock;
import art.arcane.thaumcraft.blocks.LevitatorBlock;
import art.arcane.thaumcraft.client.tints.AspectItemTintSource;
import art.arcane.thaumcraft.registries.ConfigBlocks;
import art.arcane.thaumcraft.util.RegistryUtils;
import net.minecraft.client.color.item.Constant;

import java.util.*;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class BlockDataProvider extends ModelProvider {

    private BlockModelGenerators blocks;
    private ItemModelGenerators items;

    public BlockDataProvider(PackOutput gen) {
        super(gen, Thaumcraft.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.blocks = blockModels;
        this.items = itemModels;

        registerAxisTexturedBlock(ConfigBlocks.ARCANE_STONE, false);
        registerStairAndSlab(ConfigBlocks.ARCANE_STONE, ConfigBlocks.ARCANE_STONE_STAIRS, ConfigBlocks.ARCANE_STONE_SLAB, true);
        simpleBlock(ConfigBlocks.ARCANE_STONE_BRICK);
        registerStairAndSlab(ConfigBlocks.ARCANE_STONE_BRICK, ConfigBlocks.ARCANE_STONE_BRICK_STAIRS, ConfigBlocks.ARCANE_STONE_BRICK_SLAB, false);
        registerAxisTexturedBlock(ConfigBlocks.ANCIENT_STONE, true);
        registerStairAndSlab(ConfigBlocks.ANCIENT_STONE, ConfigBlocks.ANCIENT_STONE_STAIRS, ConfigBlocks.ANCIENT_STONE_SLAB, true);
        simpleBlock(ConfigBlocks.ANCIENT_STONE_TILE);
        registerStairAndSlab(ConfigBlocks.ANCIENT_STONE_TILE, ConfigBlocks.ANCIENT_STONE_TILE_STAIRS, ConfigBlocks.ANCIENT_STONE_TILE_SLAB, false);
        registerAxisTexturedBlock(ConfigBlocks.ELDRITCH_STONE, false);
        registerStairAndSlab(ConfigBlocks.ELDRITCH_STONE, ConfigBlocks.ELDRITCH_STONE_STAIRS, ConfigBlocks.ELDRITCH_STONE_SLAB, true);

        simpleExistingBlock(ConfigBlocks.CRUCIBLE);
        simpleExistingBlock(ConfigBlocks.ARCANE_WORKBENCH);
        batchSimpleExistingBlock(ConfigBlocks.ARCANE_PEDESTAL, ConfigBlocks.ANCIENT_PEDESTAL, ConfigBlocks.ELDRITCH_PEDESTAL);

        registerDirectionalMultipart(ConfigBlocks.TUBE, TubeBlock.BY_DIRECTION, Thaumcraft.id("block/tube_generic_center"), Thaumcraft.id("block/tube_generic_side"), true);
        registerDirectionalMultipart(ConfigBlocks.TUBE_VALVE, TubeBlock.BY_DIRECTION, Thaumcraft.id("block/tube_valve_center"), Thaumcraft.id("block/tube_generic_side"), false);
        registerDirectionalMultipart(ConfigBlocks.TUBE_FILTER, TubeBlock.BY_DIRECTION, Thaumcraft.id("block/tube_filter_center"), Thaumcraft.id("block/tube_generic_side"), false);
        registerDirectionalMultipart(ConfigBlocks.TUBE_RESTRICT, TubeBlock.BY_DIRECTION, Thaumcraft.id("block/tube_generic_center"), Thaumcraft.id("block/tube_generic_side"), true);
        registerDirectionalMultipart(ConfigBlocks.TUBE_ONEWAY, TubeBlock.BY_DIRECTION, Thaumcraft.id("block/tube_generic_center"), Thaumcraft.id("block/tube_generic_side"), true);
        registerDirectionalMultipart(ConfigBlocks.TUBE_BUFFER, TubeBlock.BY_DIRECTION, Thaumcraft.id("block/tube_generic_center"), Thaumcraft.id("block/tube_generic_side"), true);
        registerAllFacingBlock(ConfigBlocks.ESSENTIA_INPUT, EssentiaInputBlock.FACING);
        registerAllFacingBlock(ConfigBlocks.ESSENTIA_OUTPUT, EssentiaOutputBlock.FACING);

        registerJars();
        registerAspectSource();
        registerInfusionPillar(ConfigBlocks.INFUSION_PILLAR_ARCANE, RegistryUtils.getBlockLocation(ConfigBlocks.ARCANE_STONE.blockSupplier()).withSuffix("_0"));
        registerInfusionPillar(ConfigBlocks.INFUSION_PILLAR_ANCIENT, RegistryUtils.getBlockLocation(ConfigBlocks.ANCIENT_STONE.blockSupplier()).withSuffix("_0"));
        registerInfusionPillar(ConfigBlocks.INFUSION_PILLAR_ELDRITCH, RegistryUtils.getBlockLocation(ConfigBlocks.ELDRITCH_STONE.blockSupplier()).withSuffix("_0"));

        registerCrystalColonies();

        registerEmptyBlock(ConfigBlocks.RUNIC_MATRIX,
				RegistryUtils.getBlockLocation(ConfigBlocks.ARCANE_STONE_BRICK.blockSupplier()),
				RegistryUtils.getBlockItemLocation(ConfigBlocks.RUNIC_MATRIX.blockSupplier()));
        registerFakeBlock(ConfigBlocks.LAMPLIGHT);

        simpleBlock(ConfigBlocks.INFUSION_STONE_COST);
        simpleBlock(ConfigBlocks.INFUSION_STONE_SPEED);

        registerDioptra();
        registerLevitator();

        simpleBlock(ConfigBlocks.ORE_AMBER);
        simpleBlock(ConfigBlocks.ORE_CINNABAR);
        simpleBlock(ConfigBlocks.ORE_QUARTZ);
        simpleBlock(ConfigBlocks.DEEPSLATE_ORE_AMBER);
        simpleBlock(ConfigBlocks.DEEPSLATE_ORE_CINNABAR);
        simpleBlock(ConfigBlocks.DEEPSLATE_ORE_QUARTZ);

        simpleBlock(ConfigBlocks.METAL_BRASS);
        simpleBlock(ConfigBlocks.METAL_THAUMIUM);
        simpleBlock(ConfigBlocks.METAL_VOID);

        translucentColumnBlock(ConfigBlocks.AMBER_BLOCK);
        translucentBlock(ConfigBlocks.AMBER_BRICK);

        registerSilverwoodTree();
        registerGreatwoodTree();

        registerCrossBlock(ConfigBlocks.VISHROOM);
        registerCrossBlock(ConfigBlocks.CINDERPEARL);
        registerCrossBlock(ConfigBlocks.SHIMMERLEAF);

		blockModels.createChest(ConfigBlocks.HUNGRY_CHEST.block(), ConfigBlocks.GREATWOOD_PLANKS.block(), ThaumcraftData.Blocks.HUNGRY_CHEST, false);
        simpleExistingBlock(ConfigBlocks.EVERFULL_URN);

        registerNitorBlocks();

        simpleExistingBlock(ConfigBlocks.TABLE_STONE, Thaumcraft.id("block/table"), TextureSlot.TOP, TextureSlot.SIDE);
        simpleExistingBlock(ConfigBlocks.TABLE_WOOD, Thaumcraft.id("block/table"), TextureSlot.TOP, TextureSlot.SIDE);
    }

    private void registerGreatwoodTree() {
        registerLogBlock(ConfigBlocks.GREATWOOD_LOG);
        registerLogBlock(ConfigBlocks.GREATWOOD_WOOD);
        registerLogBlock(ConfigBlocks.STRIPPED_GREATWOOD_LOG);
        registerLogBlock(ConfigBlocks.STRIPPED_GREATWOOD_WOOD);

        registerLeavesBlock(ConfigBlocks.GREATWOOD_LEAVES);
		registerCrossBlock(ConfigBlocks.GREATWOOD_SAPLING);

        simpleBlock(ConfigBlocks.GREATWOOD_PLANKS);
        registerWoodStairAndSlab(ConfigBlocks.GREATWOOD_PLANKS, ConfigBlocks.GREATWOOD_STAIRS, ConfigBlocks.GREATWOOD_SLAB);
        registerFenceBlock(ConfigBlocks.GREATWOOD_FENCE, ConfigBlocks.GREATWOOD_PLANKS);
        registerFenceGateBlock(ConfigBlocks.GREATWOOD_FENCE_GATE, ConfigBlocks.GREATWOOD_PLANKS);
        registerDoorBlock(ConfigBlocks.GREATWOOD_DOOR);
        registerTrapdoorBlock(ConfigBlocks.GREATWOOD_TRAPDOOR);
        registerButtonBlock(ConfigBlocks.GREATWOOD_BUTTON, ConfigBlocks.GREATWOOD_PLANKS);
        registerPressurePlateBlock(ConfigBlocks.GREATWOOD_PRESSURE_PLATE, ConfigBlocks.GREATWOOD_PLANKS);
    }

    private void registerSilverwoodTree() {
        registerLogBlock(ConfigBlocks.SILVERWOOD_LOG);
        registerLogBlock(ConfigBlocks.SILVERWOOD_WOOD);
        registerLogBlock(ConfigBlocks.STRIPPED_SILVERWOOD_LOG);
        registerLogBlock(ConfigBlocks.STRIPPED_SILVERWOOD_WOOD);

        registerLeavesBlock(ConfigBlocks.SILVERWOOD_LEAVES);
		registerCrossBlock(ConfigBlocks.SILVERWOOD_SAPLING);

        simpleBlock(ConfigBlocks.SILVERWOOD_PLANKS);
        registerWoodStairAndSlab(ConfigBlocks.SILVERWOOD_PLANKS, ConfigBlocks.SILVERWOOD_STAIRS, ConfigBlocks.SILVERWOOD_SLAB);
        registerFenceBlock(ConfigBlocks.SILVERWOOD_FENCE, ConfigBlocks.SILVERWOOD_PLANKS);
        registerFenceGateBlock(ConfigBlocks.SILVERWOOD_FENCE_GATE, ConfigBlocks.SILVERWOOD_PLANKS);
        registerDoorBlock(ConfigBlocks.SILVERWOOD_DOOR);
        registerTrapdoorBlock(ConfigBlocks.SILVERWOOD_TRAPDOOR);
        registerButtonBlock(ConfigBlocks.SILVERWOOD_BUTTON, ConfigBlocks.SILVERWOOD_PLANKS);
        registerPressurePlateBlock(ConfigBlocks.SILVERWOOD_PRESSURE_PLATE, ConfigBlocks.SILVERWOOD_PLANKS);
    }

    private void registerLogBlock(ConfigBlocks.BlockObject<? extends RotatedPillarBlock> block) {

        Identifier id = RegistryUtils.getBlockLocation(block.blockSupplier());
        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.SIDE, new Material(id))
                .put(TextureSlot.END, new Material(id.withSuffix("_top")))
                .put(TextureSlot.PARTICLE, new Material(id));
        Identifier model = ModelTemplates.CUBE_COLUMN.create(block.block(), mapping, blocks.modelOutput);

        blocks.blockStateOutput.accept(MultiVariantGenerator.dispatch(block.block(),
                BlockModelGenerators.variant(new Variant(model))).with(
                        PropertyDispatch.modify(BlockStateProperties.AXIS)
                                .select(Direction.Axis.Y, BlockModelGenerators.NOP)
                                .select(Direction.Axis.Z, BlockModelGenerators.X_ROT_90)
                                .select(Direction.Axis.X, BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_90))));
        blockParentItem(block, model);
    }

    private void registerLeavesBlock(ConfigBlocks.BlockObject<? extends Block> block) {
        Identifier id = RegistryUtils.getBlockLocation(block.blockSupplier());
        TextureMapping mapping = new TextureMapping().put(TextureSlot.ALL, new Material(id)).put(TextureSlot.PARTICLE, new Material(id));
        ModelTemplate leavesTemplate = ExtendedModelTemplateBuilder.builder()
                .parent(Identifier.withDefaultNamespace("block/leaves"))
                .requiredTextureSlot(TextureSlot.ALL)
                .build();
        Identifier model = leavesTemplate.create(block.block(), mapping, blocks.modelOutput);
        blocks.blockStateOutput.accept(MultiVariantGenerator.dispatch(block.block(), BlockModelGenerators.variant(new Variant(model))));
        blockParentItem(block, model);
    }

    private void registerCrossBlock(ConfigBlocks.BlockObject<? extends Block> block) {
        Identifier id = RegistryUtils.getBlockLocation(block.blockSupplier());
        /*TextureMapping mapping = new TextureMapping().put(TextureSlot.CROSS, new Material(id));
        ModelTemplate crossTemplate = ExtendedModelTemplateBuilder.builder()
                .parent(Identifier.withDefaultNamespace("block/cross"))
                .requiredTextureSlot(TextureSlot.CROSS)
                .build();
        Identifier model = crossTemplate.create(block.block(), mapping, blocks.modelOutput);
        blocks.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block.block(), Variant.variant().with(VariantProperties.MODEL, model)));*/
        blocks.createCrossBlock(block.block(), BlockModelGenerators.PlantType.NOT_TINTED);
        Identifier itemTexture = id.withPath(p -> "item/" + p.substring(p.lastIndexOf('/') + 1));
        Identifier itemModel = ModelTemplates.FLAT_ITEM.create(itemTexture, TextureMapping.layer0(new Material(id)), items.modelOutput);
        items.itemModelOutput.accept(block.item(), ItemModelUtils.plainModel(itemModel));
    }

    private void registerWoodStairAndSlab(ConfigBlocks.BlockObject<? extends Block> planks, ConfigBlocks.BlockObject<? extends StairBlock> stairs, ConfigBlocks.BlockObject<? extends SlabBlock> slab) {
        Identifier texture = RegistryUtils.getBlockLocation(planks.blockSupplier());
        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.PARTICLE, new Material(texture))
                .put(TextureSlot.BOTTOM, new Material(texture))
                .put(TextureSlot.TOP, new Material(texture))
                .put(TextureSlot.SIDE, new Material(texture));
        Identifier stairsStraight = ModelTemplates.STAIRS_STRAIGHT.create(stairs.block(), mapping, blocks.modelOutput);
        Identifier innerStairs = ModelTemplates.STAIRS_INNER.create(stairs.block(), mapping, blocks.modelOutput);
        Identifier outerStairs = ModelTemplates.STAIRS_OUTER.create(stairs.block(), mapping, blocks.modelOutput);
        blocks.blockStateOutput.accept(BlockModelGenerators.createStairs(stairs.block(),
                BlockModelGenerators.variant(new Variant(innerStairs)),
                BlockModelGenerators.variant(new Variant(stairsStraight)),
                BlockModelGenerators.variant(new Variant(outerStairs))));
        Identifier bottomSlab = ModelTemplates.SLAB_BOTTOM.create(slab.block(), mapping, blocks.modelOutput);
        Identifier topSlab = ModelTemplates.SLAB_TOP.create(slab.block(), mapping, blocks.modelOutput);
        blocks.blockStateOutput.accept(BlockModelGenerators.createSlab(slab.block(),
                BlockModelGenerators.variant(new Variant(bottomSlab)),
                BlockModelGenerators.variant(new Variant(topSlab)),
                BlockModelGenerators.variant(new Variant(RegistryUtils.getBlockLocation(planks.blockSupplier())))));
        blockParentItem(stairs, stairsStraight);
        blockParentItem(slab, bottomSlab);
    }

    private void registerFenceBlock(ConfigBlocks.BlockObject<? extends Block> fence, ConfigBlocks.BlockObject<? extends Block> planks) {
        Identifier texture = RegistryUtils.getBlockLocation(planks.blockSupplier());
        TextureMapping mapping = TextureMapping.defaultTexture(new Material(texture));
        Identifier post = ModelTemplates.FENCE_POST.create(fence.block(), mapping, blocks.modelOutput);
        Identifier side = ModelTemplates.FENCE_SIDE.create(fence.block(), mapping, blocks.modelOutput);
        blocks.blockStateOutput.accept(BlockModelGenerators.createFence(fence.block(),
                BlockModelGenerators.variant(new Variant(post)),
                BlockModelGenerators.variant(new Variant(side))));
        Identifier inventory = ModelTemplates.FENCE_INVENTORY.create(fence.block(), mapping, blocks.modelOutput);
        blockParentItem(fence, inventory);
    }

    private void registerFenceGateBlock(ConfigBlocks.BlockObject<? extends Block> gate, ConfigBlocks.BlockObject<? extends Block> planks) {
        Identifier texture = RegistryUtils.getBlockLocation(planks.blockSupplier());
        TextureMapping mapping = TextureMapping.defaultTexture(new Material(texture));
        Identifier open = ModelTemplates.FENCE_GATE_OPEN.create(gate.block(), mapping, blocks.modelOutput);
        Identifier closed = ModelTemplates.FENCE_GATE_CLOSED.create(gate.block(), mapping, blocks.modelOutput);
        Identifier wallOpen = ModelTemplates.FENCE_GATE_WALL_OPEN.create(gate.block(), mapping, blocks.modelOutput);
        Identifier wallClosed = ModelTemplates.FENCE_GATE_WALL_CLOSED.create(gate.block(), mapping, blocks.modelOutput);
        blocks.blockStateOutput.accept(BlockModelGenerators.createFenceGate(gate.block(),
                BlockModelGenerators.variant(new Variant(open)),
                BlockModelGenerators.variant(new Variant(closed)),
                BlockModelGenerators.variant(new Variant(wallOpen)),
                BlockModelGenerators.variant(new Variant(wallClosed)), true));
        blockParentItem(gate, closed);
    }

    private void registerDoorBlock(ConfigBlocks.BlockObject<? extends Block> door) {
        Identifier id = RegistryUtils.getBlockLocation(door.blockSupplier());
        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.TOP, new Material(id.withSuffix("_top")))
                .put(TextureSlot.BOTTOM, new Material(id.withSuffix("_bottom")));
        Identifier bottomLeft = ModelTemplates.DOOR_BOTTOM_LEFT.create(door.block(), mapping, blocks.modelOutput);
        Identifier bottomLeftOpen = ModelTemplates.DOOR_BOTTOM_LEFT_OPEN.create(door.block(), mapping, blocks.modelOutput);
        Identifier bottomRight = ModelTemplates.DOOR_BOTTOM_RIGHT.create(door.block(), mapping, blocks.modelOutput);
        Identifier bottomRightOpen = ModelTemplates.DOOR_BOTTOM_RIGHT_OPEN.create(door.block(), mapping, blocks.modelOutput);
        Identifier topLeft = ModelTemplates.DOOR_TOP_LEFT.create(door.block(), mapping, blocks.modelOutput);
        Identifier topLeftOpen = ModelTemplates.DOOR_TOP_LEFT_OPEN.create(door.block(), mapping, blocks.modelOutput);
        Identifier topRight = ModelTemplates.DOOR_TOP_RIGHT.create(door.block(), mapping, blocks.modelOutput);
        Identifier topRightOpen = ModelTemplates.DOOR_TOP_RIGHT_OPEN.create(door.block(), mapping, blocks.modelOutput);
        blocks.blockStateOutput.accept(BlockModelGenerators.createDoor(door.block(),
                BlockModelGenerators.variant(new Variant(bottomLeft)),
                BlockModelGenerators.variant(new Variant(bottomLeftOpen)),
                BlockModelGenerators.variant(new Variant(bottomRight)),
                BlockModelGenerators.variant(new Variant(bottomRightOpen)),
                BlockModelGenerators.variant(new Variant(topLeft)),
                BlockModelGenerators.variant(new Variant(topLeftOpen)),
                BlockModelGenerators.variant(new Variant(topRight)),
                BlockModelGenerators.variant(new Variant(topRightOpen))));
        Identifier itemTexture = id.withPath(p -> "item/" + p.substring(p.lastIndexOf('/') + 1));
        Identifier itemModel = ModelTemplates.FLAT_ITEM.create(itemTexture, TextureMapping.layer0(new Material(itemTexture)), items.modelOutput);
        items.itemModelOutput.accept(door.item(), ItemModelUtils.plainModel(itemModel));
    }

    private void registerTrapdoorBlock(ConfigBlocks.BlockObject<? extends Block> trapdoor) {
        Identifier id = RegistryUtils.getBlockLocation(trapdoor.blockSupplier());
        TextureMapping mapping = TextureMapping.defaultTexture(new Material(id));
        Identifier bottom = ModelTemplates.ORIENTABLE_TRAPDOOR_BOTTOM.create(trapdoor.block(), mapping, blocks.modelOutput);
        Identifier top = ModelTemplates.ORIENTABLE_TRAPDOOR_TOP.create(trapdoor.block(), mapping, blocks.modelOutput);
        Identifier open = ModelTemplates.ORIENTABLE_TRAPDOOR_OPEN.create(trapdoor.block(), mapping, blocks.modelOutput);
        blocks.blockStateOutput.accept(BlockModelGenerators.createOrientableTrapdoor(trapdoor.block(),
                BlockModelGenerators.variant(new Variant(top)),
                BlockModelGenerators.variant(new Variant(bottom)),
                BlockModelGenerators.variant(new Variant(open))));
        blockParentItem(trapdoor, bottom);
    }

    private void registerButtonBlock(ConfigBlocks.BlockObject<? extends Block> button, ConfigBlocks.BlockObject<? extends Block> planks) {
        Identifier texture = RegistryUtils.getBlockLocation(planks.blockSupplier());
        TextureMapping mapping = TextureMapping.defaultTexture(new Material(texture));
        Identifier unpressed = ModelTemplates.BUTTON.create(button.block(), mapping, blocks.modelOutput);
        Identifier pressed = ModelTemplates.BUTTON_PRESSED.create(button.block(), mapping, blocks.modelOutput);
        blocks.blockStateOutput.accept(BlockModelGenerators.createButton(button.block(),
                BlockModelGenerators.variant(new Variant(unpressed)),
                BlockModelGenerators.variant(new Variant(pressed))));
        Identifier inventory = ModelTemplates.BUTTON_INVENTORY.create(button.block(), mapping, blocks.modelOutput);
        blockParentItem(button, inventory);
    }

    private void registerPressurePlateBlock(ConfigBlocks.BlockObject<? extends Block> plate, ConfigBlocks.BlockObject<? extends Block> planks) {
        Identifier texture = RegistryUtils.getBlockLocation(planks.blockSupplier());
        TextureMapping mapping = TextureMapping.defaultTexture(new Material(texture));
        Identifier up = ModelTemplates.PRESSURE_PLATE_UP.create(plate.block(), mapping, blocks.modelOutput);
        Identifier down = ModelTemplates.PRESSURE_PLATE_DOWN.create(plate.block(), mapping, blocks.modelOutput);
        blocks.blockStateOutput.accept(BlockModelGenerators.createPressurePlate(plate.block(),
                BlockModelGenerators.variant(new Variant(up)),
                BlockModelGenerators.variant(new Variant(down))));
        blockParentItem(plate, up);
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return super.getKnownItems().filter(holder -> holder.value() instanceof BlockItem);
    }

    private void translucentBlock(ConfigBlocks.BlockObject<? extends Block> block) {
        Identifier id = RegistryUtils.getBlockLocation(block.blockSupplier());
        TextureMapping mapping = new TextureMapping().put(TextureSlot.ALL, new Material(id, true)).put(TextureSlot.PARTICLE, new Material(id, true));
        ModelTemplate template = ExtendedModelTemplateBuilder.builder()
                .parent(Identifier.withDefaultNamespace("block/cube_all"))
                .requiredTextureSlot(TextureSlot.ALL)
                .build();
        Identifier model = template.create(block.block(), mapping, blocks.modelOutput);
        blocks.blockStateOutput.accept(MultiVariantGenerator.dispatch(block.block(), BlockModelGenerators.variant(new Variant(model))));
        blockParentItem(block, model);
    }

    private void translucentColumnBlock(ConfigBlocks.BlockObject<? extends Block> block) {
        Identifier id = RegistryUtils.getBlockLocation(block.blockSupplier());
        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.SIDE, new Material(id.withSuffix("_side"), true))
                .put(TextureSlot.END, new Material(id.withSuffix("_top"), true))
                .put(TextureSlot.PARTICLE, new Material(id.withSuffix("_side"), true));
        ModelTemplate template = ExtendedModelTemplateBuilder.builder()
                .parent(Identifier.withDefaultNamespace("block/cube_column"))
                .requiredTextureSlot(TextureSlot.SIDE)
                .requiredTextureSlot(TextureSlot.END)
                .build();
        Identifier model = template.create(block.block(), mapping, blocks.modelOutput);
        blocks.blockStateOutput.accept(MultiVariantGenerator.dispatch(block.block(), BlockModelGenerators.variant(new Variant(model))));
        blockParentItem(block, model);
    }

    public void simpleBlock(ConfigBlocks.BlockObject<? extends Block> block) {
        Identifier id = RegistryUtils.getBlockLocation(block.blockSupplier());
        TextureMapping mapping = new TextureMapping().put(TextureSlot.ALL, new Material(id)).put(TextureSlot.PARTICLE, new Material(id));
        TexturedModel.Provider model = TexturedModel.createDefault(b -> mapping, ModelTemplates.CUBE_ALL);
        blocks.createTrivialBlock(block.block(), model);
        blockParentItem(block, id);
    }

    private void registerDirectionalMultipart(ConfigBlocks.BlockObject<? extends Block> block, Map<Direction, BooleanProperty> dirProperties, Identifier centerPart, Identifier sidePart, boolean hideCenter) {
        MultiPartGenerator generator = MultiPartGenerator.multiPart(block.block());

        for(Direction dir : Direction.values()) {
            Condition dirCondition = new ConditionBuilder().term(dirProperties.get(dir), true).build();
            generator.with(dirCondition, BlockModelGenerators.variant(new Variant(sidePart)).with(BY_DIRECTION.get(dir)));
        }

        if(hideCenter) {
            var condition = Arrays.stream(Direction.values()).map(d -> new ConditionBuilder().term(dirProperties.get(d), false).build()).toList();
            generator.with(new CombinedCondition(CombinedCondition.Operation.OR, condition), BlockModelGenerators.variant(new Variant(centerPart)));
        } else
            generator.with(BlockModelGenerators.variant(new Variant(centerPart)));

        blocks.blockStateOutput.accept(generator);
        blockTextureItem(block, RegistryUtils.getBlockItemLocation(block.blockSupplier()));
    }

    private void registerJars() {
        Set.of(ConfigBlocks.WARDED_JAR, ConfigBlocks.VOID_JAR).forEach(block -> {
            blocks.blockStateOutput.accept(MultiPartGenerator.multiPart(block.block())
                    .with(BlockModelGenerators.variant(new Variant(ModelLocationUtils.getModelLocation(block.block(), "_body"))))
                            .with(new ConditionBuilder().term(JarBlock.BRACED, true).build(), BlockModelGenerators.variant(new Variant(Thaumcraft.id("block/jar_brace"))))
                            .with(new ConditionBuilder().term(JarBlock.CONNECTED, true).build(), BlockModelGenerators.variant(new Variant(Thaumcraft.id("block/jar_tube")))));
            blockParentItem(block, ModelLocationUtils.getModelLocation(block.block()).withSuffix("_body"));
        });
    }

    protected void registerCrystalColonies() {
        Identifier itemTexture = Thaumcraft.id("item/block/crystal_colony");
        Identifier itemModel = ModelTemplates.FLAT_ITEM.create(itemTexture, TextureMapping.layer0(new Material(itemTexture)), items.modelOutput);
        ConfigBlocks.CRYSTAL_COLONY.forEach((aspect, blockObject) -> {
            Block block = blockObject.block();

            blocks.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(PropertyDispatch.initial(CrystalBlock.FACING, CrystalBlock.SIZE).generate((dir, size) -> {
                Identifier model = Thaumcraft.id("block/crystal_colony_" + size);
                VariantMutator rotation = switch(dir.getAxis()) {
                    case X -> BlockModelGenerators.X_ROT_90.then(dir.getAxisDirection() == Direction.AxisDirection.POSITIVE ? BlockModelGenerators.Y_ROT_90 : BlockModelGenerators.Y_ROT_270);
                    case Y -> dir.getAxisDirection() == Direction.AxisDirection.POSITIVE ? BlockModelGenerators.X_ROT_180 : BlockModelGenerators.NOP;
                    case Z -> dir.getAxisDirection() == Direction.AxisDirection.POSITIVE ? BlockModelGenerators.X_ROT_270 : BlockModelGenerators.X_ROT_90;
                };
                return BlockModelGenerators.variant(new Variant(model)).with(rotation);
            })));
            items.itemModelOutput.accept(blockObject.item(), ItemModelUtils.tintedModel(itemModel, new AspectItemTintSource()));
        });
    }

    protected void registerAspectSource() {
        Block block = ConfigBlocks.CREATIVE_ASPECT_SOURCE.block();
        Identifier texture = TextureMapping.getBlockTexture(block).sprite();

        blocks.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(PropertyDispatch.initial(CreativeAspectSourceBlock.HAS_ASPECT).generate(b -> {
            TextureMapping mapping = new TextureMapping()
                    .put(TextureSlot.PARTICLE, new Material(texture.withSuffix(b ? "_filled" : "_empty")))
                    .put(TextureSlot.SIDE, new Material(texture.withSuffix(b ? "_filled" : "_empty")))
                    .put(TextureSlot.UP, new Material(texture.withSuffix("_empty")))
                    .put(TextureSlot.DOWN, new Material(texture.withSuffix(b ? "_filled" : "_empty")));
            return BlockModelGenerators.variant(new Variant(ModelTemplates.CUBE.createWithSuffix(block, b ? "_filled" : "_empty", mapping, blocks.modelOutput)));
        })));

        blockParentItem(ConfigBlocks.CREATIVE_ASPECT_SOURCE, ModelLocationUtils.getModelLocation(block, "_empty"));
    }

	private static final ModelTemplate DIOPTRA = ModelTemplates.create("thaumcraft:dioptra", TextureSlot.PARTICLE, TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);

    protected void registerDioptra() {
        Block block = ConfigBlocks.DIOPTRA.block();
		Identifier texture = TextureMapping.getBlockTexture(block).sprite();

        blocks.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(PropertyDispatch.initial(DioptraBlock.DISPLAY_VIS).generate(displayVis -> {
			Material sideTexture = new Material(texture.withSuffix("_side_").withSuffix(displayVis ? "vis" : "flux"));
			TextureMapping mapping = new TextureMapping()
					.put(TextureSlot.PARTICLE, sideTexture)
					.put(TextureSlot.SIDE, sideTexture)
					.put(TextureSlot.TOP, new Material(texture.withSuffix("_top")))
					.put(TextureSlot.BOTTOM, new Material(texture.withSuffix("_bottom")));
            return BlockModelGenerators.variant(new Variant(DIOPTRA.createWithSuffix(block, displayVis ? "_vis" : "_flux", mapping, blocks.modelOutput)));
        })));

        blockParentItem(ConfigBlocks.DIOPTRA, ModelLocationUtils.getModelLocation(block));
    }

    private static final ModelTemplate LEVITATOR = ModelTemplates.create("thaumcraft:levitator", TextureSlot.PARTICLE, TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);

    protected void registerLevitator() {
        Block block = ConfigBlocks.LEVITATOR.block();
        Identifier texture = TextureMapping.getBlockTexture(block).sprite();

        Map<Direction, VariantMutator> rotations = new EnumMap<>(Direction.class);
        rotations.put(Direction.UP, BlockModelGenerators.NOP);
        rotations.put(Direction.DOWN, BlockModelGenerators.X_ROT_180);
        rotations.put(Direction.NORTH, BlockModelGenerators.X_ROT_90);
        rotations.put(Direction.SOUTH, BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_180));
        rotations.put(Direction.EAST, BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_90));
        rotations.put(Direction.WEST, BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_270));

        Identifier sideTexture = texture.withSuffix("_side_");
        Identifier topTexture = texture.withSuffix("_top_");
        Variant off = new Variant(LEVITATOR.createWithSuffix(block, "_off", new TextureMapping()
                .put(TextureSlot.PARTICLE, new Material(sideTexture.withSuffix("off")))
                .put(TextureSlot.SIDE, new Material(sideTexture.withSuffix("off")))
                .put(TextureSlot.TOP, new Material(topTexture.withSuffix("off")))
                .put(TextureSlot.BOTTOM, new Material(texture.withSuffix("_bottom"))), blocks.modelOutput));
        Variant on = new Variant(LEVITATOR.createWithSuffix(block, "_on", new TextureMapping()
                .put(TextureSlot.PARTICLE, new Material(sideTexture.withSuffix("on")))
                .put(TextureSlot.SIDE, new Material(sideTexture.withSuffix("on")))
                .put(TextureSlot.TOP, new Material(topTexture.withSuffix("on")))
                .put(TextureSlot.BOTTOM, new Material(texture.withSuffix("_bottom"))), blocks.modelOutput));

        blocks.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(PropertyDispatch.initial(LevitatorBlock.FACING, LevitatorBlock.ENABLED).generate((dir, enabled) -> BlockModelGenerators.variant(enabled ? on : off).with(rotations.get(dir)))));
        blockParentItem(ConfigBlocks.LEVITATOR, ModelLocationUtils.getModelLocation(block, "_on"));
    }

    private void simpleExistingBlock(ConfigBlocks.BlockObject<? extends Block> block) {
        Identifier model = ModelLocationUtils.getModelLocation(block.block());
        blocks.blockStateOutput.accept(MultiVariantGenerator.dispatch(block.block(), BlockModelGenerators.variant(new Variant(model))));
        blockParentItem(block, model);
    }

	private void simpleExistingModel(ConfigBlocks.BlockObject<? extends Block> block, Identifier modelId, String... subfolders) {
		Identifier model = RegistryUtils.getBlockLocation(modelId, subfolders);
        blocks.blockStateOutput.accept(MultiVariantGenerator.dispatch(block.block(), BlockModelGenerators.variant(new Variant(model))));
		blockParentItem(block, model);
	}

	private void simpleExistingBlock(ConfigBlocks.BlockObject<? extends Block> block, Identifier model, TextureSlot... slots) {
		Identifier texture = ModelLocationUtils.getModelLocation(block.block());
		TextureMapping mapping = new TextureMapping();
		ExtendedModelTemplateBuilder builder = ExtendedModelTemplateBuilder.builder().parent(model);
		Arrays.stream(slots).forEach(slot -> {
			builder.requiredTextureSlot(slot);
			mapping.put(slot, new Material(texture.withSuffix("_" + slot.getId())));
		});
		builder.requiredTextureSlot(TextureSlot.PARTICLE);
		mapping.put(TextureSlot.PARTICLE, new Material(texture.withSuffix(slots.length > 0 ? "_" + slots[0].getId() : "")));
		Identifier newModel = builder.build().create(block.block(), mapping, blocks.modelOutput);
        blocks.blockStateOutput.accept(MultiVariantGenerator.dispatch(block.block(), BlockModelGenerators.variant(new Variant(newModel))));
		blockParentItem(block, newModel);
	}

    private void registerStairAndSlab(ConfigBlocks.BlockObject<? extends Block> block, ConfigBlocks.BlockObject<? extends StairBlock> stairs, ConfigBlocks.BlockObject<? extends SlabBlock> slab, boolean uniqueTextures) {
        Identifier texture = RegistryUtils.getBlockLocation(block.blockSupplier());
        TextureMapping mapping = new TextureMapping().put(TextureSlot.PARTICLE, new Material(texture.withSuffix(uniqueTextures ? "_0" : "")))
                .put(TextureSlot.BOTTOM, new Material(texture.withSuffix(uniqueTextures ? "_0" : "")))
                .put(TextureSlot.TOP, new Material(texture.withSuffix(uniqueTextures ? "_1" : "")))
                .put(TextureSlot.SIDE, new Material(texture.withSuffix(uniqueTextures ? "_2" : "")));
        Identifier stairsStraight = ModelTemplates.STAIRS_STRAIGHT.create(stairs.block(), mapping, blocks.modelOutput);
        Identifier innerStairs = ModelTemplates.STAIRS_INNER.create(stairs.block(), mapping, blocks.modelOutput);
        Identifier outerStairs = ModelTemplates.STAIRS_OUTER.create(stairs.block(), mapping, blocks.modelOutput);
        blocks.blockStateOutput.accept(BlockModelGenerators.createStairs(stairs.block(),
                BlockModelGenerators.variant(new Variant(innerStairs)),
                BlockModelGenerators.variant(new Variant(stairsStraight)),
                BlockModelGenerators.variant(new Variant(outerStairs))));
        Identifier bottomSlab = ModelTemplates.SLAB_BOTTOM.create(slab.block(), mapping, blocks.modelOutput);
        Identifier topSlab = ModelTemplates.SLAB_TOP.create(slab.block(), mapping, blocks.modelOutput);
        blocks.blockStateOutput.accept(BlockModelGenerators.createSlab(slab.block(),
                BlockModelGenerators.variant(new Variant(bottomSlab)),
                BlockModelGenerators.variant(new Variant(topSlab)),
                BlockModelGenerators.variant(new Variant(RegistryUtils.getBlockLocation(block.blockSupplier())))));
        blockParentItem(stairs, stairsStraight);
        blockParentItem(slab, bottomSlab);
    }

    private void registerAxisTexturedBlock(ConfigBlocks.BlockObject<? extends Block> block, boolean allSides) {
        Identifier id = RegistryUtils.getBlockLocation(block.blockSupplier());
        TextureMapping mapping = new TextureMapping().put(TextureSlot.PARTICLE, new Material(id.withSuffix("_0")))
                .put(TextureSlot.UP, new Material(id.withSuffix("_0")))
                .put(TextureSlot.EAST, new Material(id.withSuffix("_1")))
                .put(TextureSlot.NORTH, new Material(id.withSuffix("_2")))
                .put(TextureSlot.DOWN, new Material(id.withSuffix(allSides ? "_3" : "_0")))
                .put(TextureSlot.WEST, new Material(id.withSuffix(allSides ? "_4" : "_1")))
                .put(TextureSlot.SOUTH, new Material(id.withSuffix(allSides ? "_5" : "_2")));

        Identifier model = ModelTemplates.CUBE_DIRECTIONAL.create(block.block(), mapping, blocks.modelOutput);
        blocks.blockStateOutput.accept(MultiVariantGenerator.dispatch(block.block(), new MultiVariant(WeightedList.of(
                new Weighted<>(new Variant(model), 5),
                new Weighted<>(new Variant(model).with(BlockModelGenerators.X_ROT_90), 5),
                new Weighted<>(new Variant(model).with(BlockModelGenerators.Y_ROT_90), 5),
                new Weighted<>(new Variant(model).with(BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_90)), 5)))));
        blockParentItem(block, id);
    }

    private void registerFakeBlock(DeferredBlock<?> block) {
        Identifier model = Thaumcraft.id("block/empty");
        blocks.blockStateOutput.accept(MultiVariantGenerator.dispatch(block.get(), BlockModelGenerators.variant(new Variant(model))));
    }

    private void registerAllFacingBlock(ConfigBlocks.BlockObject<? extends Block> block, net.minecraft.world.level.block.state.properties.Property<Direction> facingProperty) {
        Identifier model = ModelLocationUtils.getModelLocation(block.block());
        MultiVariantGenerator generator = MultiVariantGenerator.dispatch(block.block(), BlockModelGenerators.variant(new Variant(model)))
                .with(PropertyDispatch.modify(facingProperty).generate(facing -> switch (facing) {
					case UP -> BlockModelGenerators.NOP;
					case DOWN -> BlockModelGenerators.X_ROT_180;
					case NORTH -> BlockModelGenerators.X_ROT_90;
					case SOUTH -> BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.X_ROT_180);
					case EAST -> BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.X_ROT_90);
					case WEST -> BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.X_ROT_270);
				}));
        blocks.blockStateOutput.accept(generator);
        blockParentItem(block, model);
    }

    private void registerNitorBlocks() {
		registerEmptyBlock(ConfigBlocks.NITOR, RegistryUtils.getItemLocation(ConfigBlocks.NITOR.blockSupplier(), "block"), null);

		Identifier location = RegistryUtils.getItemLocation(ConfigBlocks.NITOR.itemSupplier(), "block");
		Identifier model = ModelTemplates.TWO_LAYERED_ITEM.create(location,
				TextureMapping.layered(new Material(location), new Material(location.withSuffix("_overlay"))),
				items.modelOutput);
		items.itemModelOutput.accept(ConfigBlocks.NITOR.item(), ItemModelUtils.tintedModel(model, new Constant(0xFFFFFFFF), new DyeItemTintSource(DyeColor.YELLOW)));
    }

	private static final ExtendedModelTemplate TEMPLATE_EMPTY = ExtendedModelTemplateBuilder.builder().requiredTextureSlot(TextureSlot.PARTICLE).build();

    private void registerEmptyBlock(ConfigBlocks.BlockObject<? extends Block> block, Identifier particleTexture, Identifier itemModel) {
		Identifier model = TexturedModel.createDefault(b -> new TextureMapping().put(TextureSlot.PARTICLE, new Material(particleTexture)), TEMPLATE_EMPTY).create(block.block(), blocks.modelOutput);
        blocks.blockStateOutput.accept(MultiVariantGenerator.dispatch(block.block(), BlockModelGenerators.variant(new Variant(model))));
		if(itemModel != null)
            blockParentItem(block, itemModel);
    }

    private static final Identifier PILLAR_MODEL = Thaumcraft.id("models/block/infusion_pillar.obj");

    private void registerInfusionPillar(ConfigBlocks.BlockObject<InfusionPillarBlock> block, Identifier particle) {
        Identifier model = TexturedModel.createDefault(
                b -> new TextureMapping()
                        .put(TextureSlot.ALL, TextureMapping.getBlockTexture(b))
                        .put(TextureSlot.PARTICLE, new Material(particle)),
                ExtendedModelTemplateBuilder.builder()
                    .customLoader(ObjModelBuilder::new, loader -> {
                        loader.modelLocation(PILLAR_MODEL);
                    }).requiredTextureSlot(TextureSlot.TEXTURE).requiredTextureSlot(TextureSlot.PARTICLE).build()).create(block.block(), blocks.modelOutput);

        blocks.blockStateOutput.accept(MultiVariantGenerator.dispatch(block.block(), BlockModelGenerators.variant(new Variant(model)))
                .with(PropertyDispatch.modify(InfusionPillarBlock.POINTING).generate(InfusionPillarBlock.PillarDirection::getMutator)));
    }

    private void blockParentItem(ConfigBlocks.BlockObject<? extends Block> block, Identifier blockModel) {
        items.itemModelOutput.accept(block.item(), ItemModelUtils.plainModel(blockModel));
    }

    private void blockTextureItem(ConfigBlocks.BlockObject<? extends Block> block, Identifier itemModel) {
        Identifier model = ModelTemplates.FLAT_ITEM.create(itemModel, TextureMapping.layer0(new Material(itemModel)), items.modelOutput);
        items.itemModelOutput.accept(block.item(), ItemModelUtils.plainModel(model));
    }

    private void batchSimpleExistingBlock(ConfigBlocks.BlockObject<? extends Block>... blocks) {
        for (ConfigBlocks.BlockObject<? extends Block> block : blocks)
            simpleExistingBlock(block);
    }

    public static final Map<Direction, VariantMutator> BY_DIRECTION = new EnumMap<>(ImmutableMap.of(
            Direction.NORTH, BlockModelGenerators.X_ROT_270,
            Direction.EAST, BlockModelGenerators.X_ROT_270.then(BlockModelGenerators.Y_ROT_90),
            Direction.SOUTH, BlockModelGenerators.X_ROT_90,
            Direction.WEST, BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_90),
            Direction.UP, BlockModelGenerators.X_ROT_180,
            Direction.DOWN, BlockModelGenerators.NOP));

    @Override
    public String getName() {
        return "Block Data";
    }
}
