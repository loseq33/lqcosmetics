package pl.klocc.kloccosmetics;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

/** Dwa plaskie skrzydla (tekstura 32x20: lewa polowa = przod, prawa = tyl). */
public class KloccWingsModel {
    public static final EntityModelLayer LAYER =
            new EntityModelLayer(Identifier.of("kloccosmetics", "wings"), "main");

    public static final float W = 16f, H = 20f;
    /** punkt zaczepienia na plecach (w pikselach modelu) */
    public static final float PX = 1.5f, PY = -3f, PZ = 2.6f;

    private final ModelPart group;
    private final ModelPart left;
    private final ModelPart right;

    public KloccWingsModel(ModelPart root) {
        this.group = root.getChild("wings");
        this.left = group.getChild("left_wing");
        this.right = group.getChild("right_wing");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();
        ModelPartData wings = root.addChild("wings", ModelPartBuilder.create(), ModelTransform.NONE);
        // w przestrzeni modelu lewa strona gracza to +x, prawa to -x
        wings.addChild("left_wing",
                ModelPartBuilder.create().uv(0, 0).cuboid(0f, 0f, 0f, W, H, 0f),
                ModelTransform.pivot(PX, PY, PZ));
        wings.addChild("right_wing",
                ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-W, 0f, 0f, W, H, 0f),
                ModelTransform.pivot(-PX, PY, PZ));
        return TexturedModelData.of(data, 32, 20);
    }

    public void setup(boolean sneaking, float time) {
        group.pivotY = sneaking ? 3.2f : 0f;
        group.pitch = sneaking ? 0.5f : 0f;

        float flap = MathHelper.sin(time * 0.12f) * 0.07f;
        float yaw = 0.45f + flap;
        float roll = 0.25f + flap * 0.5f;
        left.yaw = -yaw;
        left.roll = -roll;
        right.yaw = yaw;
        right.roll = roll;
    }

    public ModelPart root() {
        return group;
    }
}
