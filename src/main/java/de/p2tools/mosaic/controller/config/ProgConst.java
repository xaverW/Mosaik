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

package de.p2tools.mosaic.controller.config;

public class ProgConst {

    public static final String P2_PROGRAMMNAME = "P2Tools - Mosaik";
    public static final String PROGRAMNAME = "Mosaik";

    // config file
    public static final String CONFIG_FILE = "mosaik.xml";
    public static final String CONFIG_DIRECTORY = "p2Mosaik"; // im Homeverzeichnis
    public static final String XML_START = "Mosaik";

    public static final String CSS_FILE = "/de/p2tools/mosaic/mosaik.css";
    public static final String CSS_FILE_DARK_THEME ="/de/p2tools/mosaic/mosaik-dark.css";

    public static final String LOG_DIR = "Log";

    // Website MTPlayer
    public static final String ADRESSE_WEBSITE = "https://p2tool.de/mosaik/";
    public static final String ADRESSE_WEBSITE_HELP = "https://p2tool.de/mosaik/manual.html";
    public static final String WEBSITE_P2 = "https://www.p2tools.de";
    public static final String WEBSITE_PROG_UPDATE = "https://www.p2tools.de/extra/mosaik-info.xml";

    public static final String P2_ICON = "P2.png";
    public static final String P2_ICON_32 = "P2_32.png";
    public static final String P2_ICON_PATH = "/de/p2tools/mosaic/icon/";

    // Dateien/Verzeichnisse
    public static final String DIR_PROGRAM_ICONS = "Icons/Programm"; // Unterverzeichnis im Programmverzeichnis
    public static final String DIR_THUMBS = "Bilder"; // im Projektverzeichnis für die Thumbs
    public static final String DIR_STANDARD_PROJECT = "Mosaik"; // Standardname fürs Projektverzeichnis
    public static final String MOSAIC_STD_NAME = "Mosaik";
    public static final String WALLPAPER_STD_NAME = "Tapete";


    public static final double GUI_THUMB_DIVIDER_LOCATION = 0.7;

    public static final int THUMB_RESOLUTION = 600; // Auflösung der Thumbs in der ThumbCollection
    public static final float IMG_JPG_COMPRESSION = 0.7f; // der Wert kann zwischen 0.0 .. 1.0 liegen, 1 ist keine
}
