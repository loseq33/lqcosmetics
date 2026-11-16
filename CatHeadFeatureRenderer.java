package pl.klocc.kloccosmetics;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;

public class CatHeadFeatureRenderer extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {
    private final KloccCatModel model;

    public CatHeadFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context,
                                  ModelPart catRoot) {
        super(context);
        this.model = new KloccCatModel(catRoot);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                       PlayerEntityRenderState state, float limbAngle, float limbDistance) {
        HeadType type = ((KloccStateAccess) state).klocc$getHead();
        if (type == HeadType.NONE || type.texture == null) return;

        matrices.push();
        // dopasowanie do pozycji i obrotu glowy gracza
        this.getContextModel().head.rotate(matrices);
        VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(type.texture));
        model.root().render(matrices, vc, light, OverlayTexture.DEFAULT_UV);
        matrices.pop();
    }
}
