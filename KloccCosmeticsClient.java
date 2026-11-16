package pl.klocc.kloccosmetics;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EntityType;
import org.lwjgl.glfw.GLFW;

public class KloccCosmeticsClient implements ClientModInitializer {
    public static final String MOD_ID = "kloccosmetics";

    @Override
    public void onInitializeClient() {
        KloccConfig.load();

        EntityModelLayerRegistry.registerModelLayer(KloccWingsModel.LAYER, KloccWingsModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(KloccCatModel.LAYER, KloccCatModel::getTexturedModelData);

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(
                (entityType, entityRenderer, registrationHelper, context) -> {
                    if (entityType == EntityType.PLAYER && entityRenderer instanceof PlayerEntityRenderer renderer) {
                        registrationHelper.register(new WingsFeatureRenderer(renderer, context.getEntityModels()));
                    }
                });

        KeyBinding openKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.kloccosmetics.open", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_K,
                "category.kloccosmetics.main"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new KloccConfigScreen(null));
                }
            }
        });
    }
}
