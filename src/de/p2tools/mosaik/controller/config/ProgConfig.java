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


package de.p2tools.mosaik.controller.config;

import de.p2tools.p2Lib.configFile.config.ConfigDoubleProp;
import de.p2tools.p2Lib.configFile.config.ConfigIntProp;
import de.p2tools.p2Lib.configFile.config.ConfigStringProp;
import de.p2tools.p2Lib.configFile.configList.ConfigList;
import de.p2tools.p2Lib.configFile.configList.ConfigStringList;
import de.p2tools.p2Lib.configFile.pData.PDataProgConfig;
import de.p2tools.p2Lib.image.ImgFile;

public class ProgConfig extends PDataProgConfig {

    public static ConfigStringProp SYSTEM_PROG_OPEN_DIR = addStrProp("system-prog-open-dir", "");
    public static ConfigStringProp SYSTEM_PROG_OPEN_URL = addStrProp("system-prog-open-uri", "");
    public static ConfigStringProp SYSTEM_PROG_PLAY_FILE = addStrProp("system-prog-open-media", "");

    // Fenstereinstellungen
    public static ConfigStringProp SYSTEM_GROESSE_GUI = addStrProp("system-gui-size", "1000:900");

    // Einstellungen zum Erstellen der Fotolisten
    public static ConfigStringProp FOTO_FORMAT = addStrProp("foto-format", ImgFile.IMAGE_FORMAT_JPG);

    // GuiStart
    public static ConfigIntProp START_GUI_PROJECT_DATA = addIntProp("start-gui-project-data", 0);

    // GuiThumb
    public static ConfigDoubleProp THUMB_GUI_DIVIDER = addDoubleProp("thumb-gui-divider", ProgConst.GUI_THUMB_DIVIDER_LOCATION);

    // GuiChangeThumb
    public static ConfigStringProp CHANGE_THUMB_GUI_TABLE_WIDTH = addStrProp("change-thumb-gui-table-width");
    public static ConfigStringProp CHANGE_THUMB_GUI_TABLE_SORT = addStrProp("change-thumb-gui-table-sort");
    public static ConfigStringProp CHANGE_THUMB_GUI_TABLE_UPDOWN = addStrProp("change-thumb-gui-table-upDown");
    public static ConfigStringProp CHANGE_THUMB_GUI_TABLE_VIS = addStrProp("change-thumb-gui-table-vis");
    public static ConfigStringProp CHANGE_THUMB_GUI_TABLE_ORDER = addStrProp("change-thumb-gui-table-order");

    // ConfigDialog
    public static ConfigStringProp DIALOG_ADD_MOSAIK = addStrProp("dialog-add-mosaik");

    // Programmpfade
    public static ConfigStringProp CONFIG_DIR_SRC_PHOTO_PATH_SEL = addStrProp("config-dir-src-photo-path-sel");
    public static ConfigStringList CONFIG_DIR_SRC_PHOTO_PATH_LIST = addListProp("config-dir-src-photo-path-list");

    public static ConfigList CONFIG_DIR_DEST_PATH_FOTO = addListProp("config-dir-dest-path-foto");

}
