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

package de.mtplayer.controller.config;

import de.mtplayer.mLib.tools.Functions;

public class Const {

    public static final String PROGRAMMNAME = "Mosaik";
    public static final String USER_AGENT_DEFAULT = "";
    public static final String CONFIG_FILE = "mosaik.xml";
    public static final String CONFIG_FILE_COPY = "modaik.xml_copy_";
    public static final String CSS_FILE = "/de/mtplayer/mtfx.css";


    public static final String ADRESSE_MTPLAYER = "https://www.mtplayer.de";
    public static final String ADRESSE_MTPLAYER_CONFIGS = "https://www.mtplayer.de/mtp/v-" + Functions.getProgVersion() + "/";
    public static final String ADRESSE_MTPLAYER_VERSION = "https://www.mtplayer.de/mtp/prog-version.xml";

    public static final String ADRESSE_PROGRAMM_VERSION = "https://res.mediathekview.de/prog-info-13.xml";
    public static final String ADRESSE_DOWNLAD = "https://mediathekview.de/download/";
    public static final String ADRESSE_ANLEITUNG = "https://mediathekview.de/anleitung/";
    public static final String ADRESSE_ONLINE_HELP = "https://mediathekview.de/anleitung/";
    public static final String ADRESSE_VORLAGE_PROGRAMMGRUPPEN = "https://res.mediathekview.de/programmgruppen13/programmgruppen.xml";
    public static final String ADRESSE_WEBSITE = "https://mediathekview.de/";
    public static final String ADRESSE_FORUM = "https://forum.mediathekview.de/";
    public static final String ADRESSE_DONATION = "https://mediathekview.de/spenden/";

    // Dateien/Verzeichnisse
    public static final String VERZEICHNIS_PROGRAMM_ICONS = "Icons/Programm"; // Unterverzeichnis im Programmverzeichnis
    public static final String VERZEICHNIS_SENDER_ICONS = "Icons/Sender"; // Unterverzeichnis im Programmverzeichnis
    public static final String VERZEICHNIS_EINSTELLUNGEN = ".mosaik"; // im Homeverzeichnis
    public static final String JSON_DATEI_FILME = "filme.json";

    public static final String XML_START = "Mosaik";


    public static final String GUI_FILME_FILTER_DIVIDER_LOCATION = "0.3";
    public static final String GUI_DOWNLOAD_FILTER_DIVIDER_LOCATION = "0.3";
    public static final String GUI_ABO_FILTER_DIVIDER_LOCATION = "0.3";

    public static final String GUI_FILME_DIVIDER_LOCATION = "0.7";
    public static final String GUI_DOWNLOAD_DIVIDER_LOCATION = "0.7";
    public static final String GUI_ABO_DIVIDER_LOCATION = "0.7";
    public static final String GUI_MSG_DIVIDER_LOCATION = "0.7";
    public static final String GUI_MSG_LOG_DIVIDER_LOCATION = "0.5";

    public static final String CONFIG_DIALOG_SET_DIVIDER = "0.2";

    public static final int DOWNLOAD_CHART_MAX_TIME = 30; // Minuten

    public static final int LAENGE_DATEINAME_MAX = 200; // Standardwert für die Länge des Zieldateinamens
    public static final int LAENGE_FELD_MAX = 100; // Standardwert für die Länge des Feldes des
    public final static int MAX_COPY_BACKUPFILE = 5; // Maximum number of backup files to be stored.
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static final int MIN_TABLE_HEIGHT = 250;


}
