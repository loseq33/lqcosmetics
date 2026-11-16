"""Generuje tekstury moda kloccosmetics (peleryny + skrzydla).
Uruchom:  python tools/gen_textures.py   (wymaga Pillow)
"""
from PIL import Image, ImageDraw
import random, os

ROOT = os.path.join(os.path.dirname(__file__), "..", "src", "main", "resources",
                    "assets", "kloccosmetics", "textures")
S = 8  # skala tekstury peleryny (64x32 -> 512x256)

# ---------- prosty czcionka 5x7 w stylu Minecrafta ----------
G = {
 'S': [".###.", "#...#", "#....", ".###.", "....#", "#...#", ".###."],
 'B': ["####.", "#...#", "#...#", "####.", "#...#", "#...#", "####."],
 'O': [".###.", "#...#", "#...#", "#...#", "#...#", "#...#", ".###."],
 'u': [".....", ".....", "#...#", "#...#", "#...#", "#..##", ".##.#"],
 'p': [".....", ".....", "####.", "#...#", "#...#", "#...#", "####.", "#....", "#...."],
 'r': [".....", ".....", "#.##.", "##...", "#....", "#....", "#...."],
 'e': [".....", ".....", ".###.", "#...#", "#####", "#....", ".###."],
 'm': [".....", ".....", "##.#.", "#.#.#", "#.#.#", "#...#", "#...#"],
 'b': ["#....", "#....", "####.", "#...#", "#...#", "#...#", "####."],
 'l': ["..#..", "..#..", "..##.", "..#..", ".##..", "..#..", "..#.."],  # ł
 'y': [".....", ".....", "#...#", "#...#", "#...#", ".####", "....#", "#...#", ".###."],
}
G['\u0142'] = G['l']  # ł


def text_img(text, color, scale):
    w = len(text) * 6 - 1
    img = Image.new("RGBA", (w, 9), (0, 0, 0, 0))
    px = img.load()
    x = 0
    for ch in text:
        for r, row in enumerate(G[ch]):
            for c, v in enumerate(row):
                if v == '#':
                    px[x + c, r] = color
        x += 6
    return img.resize((w * scale, 9 * scale), Image.NEAREST)


def shade(c, f):
    return tuple(max(0, min(255, int(v * f))) for v in c[:3]) + (255,)


def make_cape(name, base, edge, deco):
    img = Image.new("RGBA", (64 * S, 32 * S), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    for (x, y, w, h) in [(0, 1, 1, 16), (1, 0, 10, 1), (11, 0, 10, 1),
                         (1, 1, 10, 16), (11, 1, 1, 16)]:
        d.rectangle([x * S, y * S, (x + w) * S - 1, (y + h) * S - 1], fill=base)
    # wnetrze
    d.rectangle([12 * S, 1 * S, 22 * S - 1, 17 * S - 1], fill=shade(base, 0.75))
    # obramowanie zewnetrznej strony
    d.rectangle([1 * S, 1 * S, 11 * S - 1, 17 * S - 1], outline=edge, width=2)
    ox, oy, ow, oh = 1 * S, 1 * S, 10 * S, 16 * S
    if deco:
        kind = deco[0]
        if kind == "supreme":
            txt = text_img("Supreme", (255, 255, 255, 255), 2).rotate(-90, expand=True)
            bw, bh = txt.width + 14, txt.height + 14
            bx, by = ox + (ow - bw) // 2, oy + (oh - bh) // 2
            d.rectangle([bx, by, bx + bw - 1, by + bh - 1], fill=(237, 28, 36, 255))
            img.paste(txt, (bx + 7, by + 7), txt)
        elif kind == "vtext":
            txt = text_img(deco[1], deco[2], 2).rotate(-90, expand=True)
            img.paste(txt, (ox + (ow - txt.width) // 2, oy + (oh - txt.height) // 2), txt)
        elif kind == "boss":
            txt = text_img("BOSS", (255, 255, 255, 255), 3)
            img.paste(txt, (ox + (ow - txt.width) // 2, oy + (oh - txt.height) // 2), txt)
    img.save(os.path.join(ROOT, "cape", name + ".png"))


BLACK = (17, 17, 20, 255)
WHITE = (242, 242, 242, 255)
BLUE = (30, 95, 216, 255)
PURPLE = (106, 31, 181, 255)
W_ = (255, 255, 255, 255)
K_ = (10, 10, 10, 255)

make_cape("supreme_black", BLACK, (60, 60, 66, 255), ("supreme",))
make_cape("supreme_white", WHITE, (170, 170, 170, 255), ("supreme",))
make_cape("bulbely_black", BLACK, (60, 60, 66, 255), ("vtext", "bu\u0142be\u0142y", W_))
make_cape("bulbely_white", WHITE, (170, 170, 170, 255), ("vtext", "bu\u0142be\u0142y", K_))
make_cape("bulbely_blue", BLUE, (20, 60, 150, 255), ("vtext", "bu\u0142be\u0142y", W_))
make_cape("purple", PURPLE, (70, 15, 125, 255), None)
make_cape("boss_purple", PURPLE, (70, 15, 125, 255), ("boss",))
make_cape("boss_black", BLACK, (60, 60, 66, 255), ("boss",))

# ---------- skrzydla (polowka 160x200, szczyt skrzydla po lewej, kregoslup po prawej) ----------
WW, WH = 160, 200
SH = (150, 12)   # bark
EL = (75, 10)    # lokiec
TIPS = [(0, 45), (12, 105), (45, 160), (100, 196), (140, 170)]


def lerp(a, b, t):
    return (a[0] + (b[0] - a[0]) * t, a[1] + (b[1] - a[1]) * t)


def outline(scallop):
    pts = [SH, EL, TIPS[0]]
    for i in range(len(TIPS) - 1):
        m = lerp(TIPS[i], TIPS[i + 1], 0.5)
        m = lerp(m, EL, scallop)
        pts += [m, TIPS[i + 1]]
    return pts


def dragon(name, membrane, bone, line):
    img = Image.new("RGBA", (WW, WH), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    pts = outline(0.30)
    d.polygon(pts, fill=membrane)
    d.line(pts + [pts[0]], fill=line, width=3)
    d.line([SH, EL], fill=bone, width=8)
    for t in TIPS:
        d.line([EL, t], fill=bone, width=6)
    d.line([SH, TIPS[-1]], fill=bone, width=5)
    return img


def fire(name):
    rnd = random.Random(7)
    img = Image.new("RGBA", (WW, WH), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    base = outline(0.12)
    # poszarpane jezyki ognia
    jag = []
    for i in range(len(base)):
        a, b = base[i], base[(i + 1) % len(base)]
        jag.append(a)
        if i >= 2:  # tylko krawedz zewnetrzna
            n = 4
            dx, dy = a[0] - EL[0], a[1] - EL[1]
            for k in range(1, n):
                p = lerp(a, b, k / n)
                ox, oy = p[0] - EL[0], p[1] - EL[1]
                ln = max(1.0, (ox * ox + oy * oy) ** 0.5)
                amp = (11 if k % 2 else -7) + rnd.randint(-3, 3)
                jag.append((p[0] + ox / ln * amp, p[1] + oy / ln * amp))
    layers = [(1.0, (190, 25, 5, 255)), (0.86, (240, 85, 10, 255)),
              (0.68, (255, 160, 20, 255)), (0.48, (255, 225, 90, 255)),
              (0.28, (255, 250, 200, 255))]
    for k, col in layers:
        poly = [(SH[0] + (x - SH[0]) * k, SH[1] + (y - SH[1]) * k) for x, y in jag]
        d.polygon(poly, fill=col)
    return img


def save_wing(name, right_half):
    tex = Image.new("RGBA", (WW * 2, WH), (0, 0, 0, 0))
    tex.paste(right_half.transpose(Image.FLIP_LEFT_RIGHT), (0, 0))
    tex.paste(right_half, (WW, 0))
    tex.save(os.path.join(ROOT, "wings", name + ".png"))


save_wing("dragon_black", dragon("b", (18, 14, 24, 255), (52, 46, 60, 255), (95, 60, 140, 255)))
save_wing("dragon_white", dragon("w", (212, 212, 226, 255), (255, 255, 255, 255), (130, 130, 150, 255)))
save_wing("fire", fire("f"))

import math

def angel():
    img = Image.new("RGBA", (WW, WH), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    O = (142, 28)

    def feather(angle_deg, L, w, c_out, c_mid, c_in):
        a = math.radians(angle_deg)
        dx, dy = math.cos(a), math.sin(a)
        px, py = -dy, dx

        def poly(f):
            Lf = L * f
            return [(O[0] + px * w / 2, O[1] + py * w / 2),
                    (O[0] + dx * Lf * 0.55 + px * w / 2, O[1] + dy * Lf * 0.55 + py * w / 2),
                    (O[0] + dx * Lf, O[1] + dy * Lf),
                    (O[0] + dx * Lf * 0.55 - px * w / 2, O[1] + dy * Lf * 0.55 - py * w / 2),
                    (O[0] - px * w / 2, O[1] - py * w / 2)]
        d.polygon(poly(1.0), fill=c_out)
        d.polygon(poly(0.80), fill=c_mid)
        d.polygon(poly(0.58), fill=c_in)

    for ang in range(182, 92, -5):
        t = (ang - 92) / 90.0
        L = 132 + 36 * (1 - t)
        feather(ang, L, 24, (80, 140, 235, 255), (150, 195, 250, 255), (235, 243, 255, 255))
    for ang in range(178, 96, -7):
        feather(ang, 100, 22, (190, 215, 250, 255), (235, 243, 255, 255), (255, 255, 255, 255))
    d.line([(148, 22), (105, 14), (60, 16), (14, 26)], fill=(222, 170, 40, 255), width=7)
    return img


save_wing("angel", angel())
save_wing("green", dragon("g", (28, 105, 48, 255), (190, 196, 204, 255), (15, 60, 25, 255)))

# ---------- kocia glowa (64x32 x8) ----------
os.makedirs(os.path.join(ROOT, "hats"), exist_ok=True)


def make_cat():
    rnd = random.Random(3)
    img = Image.new("RGBA", (64 * S, 32 * S), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    base = (152, 152, 158)

    def texel(x, y, col):
        d.rectangle([x * S, y * S, (x + 1) * S - 1, (y + 1) * S - 1], fill=col + (255,))

    for y in range(16):
        for x in range(32):
            v = rnd.randint(-12, 12)
            texel(x, y, tuple(max(0, min(255, c + v)) for c in base))
    dark = (92, 92, 98)
    # pregi na czubku glowy (gora: 8..16, 0..8)
    for x in (10, 12, 14):
        for y in range(1, 7):
            texel(x, y, dark)
    # prawa/lewa/tyl - pregi
    for fx0 in (0, 16, 24):
        for y in (10, 12, 14):
            for x in range(fx0, fx0 + 8):
                if (x + y) % 2 == 0:
                    texel(x, y, dark)
    # przod (8..16, 8..16)
    fx, fy = 8, 8
    for (x, y) in [(3, 0), (4, 0), (3, 1), (4, 1), (1, 1), (6, 1)]:
        texel(fx + x, fy + y, dark)
    for (x, y) in [(2, 5), (3, 5), (4, 5), (5, 5), (2, 6), (3, 6), (4, 6), (5, 6), (3, 7), (4, 7)]:
        texel(fx + x, fy + y, (228, 228, 230))
    for (x, y) in [(1, 3), (2, 3), (1, 4), (2, 4), (5, 3), (6, 3), (5, 4), (6, 4)]:
        texel(fx + x, fy + y, (130, 205, 70))
    for (x, y) in [(2, 3), (2, 4), (5, 3), (5, 4)]:
        texel(fx + x, fy + y, (10, 10, 10))
    for (x, y) in [(3, 5), (4, 5)]:
        texel(fx + x, fy + y, (236, 150, 162))
    for (x, y) in [(3, 6), (4, 6)]:
        texel(fx + x, fy + y, (80, 80, 85))
    for (x, y) in [(0, 5), (7, 5), (0, 6), (7, 6)]:
        texel(fx + x, fy + y, (240, 240, 240))
    # uszy (32..38, 0..4)
    for y in range(4):
        for x in range(32, 38):
            texel(x, y, (145, 145, 150))
    for (x, y) in [(33, 2), (34, 2), (33, 3), (34, 3)]:
        texel(x, y, (240, 170, 180))
    img.save(os.path.join(ROOT, "hats", "cat.png"))


make_cat()
print("OK")
