
[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)

# Mosaik

Mit dem Programm Mosaik lassen sich aus Bildersammlungen Fotomosaiks erstellen. Bei einem Foto als Vorlage werden die einzelnen Pixel durch Minibilder aus der Bildersammlungen mit passender Farbe ersetzt.
<br />


## Anleitung

Nach dem ersten Programmstart muss ein Ordner für das neue Projekt ausgewählt werden. Im zweiten Tab kann man dann Bilder importieren. Diese werden als Kopie im Ordner: *Projektordner/Bilder* in einer Größe von 600x600 Pixel gespeichert. Diese Miniaturbilder werden die "Pixel" des Fotomosaiks. Im dritten Tab kann dann das Fotomosaik erstellt werden. Dort können auch die verschiedenen Einstellungen zum Erstellen des Mosaiks vorgenommen werden (z.B. Größe).
<br />


## Infos

Die Qualität der erstellten Mosaiks hängt sehr von der Anzahl der importierten Miniaturbilder ab. Für gute Ergebnisse sollten es mehr als 1000 Bilder sein.
<br />
<br />
Ein guter Wert für die Größe und Anzahl der Miniaturbilder im Mosaik ist 50 - 100 Pixel fürs Miniaturbild und 100 davon pro Zeile. Beim Erstellen des Mosaiks sollte man die Größe nicht außer Acht lassen. Hier ein paar Kennzahlen dazu:
<br /><br />


| Anz. Pixel (Höhe, Breite) jedes Minibildes |Anz. Minibilder im Mosaik pro Zeile| Anz. Pixel des erstellten Mosaik pro Zeile  | Arbeitsspeicher den das Programm braucht |Dateigröße des erstellten Mosaik |
|:--:|:--:|:--:|:--:|:--:|
| 50 | 100 | 5.000 | 500 MByte | 10 MByte |
| 100 | 100 | 10.000 | 1 GByte | 25 MByte |
| 250 | 100 | 25.000 | 5 GByte | 150 MByte |
<br />

Die resultierende Dateigröße des Mosaiks hängt auch sehr vom Inhalt des Bildes ab. Ein weißes PNG mit 10.000x10.000 Pixel hat nur eine Dateigröße von 300 kByte. Als farbiges Bild kann es mehr als 100 MByte haben.
<br />

Sollte das Programm für große Mosaiks mehr Speicher brauchen, kann es aus einer Dosbox/Terminal heraus gestartet werden (z.B. stellt das 4GByte Speicher zur Verfügung):

*java -Xmx4G -jar ./Mosaik.jar*

oder für Windows

*java -Xmx4G -jar "C:\Users\PFAD\Mosaik.jar"*
<br />


## Systemvoraussetzungen

Unterstützt wird Windows und Linux. 

*bis Programmversion 3*

Das Programm benötigt unter Windows und Linux eine aktuelle Java-VM ab Version: 1.8 (= Java 8, Java 9, Java 10).
Für Linux-Benutzer wird OpenJDK8 empfohlen. Außerdem benötigen Linux Benutzer die aktuelle Version von JavaFX (OpenJFX).

**ab Programmversion 4**

Das Programm benötigt unter Windows und Linux eine aktuelle Java-VM ab Version: Java 11.
Für Linux-Benutzer wird OpenJDK11 empfohlen. (FX-Runtime bringt das Programm bereits mit und muss nicht installiert werden).
<br />


## Download

Das Programm wird in drei Paketen angeboten. Diese unterscheiden sich nur im "Zubehör", das Programm selbst ist in allen Paketen identisch:

- **Mosaik-XX.zip**  
Das Programmpaket bringt nur das Programm aber kein Java mit. Auf dem Rechner muss eine Java-Laufzeitumgebung ab Java11 installiert sein. Dieses Programmpaket kann auf allen Betriebssystemen verwendet werden. Es bringt Startdateien für Linux und Windows mit.

- **Mosaik-XX__Linux+Java.zip**  
**Mosaik-XX__Win+Java.zip**  
Diese Programmpakete bringen die Java-Laufzeitumgebung mit und sind nur für das angegebene Betriebssystem: Linux oder Windows. Es muss kein Java auf dem System installiert sein. (Die Java-Laufzeitumgebung liegt im Ordner "Java" und kommt von jdk.java.net).

zum Download: [github.com/xaverW/Mosaik/releases](https://github.com/xaverW/Mosaik/releases)
<br />


## Links

[www.p2tools.de/mosaik/]( https://www.p2tools.de/mosaik/)

