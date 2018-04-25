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

import de.p2tools.p2Lib.configFile.pData.PDataProgConfig;
import de.p2tools.p2Lib.image.ImgFile;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class ProgConfig extends PDataProgConfig {

    public static StringProperty SYSTEM_PROG_OPEN_DIR = addStrProp("system-prog-open-dir", "");

    public static StringProperty SYSTEM_PROG_OPEN_URL = addStrProp("system-prog-open-uri", "");
    public static StringProperty SYSTEM_PROG_PLAY_FILE = addStrProp("system-prog-open-media", "");
    public static IntegerProperty SYSTEM_INFOS_NR = addIntProp("system-info-nr", 0);
    public static StringProperty SYSTEM_LOG_DIR = addStrProp("system-log-dir", "");

    // Fenstereinstellungen
    public static StringProperty SYSTEM_GROESSE_GUI = addStrProp("system-gui-size", "1000:900");

    // Einstellungen zum Erstellen der Fotolisten
    public static StringProperty FOTO_FORMAT = addStrProp("foto-format", ImgFile.IMAGE_FORMAT_JPG);

    // GuiStart
    public static IntegerProperty START_GUI_PROJECT_DATA = addIntProp("start-gui-project-data", 0);
    public static BooleanProperty START_SHOW_MEM_DATA = addBoolProp("start-show-mem-data", false);

    // GuiThumb
    public static DoubleProperty THUMB_GUI_DIVIDER = addDoubleProp("thumb-gui-divider", ProgConst.GUI_THUMB_DIVIDER_LOCATION);

    // GuiThumb
    public static StringProperty THUMB_GUI_TABLE_WIDTH = addStrProp("thumb-gui-table-width");
    public static StringProperty THUMB_GUI_TABLE_SORT = addStrProp("thumb-gui-table-sort");
    public static StringProperty THUMB_GUI_TABLE_UPDOWN = addStrProp("thumb-gui-table-upDown");
    public static StringProperty THUMB_GUI_TABLE_VIS = addStrProp("thumb-gui-table-vis");
    public static StringProperty THUMB_GUI_TABLE_ORDER = addStrProp("thumb-gui-table-order");

    // GuiChangeThumb
    public static StringProperty CHANGE_THUMB_GUI_TABLE_WIDTH = addStrProp("change-thumb-gui-table-width");
    public static StringProperty CHANGE_THUMB_GUI_TABLE_SORT = addStrProp("change-thumb-gui-table-sort");
    public static StringProperty CHANGE_THUMB_GUI_TABLE_UPDOWN = addStrProp("change-thumb-gui-table-upDown");
    public static StringProperty CHANGE_THUMB_GUI_TABLE_VIS = addStrProp("change-thumb-gui-table-vis");
    public static StringProperty CHANGE_THUMB_GUI_TABLE_ORDER = addStrProp("change-thumb-gui-table-order");

    // ConfigDialog
    public static StringProperty DIALOG_ADD_MOSAIK = addStrProp("dialog-add-mosaik");

    // Programmpfade
    public static StringProperty CONFIG_SRC_PHOTO_PATH_SEL = addStrProp("config-src-photo-path-sel");
    public static ObservableList<String> CONFIG_SRC_PHOTO_PATH_LIST = addListProp("config-src-photo-path-list");

    public static StringProperty CONFIG_ADD_PHOTO_PATH_SEL = addStrProp("config-add-photo-path-sel");
    public static ObservableList<String> CONFIG_ADD_PHOTO_PATH_LIST = addListProp("config-add-photo-path-list");

    public static StringProperty CONFIG_DEST_PHOTO_PATH_SEL = addStrProp("config-dest-photo-path-sel");
    public static ObservableList<String> CONFIG_DEST_PHOTO_PATH_LIST = addListProp("config-dest-photo-path-list");

    public static StringProperty CONFIG_DEST_WALLPAPER_PATH_SEL = addStrProp("config-dest-wallpaper-path-sel");
    public static ObservableList<String> CONFIG_DEST_WALLPAPER_PATH_LIST = addListProp("config-dest-wallpaper-path-list");


    private static ProgConfig instance;

    public static final ProgConfig getInstance() {
        return instance == null ? instance = new ProgConfig() : instance;
    }
}
