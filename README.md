# Spigot/Bukkit & BungeeCord Plugins

Dieses repo archiviert Plugins, die ich damals im Betrieb eines kleines Minecraft-Servernetzwerkes entwickelt habe. Teilweise ist Code nicht 100% von mir, sondern auch von einem damaligen Kumpel, der auch bei diesem Netzwerk dabei war, geschrieben worden. Information dazu: Das Netzwerk war primär ein Minigame-Netzwerk.

## Inhalt

- **BedWars** (Minigame): Ein Minigame, in welchem mehrere Teams (min. 2) auf fliegenden Inseln gegeneinander antreten und die gegnerischen Betten zerstören müssen, um das respawnen der Gegner zu verhindern.
- **KnockbackFFA** (Minigame): Ein Minigame, in welchem auf einer Map, primär bestehend aus fliegenden Inseln, man versuchen muss sich gegeneinander runterzuschlagen. Dabei gibt es verschiedene Kits, welche z.B. Schneebälle oder ähnliches enthalten um die Gegner runterzuschlagen. Dabei gibt es keinen Gewinner, sondern nur Kill-Streaks. Das Spiel geht "unendlich".
- **LobbySystem**: Ein Plugin, welches den Lobby-Server mit leben füllt. Dabei gibt es einen Navigator, Gadgets, Settings, etc. Ein solches System ist eigentlich bekannt von jedem Server welcher eine Lobby zur Spiel auswahl hat.
- **NickAPI** (Lib-Plugin): Ein Plugin welches Funktionen zur veränderung eines Spielernamens, um under cover zu sein, bietet. Andere Plugins (z.B. auch das BedWars- und KnockbackFFA-Plugin) haben dies als Dependency.
- **PPermissions-BungeeCord & -Spigot**: Permission-Verwaltungsplugin mit Rollen, welche auch auf Zeit angewendet werden können. Außerdem verwaltet es Prefixe im Chat und in der Tablist.
- **ReplaySystem**: Ein ReplaySystem, welches z.B. eine Runde eines Minigames aufzeichnet und später wieder abspielen kann. Dabei kann man sich in der Welt beim Abspielen frei bewegen. Ist nach meinem Wissensstand nicht fehlerfrei und wurde damals nur für Spieler-Reports von Teammitgliedern eingesetzt.

## Weiteres

Diese Plugins sind mittlerweile einige Jahre alt und waren damals so wie sie hier zu sehen sind im Einsatz. Es kann sein, dass Dependencies hier im Repo fehlen, da ich nur den Sourcecode aufgehoben habe. Außerdem ist mir bewusst, dass hier teilweise Code enthalten ist, welcher nicht wirklich effizent ist (Beispiel: Jedes mal Daten von der Datenbank ziehen, anstatt dies nur einmal zu tun und das Ergebniss z.B. bis zum Verlassen des Servers zu cachen).