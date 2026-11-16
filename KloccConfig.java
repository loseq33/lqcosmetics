package pl.klocc.kloccosmetics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/** Ustawienia zapisywane w config/kloccosmetics.json. Klucz "@self" = Ty. */
public final class KloccConfig {
    public static final String SELF = "@self";

    public static final class Setup {
        public Cape cape = Cape.NONE;
        public WingType wings = WingType.NONE;
        public HeadType head = HeadType.NONE;
    }

    private static final class Data {
        Map<String, Setup> players = new LinkedHashMap<>();
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Data data = new Data();

    private KloccConfig() {}

    private static Path file() {
        return FabricLoader.getInstance().getConfigDir().resolve("kloccosmetics.json");
    }

    public static void load() {
        Path f = file();
        if (!Files.exists(f)) return;
        try (Reader r = Files.newBufferedReader(f)) {
            Data d = GSON.fromJson(r, Data.class);
            if (d != null && d.players != null) data = d;
        } catch (Exception e) {
            System.err.println("[KloccCosmetics] Nie udalo sie wczytac configu: " + e);
        }
        data.players.values().removeIf(s -> s == null);
        for (Setup s : data.players.values()) {
            if (s.cape == null) s.cape = Cape.NONE;
            if (s.wings == null) s.wings = WingType.NONE;
            if (s.head == null) s.head = HeadType.NONE;
        }
    }

    public static void save() {
        try (Writer w = Files.newBufferedWriter(file())) {
            GSON.toJson(data, w);
        } catch (IOException e) {
            System.err.println("[KloccCosmetics] Nie udalo sie zapisac configu: " + e);
        }
    }

    /** Pusty tekst, "ja", "me" albo wlasny nick = Ty. */
    public static String normalize(String input) {
        String s = input == null ? "" : input.trim();
        if (s.isEmpty()) return SELF;
        String l = s.toLowerCase(Locale.ROOT);
        if (l.equals("ja") || l.equals("me") || l.equals("self")) return SELF;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.getSession() != null && l.equals(mc.getSession().getUsername().toLowerCase(Locale.ROOT))) {
            return SELF;
        }
        return l;
    }

    public static Setup get(String key) {
        return data.players.get(key);
    }

    public static Map<String, Setup> all() {
        return data.players;
    }

    public static void set(String key, Cape cape, WingType wings, HeadType head) {
        if (cape == Cape.NONE && wings == WingType.NONE && head == HeadType.NONE) {
            data.players.remove(key);
        } else {
            Setup s = new Setup();
            s.cape = cape;
            s.wings = wings;
            s.head = head;
            data.players.put(key, s);
        }
        save();
    }

    public static void remove(String key) {
        data.players.remove(key);
        save();
    }

    public static Setup lookup(AbstractClientPlayerEntity player) {
        if (data.players.isEmpty()) return null;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (player == mc.player) {
            Setup s = data.players.get(SELF);
            if (s != null) return s;
        }
        return data.players.get(player.getGameProfile().getName().toLowerCase(Locale.ROOT));
    }
}
