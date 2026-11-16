package pl.klocc.kloccosmetics;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public enum WingType {
    NONE("Brak", null, 1.0f, false),
    DRAGON_BLACK("Czarne - małe (smok Endu)", "dragon_black", 1.0f, false),
    DRAGON_WHITE("Białe - małe (smok Endu)", "dragon_white", 1.0f, false),
    BAT_SMALL("Czarne - nietoperz (mini)", "dragon_black", 0.6f, false),
    ANGEL("Anielskie", "angel", 1.4f, false),
    GREEN("Zielone - smocze", "green", 1.4f, false),
    FIRE_SMALL("Ogniste - małe", "fire", 1.0f, true),
    FIRE_BIG("Ogniste - duże", "fire", 1.9f, true);

    private final String label;
    public final Identifier texture;
    public final float scale;
    /** swieci w ciemnosci */
    public final boolean glow;

    WingType(String label, String file, float scale, boolean glow) {
        this.label = label;
        this.texture = file == null ? null
                : Identifier.of("kloccosmetics", "textures/wings/" + file + ".png");
        this.scale = scale;
        this.glow = glow;
    }

    public Text text() {
        return Text.literal(label);
    }
}
