package pl.klocc.kloccosmetics;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;

public class WingsFeatureRenderer extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {
    private final KloccWingsModel model;

    public WingsFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context,
                                EntityModelLoader loader) {
        super(context);
        this.model = new KloccWingsModel(loader.getModelPart(KloccWingsModel.LAYER));
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                       PlayerEntityRenderState state, float limbAngle, float limbDistance) {
        KloccStateAccess access = (KloccStateAccess) state;
        WingType type = access.klocc$getWings();
        if (type == WingType.NONE || type.texture == null) return;

        model.setup(access.klocc$isSneaking(), access.klocc$getTime());

        matrices.push();
        // skalowanie wzgledem punktu zaczepienia na plecach
        float px = 0f, py = KloccWingsModel.PY / 16f, pz = KloccWingsModel.PZ / 16f;
        matrices.translate(px, py, pz);
        matrices.scale(type.scale * 0.65f, type.scale, type.scale);
        matrices.translate(-px, -py, -pz);

        VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(type.texture));
        int l = type.glow ? LightmapTextureManager.MAX_LIGHT_COORDINATE : light;
        model.root().render(matrices, vc, l, OverlayTexture.DEFAULT_UV);
        matrices.pop();
    }
}
