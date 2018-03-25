/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://sourceforge.net/projects/mtplayer/
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */

package de.p2tools.mosaik.gui;

public class HelpText {

    public static final String PROJECT_PATH = "Hier wird der Ordner ausgewählt, " +
            "in dem das Mosaik erstellt wird. In dem Ordner werden auch die " +
            "erstellten Vorschaubilder zum Bauen des Mosaik gespeichert." +
            "\n\n" +
            "Der Projektname dient nur der eigenen Erinnerung.";

    public static final String GET_THUMB_DIR = "Aus den Fotos in dem gewählten Ordner werden " +
            "Miniaturbilder erstellt. Diese werden dann im Verzeichnis des Projekts gespeichert. " +
            "Die Miniaturbilder werden zum Erstellen des Mosaik verwendet\n\n" +
            "Für gute Ergebnisse sollten mindestens 1000 Bilder importiert werden.";

    public static final String IMAGE_TAMPLATE = "Dieses Foto wird als Vorlage für das Mosaik verwendet. " +
            "Das Mosaik ist ein neues Bild bei dem die Bildpunkte der Vorlage durch " +
            "ein passendes Miniaturbild ersetzt werden.";

    public static final String MOSAIK_DEST = "Das erstellte Mosaik wird in dieser Datei gespeichert.";


    public static final String MOSAIK_PIXEL_SIZE = "" +
            "Die Pixel des Vorlagenfoto werden durch Miniaturbilder ersetzt. " +
            "Hier kann die Größe und die Anzahl dieser Miniaturbilder vorgegeben werden. " +
            "\n\n" +

            "Die Größe (in Pixel für die Breite und Höhe) dieser Miniaturbilder " +
            "wird mit dem ersten Regler eingestellt. Ein guter Wert liegt zwischen 50 und 100 Pixel." +
            "\n\n" +

            "Das Mosaik wird mit \"Anzahl\" Miniaturbildern pro Zeile aufgebaut. Dieser Wert wird " +
            "mit dem zweiten Regler vorgegeben. " +
            "Dadurch hat das erstellte Mosaik eine Breite von \"Anzahl\" * \"Pixelgröße\" in Pixeln. " +
            "Ein gutes Ergebnis bekommt man etwa ab 100 Miniaturbildern pro Zeile." +
            "\n\n" +

            "Je größer das Mosaik wird, desto größer wird auch die erstellte Datei. Ein Mosaik mit 100 Pixeln für das " +
            "Miniaturbild und einer Anzahl von 100 Miniaturbildern hat etwa eine Größe von 25 MByte. " +
            "Die resultierende Dateigröße des Mosaiks hängt auch sehr vom Inhalt des Bildes ab.";

//    public static final String MOSAIK_PIXEL_COUNT = "Dadurch wird die Größe des erstellten Mosaik vorgegeben. " +
//            "Das Mosaik wird mit \"Anzahl\" Miniaturbildern pro Zeile aufgebaut. Dadurch hat " +
//            "das erstellte Mosaik eine Breite von \"Anzahl\" * \"Pixelgröße\" in Pixeln. " +
//            "Je größer das Mosaik wird, desto größer wird auch die erstellte Datei. " +
//            "Ein gutes Ergebnis bekommt man etwa ab 100 " +
//            "Miniaturbildern pro Zeile.";

    public static final String WALLPAPER_DEST = "Die erstellte Fototapete wird in der angegebenen Datei " +
            "gespeichert.";

    public static final String WALLPAPER_PIXEL_SIZE = "Hier kann die Größe (in Pixel) und die Anzahl (pro Zeile) " +
            "der Miniaturbilder für die Fototapete vorgegeben werden." +
            "\n\n" +
            "Mit der Größe der Miniaturbilder und ihrer Anzahl pro Zeile " +
            "wird auch die Größe der erstellten Fototapete festgelegt. " +
            "Die erstellte Fototapete hat eine Breite von \"Anzahl\" * \"Pixelgröße\" in Pixeln. Die Höhe hängt " +
            "dann von der Anzahl der Miniaturbilder ab: \n" +
            "Anzahl Zeilen = Anzahl Miniaturbilder in der Liste / Anzahl " +
            "Miniaturbilder pro Zeile";

    public static final String MOSAIK_PIXEL_FOTO = "Hier kann ausgewählt werden, was als Miniaturbilder " +
            "zum Bauen des Mosaik verwendet wird." +
            "\n\n" +
            "Möglichkeit 1: Es werden die erstellten Miniaturbilder des Projekts verwendet." +
            "\n\n" +
            "Möglichkeit 2: Aus der Fotovorlage werden Miniaturbilder erstellt, " +
            "diese werden dann passend \"eingefärbt\" und dann zum Bauen des Mosaik verwendet.";

    public static final String MOSAIK_BW = "Das Mosaik wird als Schwarz-Weiß Bild erstellt.";

    public static final String THUMB_SIZE = "Die Größe der Miniaturbilder wird geringfügig verkleinert. " +
            "Werden z.B. die hellen \"Pixel\" des Mosaik " +
            "verkleinert, werden dadurch die dunklen \"Pixel\" hervorgehoben.";

    public static final String THUMB_RESIZE = "Um die Größe wird das Miniaturbild verkleinert.";

    public static final String THUMB_BORDER = "Um die einzelnen Miniaturbilder kann ein Rahmen " +
            "gezeichnet werden. Die Miniaturbilder haben dann also einen Abstand von 2 x Rahmen. " +
            "Mit dem Regler kann die Breite des Rahmens (in Pixel), also der Abstand der Miniaturbilder " +
            "eingestellt werden." +
            "\n" +
            "Mit der Farbauswahl kann eine Hintergrundfarbe gesetzt werden die dann zwischen den Miniaturbilder " +
            "zu sehen ist (oder aber ein Hintergrundbild).";

}
