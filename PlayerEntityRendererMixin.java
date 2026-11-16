package pl.klocc.kloccosmetics.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.klocc.kloccosmetics.Cape;
import pl.klocc.kloccosmetics.HeadType;
import pl.klocc.kloccosmetics.KloccConfig;
import pl.klocc.kloccosmetics.KloccStateAccess;
import pl.klocc.kloccosmetics.WingType;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {

    @Inject(
        method = "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V",
        at = @At("TAIL")
    )
    private void klocc$applyCosmetics(AbstractClientPlayerEntity player, PlayerEntityRenderState state,
                                      float tickDelta, CallbackInfo ci) {
        KloccConfig.Setup setup = KloccConfig.lookup(player);
        WingType wings = WingType.NONE;
        HeadType head = HeadType.NONE;

        if (setup != null) {
            // Peleryna - podmieniamy teksture peleryny tylko w stanie renderowania (lokalnie).
            if (setup.cape != null && setup.cape != Cape.NONE && !player.isInvisible()) {
                SkinTextures old = state.skinTextures;
                state.skinTextures = new SkinTextures(old.texture(), old.textureUrl(),
                        setup.cape.texture, old.elytraTexture(), old.model(), old.secure());
                state.capeVisible = true;
            }
            // Skrzydla - chowamy gdy gracz niewidzialny lub ma elytry.
            if (setup.wings != null && !player.isInvisible()
                    && !player.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA)) {
                wings = setup.wings;
            }
            if (setup.head != null && !player.isInvisible()) {
                head = setup.head;
            }
        }

        ((KloccStateAccess) state).klocc$set(wings, head, player.isSneaking(), player.age + tickDelta);
    }
}
