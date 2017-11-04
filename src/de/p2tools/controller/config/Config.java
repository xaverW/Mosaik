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


package de.p2tools.controller.config;

import com.jidesoft.utils.SystemInfo;
import de.p2tools.mLib.tools.*;

public class Config extends MLConfig {

    public static final String SYSTEM = "system";

    // ============================================
    public static MLConfigs SYSTEM_BUILD_NR = addNewKey("BuildNr");
    public static MLConfigs SYSTEM_UPDATE_SEARCH = addNewKey("Update-Suchen", Boolean.TRUE.toString());
    public static MLConfigs SYSTEM_UPDATE_DATE = addNewKey("Update-Datum");

    // wegen des Problems mit ext. Programmaufrufen und Leerzeichen
    public static MLConfigs SYSTEM_USE_REPLACETABLE = addNewKey("Ersetzungstabelle-verwenden", SystemInfo.isLinux() || SystemInfo.isMacOSX() ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
    public static MLConfigs SYSTEM_ONLY_ASCII = addNewKey("nur-ascii", Boolean.FALSE.toString());
    public static MLConfigs SYSTEM_INFO_NR_SHOWN = addNewKey("Hinweis-Nr-angezeigt");
    public static MLConfigs SYSTEM_PROG_OPEN_DIR = addNewKey("Programm-Ordner-oeffnen");
    public static MLConfigs SYSTEM_PROG_OPEN_URL = addNewKey("Programm-Url-oeffnen");
    public static MLConfigs SYSTEM_PROG_PLAY_FILE = addNewKey("Programm-zum-Abspielen");

    // Fenstereinstellungen
    public static MLConfigs SYSTEM_GROESSE_GUI = addNewKey("Groesse-Gui", "1000:900");
    public static MLConfigs SYSTEM_GROESSE_DIALOG_FILMINFO = addNewKey("Groesse-Filminfo", "600:800");
    public static MLConfigs SYSTEM_ICON_STANDARD = addNewKey("Icon-Standard", Boolean.TRUE.toString());
    public static MLConfigs SYSTEM_ICON_PFAD = addNewKey("Icon-Pfad");



    // Film
    public static MLConfigs FILM_GUI_FILTER_DIVIDER = addNewKey("Film-Gui-Filter-Divider", Const.GUI_FILME_FILTER_DIVIDER_LOCATION);
    public static MLConfigs FILM_GUI_FILTER_DIVIDER_ON = addNewKey("Film-Gui-Filter-Divider-On", Boolean.TRUE.toString());
    public static MLConfigs FILM_GUI_DIVIDER = addNewKey("Film-Gui-Divider", Const.GUI_FILME_DIVIDER_LOCATION);
    public static MLConfigs FILM_GUI_DIVIDER_ON = addNewKey("Film-Gui-Divider-On", Boolean.TRUE.toString());
    public static MLConfigs FILM_GUI_TABLE_WIDTH = addNewKey("Film-Gui-Table-Width");
    public static MLConfigs FILM_GUI_TABLE_SORT = addNewKey("Film-Gui-Table-Sort");
    public static MLConfigs FILM_GUI_TABLE_UPDOWN = addNewKey("Film-Gui-Table-UpDown");
    public static MLConfigs FILM_GUI_TABLE_VIS = addNewKey("Film-Gui-Table-Vis");
    public static MLConfigs FILM_GUI_TABLE_ORDER = addNewKey("Film-Gui-Table-Order");

    // Download
    public static MLConfigs DOWNLOAD_GUI_FILTER_DIVIDER = addNewKey("Download-Gui-Filter-Divider", Const.GUI_DOWNLOAD_FILTER_DIVIDER_LOCATION);
    public static MLConfigs DOWNLOAD_GUI_FILTER_DIVIDER_ON = addNewKey("Download-Gui-Filter-Divider-On", Boolean.TRUE.toString());
    public static MLConfigs DOWNLOAD_GUI_DIVIDER = addNewKey("Download-Gui-Divider", Const.GUI_DOWNLOAD_DIVIDER_LOCATION);
    public static MLConfigs DOWNLOAD_GUI_DIVIDER_ON = addNewKey("Download-Gui-Divider-On", Boolean.TRUE.toString());
    public static MLConfigs DOWNLOAD_GUI_TABLE_WIDTH = addNewKey("Download-Gui-Table-Width");
    public static MLConfigs DOWNLOAD_GUI_TABLE_SORT = addNewKey("Download-Gui-Table-Sort");
    public static MLConfigs DOWNLOAD_GUI_TABLE_UPDOWN = addNewKey("Download-Gui-Table-UpDown");
    public static MLConfigs DOWNLOAD_GUI_TABLE_VIS = addNewKey("Download-Gui-Table-Vis");
    public static MLConfigs DOWNLOAD_GUI_TABLE_ORDER = addNewKey("Download-Gui-Table-Order");
    public static MLConfigs DOWNLOAD_NOTIFICATION = addNewKey("Download-Notification-anzeigen", Boolean.TRUE.toString());


    // Meldungen
    public static MLConfigs MSG_VISIBLE = addNewKey("Meldungen-anzeigen", Boolean.FALSE.toString());
    public static MLConfigs MSG_PANEL_LOGS_DIVIDER = addNewKey("Meldungen-Panel-Logs-Divider", Const.GUI_MSG_LOG_DIVIDER_LOCATION);
    public static MLConfigs MSG_PANEL_DIVIDER = addNewKey("Meldungen-Panel-Divider", Const.GUI_MSG_DIVIDER_LOCATION);


    // ConfigDialog
    public static MLConfigs CONFIG_DIALOG_SIZE = addNewKey("Config-Dialog-Groesse");
    public static MLConfigs CONFIG_DIALOG_ACCORDION = addNewKey("Config_Dialog-accordion", Boolean.TRUE.toString());
    public static MLConfigs CONFIG_DIALOG_SET_DIVIDER = addNewKey("Config-Dialog-set-divider", Const.CONFIG_DIALOG_SET_DIVIDER);
    public static MLConfigs CONFIG_DIALOG_IMPORT_SET_GROESSE = addNewKey("Config-Dialog-import-set-Groesse", "800:600");

    // Farben
    public static MLConfigs FARBE__FILM_LIVESTREAM = addNewKey("FARBE_FILM_LIVESTREAM");
    public static MLConfigs FARBE__FILM_HISTORY = addNewKey("FARBE_FILM_HISTORY");
    public static MLConfigs FARBE__FILM_NEU = addNewKey("FARBE_FILM_NEU");
    public static MLConfigs FARBE__FILM_GEOBLOCK_BACKGROUND = addNewKey("FARBE_FILM_GEOBLOCK_BACKGROUND");
    public static MLConfigs FARBE__FILM_GEOBLOCK_BACKGROUND_SEL = addNewKey("FARBE_FILM_GEOBLOCK_BACKGROUND_SEL");
    public static MLConfigs FARBE__DOWNLOAD_IST_ABO = addNewKey("FARBE_DOWNLOAD_IST_ABO");
    public static MLConfigs FARBE__DOWNLOAD_IST_DIREKTER_DOWNLOAD = addNewKey("FARBE_DOWNLOAD_IST_DIREKTER_DOWNLOAD");
    public static MLConfigs FARBE__DOWNLOAD_ANSEHEN = addNewKey("FARBE_DOWNLOAD_ANSEHEN");
    public static MLConfigs FARBE__DOWNLOAD_WAIT = addNewKey("FARBE_DOWNLOAD_WAIT");
    public static MLConfigs FARBE__DOWNLOAD_WAIT_SEL = addNewKey("FARBE_DOWNLOAD_WAIT_SEL");
    public static MLConfigs FARBE__DOWNLOAD_RUN = addNewKey("FARBE_DOWNLOAD_RUN");
    public static MLConfigs FARBE__DOWNLOAD_RUN_SEL = addNewKey("FARBE_DOWNLOAD_RUN_SEL");
    public static MLConfigs FARBE__DOWNLOAD_FERTIG = addNewKey("FARBE_DOWNLOAD_FERTIG");
    public static MLConfigs FARBE__DOWNLOAD_FERTIG_SEL = addNewKey("FARBE_DOWNLOAD_FERTIG_SEL");
    public static MLConfigs FARBE__DOWNLOAD_FEHLER = addNewKey("FARBE_DOWNLOAD_FEHLER");
    public static MLConfigs FARBE__DOWNLOAD_FEHLER_SEL = addNewKey("FARBE_DOWNLOAD_FEHLER_SEL");
    public static MLConfigs FARBE__ABO_FEHLER = addNewKey("FARBE_ABO_FEHLER");
    public static MLConfigs FARBE__ABO_FEHLER_SEL = addNewKey("FARBE_ABO_FEHLER_SEL");
    public static MLConfigs FARBE__ABO_AUSGESCHALTET = addNewKey("FARBE_ABO_AUSGESCHALTET");
    public static MLConfigs FARBE__ABO_AUSGESCHALTET_SEL = addNewKey("FARBE_ABO_AUSGESCHALTET_SEL");
    public static MLConfigs FARBE__FILTER_REGEX = addNewKey("FARBE_FILTER_REGEX");
    public static MLConfigs FARBE__FILTER_REGEX_FEHLER = addNewKey("FARBE_FILTER_REGEX_FEHLER");
    public static MLConfigs FARBE__BUTTON_SET_ABSPIELEN = addNewKey("FARBE_BUTTON_SET_ABSPIELEN");
    public static MLConfigs FARBE__FILMLISTE_LADEN_AKTIV = addNewKey("FARBE_FILMLISTE_LADEN_AKTIV");
    public static MLConfigs FARBE__DOWNLOAD_DATEINAME_EXISTIERT = addNewKey("FARBE_DOWNLOAD_DATEINAME_EXISTIERT");
    public static MLConfigs FARBE__DOWNLOAD_DATEINAME_NEU = addNewKey("FARBE_DOWNLOAD_DATEINAME_NEU");
    public static MLConfigs FARBE__DOWNLOAD_DATEINAME_ALT = addNewKey("FARBE_DOWNLOAD_DATEINAME_ALT");


    public static void loadSystemParameter() {
        Log.sysLog("");
        Log.sysLog("=======================================");
        Log.sysLog("Systemparameter");
        Log.sysLog("-----------------");
        Log.sysLog("=======================================");
        Log.sysLog("");
    }

}
