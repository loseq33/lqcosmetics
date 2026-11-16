package pl.klocc.kloccosmetics;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

/** Kocia glowa: kostka 8x8x8 (powiekszona, zeby przykryc glowe gracza) + dwa uszy. */
public class KloccCatModel {
    public static final EntityModelLayer LAYER =
            new EntityModelLayer(Identifier.of("kloccosmetics", "cat"), "main");

    private final ModelPart root;

    public KloccCatModel(ModelPart root) {
        this.root = root.getChild("cat");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData rootData = data.getRoot();
        ModelPartData cat = rootData.addChild("cat",
                ModelPartBuilder.create().uv(0, 0).cuboid(-4f, -8f, -4f, 8f, 8f, 8f, new Dilation(0.7f)),
                ModelTransform.NONE);
        cat.addChild("left_ear",
                ModelPartBuilder.create().uv(32, 0).cuboid(1.5f, -12f, -1.5f, 2f, 3f, 1f),
                ModelTransform.NONE);
        cat.addChild("right_ear",
                ModelPartBuilder.create().uv(32, 0).mirrored().cuboid(-3.5f, -12f, -1.5f, 2f, 3f, 1f),
                ModelTransform.NONE);
        return TexturedModelData.of(data, 64, 32);
    }

    public ModelPart root() {
        return root;
    }
}
