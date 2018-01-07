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

import de.p2tools.mLib.configFile.ConfigsData;
import de.p2tools.mLib.tools.MLConfig;
import de.p2tools.mLib.tools.MLConfigs;

public class ProgConfig extends MLConfig {

    public static final String SYSTEM = "system";

    // ============================================
    public static MLConfigs SYSTEM_BUILD_NR = addNewKey("BuildNr");
    public static MLConfigs SYSTEM_UPDATE_SEARCH = addNewKey("Update-Suchen", Boolean.TRUE.toString());
    public static MLConfigs SYSTEM_UPDATE_DATE = addNewKey("Update-Datum");

    // wegen des Problems mit ext. Programmaufrufen und Leerzeichen
    public static MLConfigs SYSTEM_PROG_OPEN_DIR = addNewKey("Programm-Ordner-oeffnen");
    public static MLConfigs SYSTEM_PROG_OPEN_URL = addNewKey("Programm-Url-oeffnen");
    public static MLConfigs SYSTEM_PROG_PLAY_FILE = addNewKey("Programm-zum-Abspielen");

    // Fenstereinstellungen
    public static MLConfigs SYSTEM_GROESSE_GUI = addNewKey("Groesse-Gui", "1000:900");
    public static MLConfigs SYSTEM_GROESSE_DIALOG_FILMINFO = addNewKey("Groesse-Filminfo", "600:800");
    public static MLConfigs SYSTEM_ICON_STANDARD = addNewKey("Icon-Standard", Boolean.TRUE.toString());
    public static MLConfigs SYSTEM_ICON_PFAD = addNewKey("Icon-Pfad");


    // Einstellungen zum Erstellen der Fotolisten
    public static MLConfigs FOTO_SIZE = addNewKey("foto-size", 600);
    public static MLConfigs FOTO_SQUARE = addNewKey("foto-square", Boolean.TRUE.toString());
    public static MLConfigs FOTO_FORMAT = addNewKey("foto-format", ProgConst.IMAGE_FORMAT_JPG);

    // GuiStart
    public static MLConfigs START_GUI_PROJECT_DATA = addNewKey("start-gui-project-data");

    // GuiThumb
    public static MLConfigs THUMB_GUI_DIVIDER = addNewKey("thumb-gui-divider", ProgConst.GUI_FILME_DIVIDER_LOCATION);
    public static MLConfigs THUMB_GUI_TABLE_WIDTH = addNewKey("thumb-gui-table-width");
    public static MLConfigs THUMB_GUI_TABLE_SORT = addNewKey("thumb-gui-table-sort");
    public static MLConfigs THUMB_GUI_TABLE_UPDOWN = addNewKey("thumb-gui-table-upDown");
    public static MLConfigs THUMB_GUI_TABLE_VIS = addNewKey("thumb-gui-table-vis");
    public static MLConfigs THUMB_GUI_TABLE_ORDER = addNewKey("thumb-gui-table-order");
    public static MLConfigs THUMB_GUI_THUMB_COLLECTION = addNewKey("thumb-gui-thumb-collection");

    // GuiChangeThumb
    public static MLConfigs CHANGE_THUMB_GUI_DIVIDER = addNewKey("change-thumb-gui-divider", ProgConst.GUI_FILME_DIVIDER_LOCATION);
    public static MLConfigs CHANGE_THUMB_GUI_TABLE_WIDTH = addNewKey("change-thumb-gui-table-width");
    public static MLConfigs CHANGE_THUMB_GUI_TABLE_SORT = addNewKey("change-thumb-gui-table-sort");
    public static MLConfigs CHANGE_THUMB_GUI_TABLE_UPDOWN = addNewKey("change-thumb-gui-table-upDown");
    public static MLConfigs CHANGE_THUMB_GUI_TABLE_VIS = addNewKey("change-thumb-gui-table-vis");
    public static MLConfigs CHANGE_THUMB_GUI_TABLE_ORDER = addNewKey("change-thumb-gui-table-order");


    // Meldungen
    public static MLConfigs MSG_VISIBLE = addNewKey("Meldungen-anzeigen", Boolean.FALSE.toString());
    public static MLConfigs MSG_PANEL_LOGS_DIVIDER = addNewKey("Meldungen-Panel-Logs-Divider", ProgConst.GUI_MSG_LOG_DIVIDER_LOCATION);
    public static MLConfigs MSG_PANEL_DIVIDER = addNewKey("Meldungen-Panel-Divider", ProgConst.GUI_MSG_DIVIDER_LOCATION);


    // ConfigDialog
    public static MLConfigs CONFIG_DIALOG_SIZE = addNewKey("Config-Dialog-Groesse");
    public static MLConfigs CONFIG_DIALOG_ACCORDION = addNewKey("Config_Dialog-accordion", Boolean.TRUE.toString());
    public static MLConfigs CONFIG_DIALOG_SET_DIVIDER = addNewKey("Config-Dialog-set-divider", ProgConst.CONFIG_DIALOG_SET_DIVIDER);
    public static MLConfigs CONFIG_DIALOG_IMPORT_SET_GROESSE = addNewKey("Config-Dialog-import-set-Groesse", "800:600");

    public static ConfigsData getConfigsDate() {
        // sonst werde die Keys nich vorher angelegt :)
        return MLConfig.getConfigsData();
    }

}
