/*
 *    Copyright (C) 2008
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package mosaik.daten;

public class Konstanten {

    public static final String KONST_LOG_DATEI = ".mosaik_log";
    public static final String KONST_PROJEKTDATEI_SUFIX = "mosaik";
    public static final String KONST_ARCHIV = "bilder";
    public static final String KONST_XML_DATEI = ".mosaik";
    public static final String KONST_XML_START = "Mosaik";
    public static final String KONST_KODIERUNG_UTF = "UTF-8";
    public static final String IMAGE_FORMAT_JPG = "jpg";
    public static final String IMAGE_FORMAT_PNG = "png";
    //Tags System
    public static final String SYSTEM = "System";
    public static final int SYSTEM_MAX_ELEM = 2;
    public static final String SYSTEM_PROJEKTDATEI_PFAD = "System-Projektdatei";
    public static final int SYSTEM_PROJECTDATEI_PFAD_NR = 0;
    public static final String SYSTEM_PROJECT_NAME = "System-Projektname";
    public static final int SYSTEM_PROJECT_NAME_NR = 1;
    public static final String[] SYSTEM_COLUMN_NAMES = {SYSTEM_PROJEKTDATEI_PFAD, SYSTEM_PROJECT_NAME};
    //Tags Farben
    public static final String FARBEN = "Farben";
    public static final int FARBEN_MAX_ELEM = 6;
    public static final String FARBEN_PFAD = "Pfad";
    public static final int FARBEN_PFAD_NR = 0;
    public static final String FARBEN_R = "Rot";
    public static final int FARBEN_R_NR = 1;
    public static final String FARBEN_G = "Grün";
    public static final int FARBEN_G_NR = 2;
    public static final String FARBEN_B = "Blau";
    public static final int FARBEN_B_NR = 3;
    public static final String FARBEN_FARBE = "Farbe";
    public static final int FARBEN_FARBE_NR = 4;
    public static final String FARBEN_BENUTZEN = "Benutzen";
    public static final int FARBEN_BENUTZEN_NR = 5;
    public static final String[] FARBEN_COLUMN_NAMES = {FARBEN_PFAD, FARBEN_R, FARBEN_G, FARBEN_B, FARBEN_FARBE,
                                                        FARBEN_BENUTZEN
    };
    //Tags Projekt
    public static final String PROJEKT = "Projekt";
    public static final int PROJEKT_MAX_ELEM = 11;
    public static final String PROJEKT_NAME = "Projekt-Name";
    public static final int PROJEKT_NAME_NR = 0;
    public static final String PROJEKT_PFAD_BILDER = "Projekt-Pfad-Bilder";
    public static final int PROJEKT_PFAD_BILDER_NR = 1;
    public static final String PROJEKT_PFAD_TAPETE_ZIEL = "Projekt-Pfad-Tapete-Ziel";
    public static final int PROJEKT_PFAD_TAPETE_ZIEL_NR = 2;
    public static final String PROJEKT_AUFLOESUNG_ZIEL = "Projekt-Aufloesung-Ziel";
    public static final int PROJEKT_AUFLOESUNG_ZIEL_NR = 3;
    public static final String PROJEKT_MOSAIK_QUELLE = "Projekt-Mosaik-Quelle";
    public static final int PROJEKT_MOSAIK_QUELLE_NR = 4;
    public static final String PROJEKT_MOSAIK_ZIEL = "Projekt-Mosaik-Ziel";
    public static final int PROJEKT_MOSAIK_ZIEL_NR = 5;
    public static final String PROJEKT_MOSAIK_FORMAT = "Projekt-Mosaik-Format";
    public static final int PROJEKT_MOSAIK_FORMAT_NR = 6;
    public static final String PROJEKT_ARCHIV_FORMAT = "Projekt-Archiv-Format";
    public static final int PROJEKT_ARCHIV_FORMAT_NR = 7;
    public static final String PROJEKT_ARCHIV_RECHTECK = "Projekt-Archiv-Rechteck";
    public static final int PROJEKT_ARCHIV_RECHTECK_NR = 8;
    public static final String PROJEKT_ANZAHL_BILD = "Projekt-Anzahl-Bild";
    public static final int PROJEKT_ANZAHL_BILD_NR = 9;
    public static final String PROJEKT_BILDER_HOEHE = "Projekt-Bilder-Hoehe";
    public static final int PROJEKT_BILDER_HOEHE_NR = 10;
    public static final String[] PROJEKT_COLUMN_NAMES = {PROJEKT_NAME, PROJEKT_PFAD_BILDER, PROJEKT_PFAD_TAPETE_ZIEL,
                                                         PROJEKT_AUFLOESUNG_ZIEL, PROJEKT_MOSAIK_QUELLE,
                                                         PROJEKT_MOSAIK_ZIEL, PROJEKT_MOSAIK_FORMAT,
                                                         PROJEKT_ARCHIV_FORMAT, PROJEKT_ARCHIV_RECHTECK, PROJEKT_ANZAHL_BILD,
                                                         PROJEKT_BILDER_HOEHE
    };
}


