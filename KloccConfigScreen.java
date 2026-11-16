package pl.klocc.kloccosmetics;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.stream.Collectors;

public class KloccConfigScreen extends Screen {
    private final Screen parent;
    private TextFieldWidget targetField;
    private CyclingButtonWidget<Cape> capeBtn;
    private CyclingButtonWidget<WingType> wingsBtn;
    private CyclingButtonWidget<HeadType> headBtn;

    public KloccConfigScreen(Screen parent) {
        super(Text.literal("KloccCosmetics"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;

        targetField = new TextFieldWidget(this.textRenderer, cx - 100, 62, 200, 20, Text.literal("Nick"));
        targetField.setMaxLength(32);
        targetField.setPlaceholder(Text.literal("Nick gracza (puste = Ty)"));
        addDrawableChild(targetField);

        capeBtn = CyclingButtonWidget.builder(Cape::text)
                .values(Cape.values())
                .initially(Cape.NONE)
                .build(cx - 100, 96, 200, 20, Text.literal("Peleryna"), (b, v) -> apply());
        addDrawableChild(capeBtn);

        wingsBtn = CyclingButtonWidget.builder(WingType::text)
                .values(WingType.values())
                .initially(WingType.NONE)
                .build(cx - 100, 122, 200, 20, Text.literal("Skrzydła"), (b, v) -> apply());
        addDrawableChild(wingsBtn);

        headBtn = CyclingButtonWidget.builder(HeadType::text)
                .values(HeadType.values())
                .initially(HeadType.NONE)
                .build(cx - 100, 148, 200, 20, Text.literal("Głowa"), (b, v) -> apply());
        addDrawableChild(headBtn);

        addDrawableChild(ButtonWidget.builder(Text.literal("Usuń dla tego gracza"), b -> {
            KloccConfig.remove(KloccConfig.normalize(targetField.getText()));
            reload();
        }).dimensions(cx - 100, 178, 200, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Gotowe"), b -> close())
                .dimensions(cx - 100, this.height - 32, 200, 20).build());

        targetField.setChangedListener(s -> reload());
        reload();
        setInitialFocus(targetField);
    }

    /** Wczytuje zapisane ustawienia dla gracza wpisanego w polu. */
    private void reload() {
        if (capeBtn == null || wingsBtn == null || headBtn == null) return;
        KloccConfig.Setup s = KloccConfig.get(KloccConfig.normalize(targetField.getText()));
        capeBtn.setValue(s == null ? Cape.NONE : s.cape);
        wingsBtn.setValue(s == null ? WingType.NONE : s.wings);
        headBtn.setValue(s == null ? HeadType.NONE : s.head);
    }

    private void apply() {
        KloccConfig.set(KloccConfig.normalize(targetField.getText()), capeBtn.getValue(), wingsBtn.getValue(), headBtn.getValue());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int cx = this.width / 2;
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, cx, 15, 0xFFFFFF);
        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal("Widoczne tylko u Ciebie (po stronie klienta)"), cx, 30, 0xAAAAAA);
        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal("Dla kogo (puste = Ty):"), cx, 50, 0xFFFFFF);

        if (!KloccConfig.all().isEmpty()) {
            String names = KloccConfig.all().keySet().stream()
                    .map(k -> k.equals(KloccConfig.SELF) ? "Ty" : k)
                    .collect(Collectors.joining(", "));
            List<OrderedText> lines = this.textRenderer.wrapLines(Text.literal("Zapisani: " + names), 260);
            int y = 210;
            for (OrderedText line : lines) {
                context.drawTextWithShadow(this.textRenderer, line, cx - 130, y, 0xCCCCCC);
                y += 11;
            }
        }
    }

    @Override
    public void close() {
        if (this.client != null) this.client.setScreen(parent);
    }
}
