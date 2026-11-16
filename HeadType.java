package pl.klocc.kloccosmetics;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public enum HeadType {
    NONE("Brak", null),
    CAT("Kocia głowa", "cat");

    private final String label;
    public final Identifier texture;

    HeadType(String label, String file) {
        this.label = label;
        this.texture = file == null ? null
                : Identifier.of("kloccosmetics", "textures/hats/" + file + ".png");
    }

    public Text text() {
        return Text.literal(label);
    }
}
