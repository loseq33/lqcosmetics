# KloccCosmetics (Fabric 1.21.4)

Mod kosmetyczny **tylko po stronie klienta**: skrzydla i peleryny widoczne tylko dla Ciebie
(na Tobie lub na wybranych graczach). Serwer i inni gracze nic nie widza i nie potrzebuja moda.

## Ustawianie
* Mod Menu -> KloccCosmetics -> ikona konfiguracji, albo klawisz **K** (do zmiany w Sterowaniu).
* Pole nicku puste = Ty. Wpisz nick innego gracza zeby nalozyc kosmetyke na niego.

## Budowanie
Najprosciej: pobierz oficjalny szablon "fabric-example-mod" (branch 1.21.4),
skopiuj z tego projektu: `src/`, `build.gradle`, `gradle.properties`, `settings.gradle`
(nadpisujac pliki szablonu) i uruchom `./gradlew build`.
Gotowy plik jar: `build/libs/kloccosmetics-1.0.0.jar`.
Wymagane: JDK 21, Fabric Loader >= 0.16.10, Fabric API 0.119.x dla 1.21.4.
(Mod Menu jest opcjonalny w grze, ale uzyty jako zaleznosc kompilacji.)

## Tekstury
`python tools/gen_textures.py` (Pillow) generuje wszystkie PNG od nowa.
Mozesz je tez podmienic wlasnymi:
* `textures/cape/*.png` - peleryny (format 64x32 pomnozony x8, tu 512x256)
* `textures/wings/*.png` - skrzydla (polowa lewa = przod, prawa = tyl)
