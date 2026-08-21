package art.arcane.thaumcraft.client.rendering.models.blocks;

import art.arcane.thaumcraft.client.rendering.ber.RunicMatrixBER;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.Direction;

import java.util.Map;

public class RunicMatrixModel extends Model<RunicMatrixBER.RenderState> {

    private final ModelPart rootw;
    private final ModelPart lnw, lne, lsw, lse;
    private final ModelPart unw, une, usw, use;

    private final Map<Direction, ModelPart[]> rotations;

    public RunicMatrixModel(ModelPart root) {
        super(root, RenderTypes::entityCutout);
        this.rootw = root.getChild("root");
        this.lnw = rootw.getChild("lnw");
        this.lne = rootw.getChild("lne");
        this.lsw = rootw.getChild("lsw");
        this.lse = rootw.getChild("lse");
        this.unw = rootw.getChild("unw");
        this.une = rootw.getChild("une");
        this.usw = rootw.getChild("usw");
        this.use = rootw.getChild("use");

        rotations = ImmutableMap.<Direction, ModelPart[]>builder()
                .put(Direction.WEST, new ModelPart[] { lnw, lsw, unw, usw })
                .put(Direction.EAST, new ModelPart[] { lne, lse, une, use })
                .put(Direction.DOWN, new ModelPart[] { lnw, lne, lsw, lse })
                .put(Direction.UP, new ModelPart[] { unw, une, usw, use })
                .put(Direction.NORTH, new ModelPart[] { lnw, lne, unw, une })
                .put(Direction.SOUTH, new ModelPart[] { lsw, lse, usw, use })
                .build();
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));

        createElement(root, "lnw", 4.0F, 4.0F, -4.0F, 90, 0, 0);
        createElement(root, "lne", -4.0F, 4.0F, -4.0F, 90, 90, 0);
        createElement(root, "lsw", 4.0F, 4.0F, 4.0F, 90, 90, 90);
        createElement(root, "lse", -4.0F, 4.0F, 4.0F, 0, 90, 90);
        createElement(root, "unw", 4.0F, -4.0F, -4.0F, 0, 0, 90);
        createElement(root, "une", -4.0F, -4.0F, -4.0F, 0, 90, 0);
        createElement(root, "usw", 4.0F, -4.0F, 4.0F, 90, 0, 90);
        createElement(root, "use", -4.0F, -4.0F, 4.0F, 0, 0, 0);

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(RunicMatrixBER.RenderState state) { }

    private static void createElement(PartDefinition root, String name, float x, float y, float z, float pitch, float yaw, float roll) {
        root.addOrReplaceChild(name,
                CubeListBuilder.create().addBox(-3.5F, -3.5F, -3.5F, 7.0F, 7.0F, 7.0F, CubeDeformation.NONE, 0.4375F, 0.4375F),
                PartPose.offsetAndRotation(x, y, z, (float)Math.toRadians(pitch), (float)Math.toRadians(yaw), (float)Math.toRadians(roll)));
    }
}
