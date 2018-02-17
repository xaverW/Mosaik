
[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)

# Mosaik

Mit dem Programm Mosaik lassen sich aus Bildersammlungen Fotomosaiks erstellen. Bei einem Foto als Vorlage werden die einzelnen Pixel durch Minibilder aus der Bildersammlungen mit passender Farbe ersetzt.



## Anleitung
Nach dem ersten Programmstart muss ein Ordner für das neue Projekt ausgewählt werden. Im zweiten Tab kann man dann Bilder importieren. Diese werden als Kopie im Ordner: *Projektordner/Bilder* in einer Größe von 600x600 Pixel gespeichert. Diese Miniaturbilder werden die "Pixel" des Fotomosaiks. Im dritten Tab kann dann das Fotomosaik erstellt werden. Dort können auch die verschiedenen Einstellungen zum Erstellen des Mosaiks vorgenommen werden (z.B. Größe).



## Infos
Die Qualität der erstellten Mosaiks hängt von der Anzahl der importierten Miniaturbilder ab. Für gute Ergebnisse sollten es mehr als 1000 Bilder sein. Beim Erstellen des Mosaiks sollte man die Größe nicht außer Acht lassen. Hier ein paar Kennzahlen dazu:
<br /><br />


| Anz. Pixel Minibilder |Anz. Minibilder im Mosaik pro Zeile| Anz. Pixel des erstellten Mosaik pro Zeile  | Arbeitsspeicher den das Programm braucht |Dateigröße des erstellten Mosaik |
|--|--|--|--|--|
| 250 | 100 | 25.000 | 6 GByte | 650 MByte |
| 100 | 100 | 10.000 | <2 GByte | 150 MByte |
| 50 | 100 | 5.000 | <1 GByte | 40 MByte |


<br /><br />
Die resultierende Dateigröße des Mosaiks hängt auch sehr vom Inhalt des Bildes ab. Ein weißes PNG mit 10.000x10.000 Pixel hat nur eine Dateigröße von 300 kByte. Als farbiges Bild kann es mehr als 100 MByte haben.


<br /><br />
Sollte das Programm für große Mosaiks mehr Speicher brauchen, kann es aus einer Dosbox/Terminal heraus gestartet werden (z.B. stellt das 4GByte Speicher zur Verfügung):

*java -Xmx4G -jar ./Mosaik.jar*

oder für Windows

*java -Xmx4G -jar "C:\Users\PFAD\Mosaik.jar"*
