package pl.klocc.kloccosmetics;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public enum Cape {
    NONE("Brak", null),
    SUPREME_BLACK("Czarna - Supreme", "supreme_black"),
    SUPREME_WHITE("Biała - Supreme", "supreme_white"),
    BULBELY_BLACK("Czarna - bułbeły", "bulbely_black"),
    BULBELY_WHITE("Biała - bułbeły", "bulbely_white"),
    BULBELY_BLUE("Niebieska - bułbeły", "bulbely_blue"),
    PURPLE("Fioletowa (bez napisu)", "purple"),
    BOSS_PURPLE("Fioletowa - BOSS", "boss_purple"),
    BOSS_BLACK("Czarna - BOSS", "boss_black");

    private final String label;
    public final Identifier texture;

    Cape(String label, String file) {
        this.label = label;
        this.texture = file == null ? null
                : Identifier.of("kloccosmetics", "textures/cape/" + file + ".png");
    }

    public Text text() {
        return Text.literal(label);
    }
}
