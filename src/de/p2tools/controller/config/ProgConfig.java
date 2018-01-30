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

import de.p2tools.p2Lib.configFile.ConfigsData;
import de.p2tools.p2Lib.tools.PConfig;
import de.p2tools.p2Lib.tools.PConfigs;

public class ProgConfig extends PConfig {

    public static final String SYSTEM = "system";

    // ============================================
    public static PConfigs SYSTEM_BUILD_NR = addNewKey("BuildNr");
    public static PConfigs SYSTEM_UPDATE_SEARCH = addNewKey("Update-Suchen", Boolean.TRUE.toString());
    public static PConfigs SYSTEM_UPDATE_DATE = addNewKey("Update-Datum");

    // wegen des Problems mit ext. Programmaufrufen und Leerzeichen
    public static PConfigs SYSTEM_PROG_OPEN_DIR = addNewKey("Programm-Ordner-oeffnen");
    public static PConfigs SYSTEM_PROG_OPEN_URL = addNewKey("Programm-Url-oeffnen");
    public static PConfigs SYSTEM_PROG_PLAY_FILE = addNewKey("Programm-zum-Abspielen");

    // Fenstereinstellungen
    public static PConfigs SYSTEM_GROESSE_GUI = addNewKey("Groesse-Gui", "1000:900");
    public static PConfigs SYSTEM_GROESSE_DIALOG_FILMINFO = addNewKey("Groesse-Filminfo", "600:800");
    public static PConfigs SYSTEM_ICON_STANDARD = addNewKey("Icon-Standard", Boolean.TRUE.toString());
    public static PConfigs SYSTEM_ICON_PFAD = addNewKey("Icon-Pfad");


    // Einstellungen zum Erstellen der Fotolisten
    public static PConfigs FOTO_SIZE = addNewKey("foto-size", 600);
    public static PConfigs FOTO_SQUARE = addNewKey("foto-square", Boolean.TRUE.toString());
    public static PConfigs FOTO_FORMAT = addNewKey("foto-format", ProgConst.IMAGE_FORMAT_JPG);

    // GuiStart
    public static PConfigs START_GUI_PROJECT_DATA = addNewKey("start-gui-project-data");

    // GuiThumb
    public static PConfigs THUMB_GUI_DIVIDER = addNewKey("thumb-gui-divider", ProgConst.GUI_THUMB_DIVIDER_LOCATION);
    public static PConfigs THUMB_GUI_TABLE_WIDTH = addNewKey("thumb-gui-table-width");
    public static PConfigs THUMB_GUI_TABLE_SORT = addNewKey("thumb-gui-table-sort");
    public static PConfigs THUMB_GUI_TABLE_UPDOWN = addNewKey("thumb-gui-table-upDown");
    public static PConfigs THUMB_GUI_TABLE_VIS = addNewKey("thumb-gui-table-vis");
    public static PConfigs THUMB_GUI_TABLE_ORDER = addNewKey("thumb-gui-table-order");
    public static PConfigs THUMB_GUI_THUMB_COLLECTION = addNewKey("thumb-gui-thumb-collection");

    // GuiChangeThumb
    public static PConfigs CHANGE_THUMB_GUI_DIVIDER = addNewKey("change-thumb-gui-divider", ProgConst.GUI_THUMB_DIVIDER_LOCATION);
    public static PConfigs CHANGE_THUMB_GUI_TABLE_WIDTH = addNewKey("change-thumb-gui-table-width");
    public static PConfigs CHANGE_THUMB_GUI_TABLE_SORT = addNewKey("change-thumb-gui-table-sort");
    public static PConfigs CHANGE_THUMB_GUI_TABLE_UPDOWN = addNewKey("change-thumb-gui-table-upDown");
    public static PConfigs CHANGE_THUMB_GUI_TABLE_VIS = addNewKey("change-thumb-gui-table-vis");
    public static PConfigs CHANGE_THUMB_GUI_TABLE_ORDER = addNewKey("change-thumb-gui-table-order");


    // Meldungen
    public static PConfigs MSG_VISIBLE = addNewKey("Meldungen-anzeigen", Boolean.FALSE.toString());
    public static PConfigs MSG_PANEL_LOGS_DIVIDER = addNewKey("Meldungen-Panel-Logs-Divider", ProgConst.GUI_MSG_LOG_DIVIDER_LOCATION);
    public static PConfigs MSG_PANEL_DIVIDER = addNewKey("Meldungen-Panel-Divider", ProgConst.GUI_MSG_DIVIDER_LOCATION);


    // ConfigDialog
    public static PConfigs DIALOG_ADD_MOSAIK = addNewKey("dialog-add-mosaik");
    public static PConfigs CONFIG_DIALOG_ACCORDION = addNewKey("Config_Dialog-accordion", Boolean.TRUE.toString());
    public static PConfigs CONFIG_DIALOG_SET_DIVIDER = addNewKey("Config-Dialog-set-divider", ProgConst.CONFIG_DIALOG_SET_DIVIDER);
    public static PConfigs CONFIG_DIALOG_IMPORT_SET_GROESSE = addNewKey("Config-Dialog-import-set-Groesse", "800:600");

    // Programmpfade
    public static PConfigs CONFIG_DIR_MOSAIK_PATH = addNewKey("config-dir-mosaik-path", "");
    public static PConfigs CONFIG_DIR_SRC_PHOTO_PATH = addNewKey("config-dir-src-photo-path", "");

    public static ConfigsData getConfigsData() {
        // sonst werden die Keys nich vorher angelegt :)
        return PConfig.getConfigsData();
    }

}
