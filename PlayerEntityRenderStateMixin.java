package pl.klocc.kloccosmetics.mixin;

import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import pl.klocc.kloccosmetics.HeadType;
import pl.klocc.kloccosmetics.KloccStateAccess;
import pl.klocc.kloccosmetics.WingType;

@Mixin(PlayerEntityRenderState.class)
public class PlayerEntityRenderStateMixin implements KloccStateAccess {
    @Unique private WingType klocc$wings = WingType.NONE;
    @Unique private HeadType klocc$head = HeadType.NONE;
    @Unique private boolean klocc$sneaking;
    @Unique private float klocc$time;

    @Override public WingType klocc$getWings() { return klocc$wings; }
    @Override public HeadType klocc$getHead() { return klocc$head; }
    @Override public boolean klocc$isSneaking() { return klocc$sneaking; }
    @Override public float klocc$getTime() { return klocc$time; }

    @Override
    public void klocc$set(WingType wings, HeadType head, boolean sneaking, float time) {
        this.klocc$wings = wings;
        this.klocc$head = head;
        this.klocc$sneaking = sneaking;
        this.klocc$time = time;
    }
}
