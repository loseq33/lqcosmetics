package pl.klocc.kloccosmetics;

/** Dodatkowe dane doklejane (mixinem) do PlayerEntityRenderState. */
public interface KloccStateAccess {
    WingType klocc$getWings();

    HeadType klocc$getHead();

    boolean klocc$isSneaking();

    float klocc$getTime();

    void klocc$set(WingType wings, HeadType head, boolean sneaking, float time);
}
