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

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.pData.PDataProgConfig;
import de.p2tools.p2Lib.image.ImgFile;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class ProgConfig extends PDataProgConfig {

    private static final ArrayList<Config> arrayList = new ArrayList<>();

    public static StringProperty SYSTEM_PROG_OPEN_DIR = addStr("system-prog-open-dir", "");
    public static StringProperty SYSTEM_PROG_OPEN_URL = addStr("system-prog-open-uri", "");
    public static StringProperty SYSTEM_PROG_PLAY_FILE = addStr("system-prog-open-media", "");
    public static IntegerProperty SYSTEM_INFOS_NR = addInt("system-info-nr", 0);
    public static StringProperty SYSTEM_LOG_DIR = addStr("system-log-dir", "");
    public static BooleanProperty SYSTEM_DARK_THEME = addBool("system-dark-theme", false);
    public static BooleanProperty SYSTEM_THEME_CHANGED = addBool("system-theme-changed");


    // Fenstereinstellungen
    public static StringProperty SYSTEM_GUI_SIZE = addStr("system-gui-size", "1000:900");

    // Einstellungen zum Erstellen der Fotolisten
    public static StringProperty FOTO_FORMAT = addStr("foto-format", ImgFile.IMAGE_FORMAT_JPG);

    // GuiStart
    public static IntegerProperty START_GUI_PROJECT_DATA = addInt("start-gui-project-data", 0);
    public static BooleanProperty START_SHOW_MEM_DATA = addBool("start-show-mem-data", false);

    // GuiThumb
    public static DoubleProperty THUMB_GUI_DIVIDER = addDoubleProp(arrayList, "thumb-gui-divider", ProgConst.GUI_THUMB_DIVIDER_LOCATION);

    // GuiThumb
    public static StringProperty THUMB_GUI_TABLE_WIDTH = addStr("thumb-gui-table-width");
    public static StringProperty THUMB_GUI_TABLE_SORT = addStr("thumb-gui-table-sort");
    public static StringProperty THUMB_GUI_TABLE_UPDOWN = addStr("thumb-gui-table-upDown");
    public static StringProperty THUMB_GUI_TABLE_VIS = addStr("thumb-gui-table-vis");
    public static StringProperty THUMB_GUI_TABLE_ORDER = addStr("thumb-gui-table-order");

    // GuiChangeThumb
    public static StringProperty CHANGE_THUMB_GUI_TABLE_WIDTH = addStr("change-thumb-gui-table-width");
    public static StringProperty CHANGE_THUMB_GUI_TABLE_SORT = addStr("change-thumb-gui-table-sort");
    public static StringProperty CHANGE_THUMB_GUI_TABLE_UPDOWN = addStr("change-thumb-gui-table-upDown");
    public static StringProperty CHANGE_THUMB_GUI_TABLE_VIS = addStr("change-thumb-gui-table-vis");
    public static StringProperty CHANGE_THUMB_GUI_TABLE_ORDER = addStr("change-thumb-gui-table-order");

    // ConfigDialog
    public static StringProperty DIALOG_ADD_MOSAIK = addStr("dialog-add-mosaik");

    // Programmpfade
    public static StringProperty CONFIG_SRC_PHOTO_PATH_SEL = addStr("config-src-photo-path-sel");
    public static ObservableList<String> CONFIG_SRC_PHOTO_PATH_LIST = addListProp(arrayList, "config-src-photo-path-list");

    public static StringProperty CONFIG_ADD_PHOTO_PATH_SEL = addStr("config-add-photo-path-sel");
    public static ObservableList<String> CONFIG_ADD_PHOTO_PATH_LIST = addListProp(arrayList, "config-add-photo-path-list");

    public static StringProperty CONFIG_DEST_PHOTO_PATH_SEL = addStr("config-dest-photo-path-sel");
    public static ObservableList<String> CONFIG_DEST_PHOTO_PATH_LIST = addListProp(arrayList, "config-dest-photo-path-list");

    public static StringProperty CONFIG_DEST_WALLPAPER_PATH_SEL = addStr("config-dest-wallpaper-path-sel");
    public static ObservableList<String> CONFIG_DEST_WALLPAPER_PATH_LIST = addListProp(arrayList, "config-dest-wallpaper-path-list");


    private static ProgConfig instance;

    private ProgConfig() {
        super(arrayList, "ProgConfig");
    }

    public static final ProgConfig getInstance() {
        return instance == null ? instance = new ProgConfig() : instance;
    }


    private static StringProperty addStr(String key) {
        return addStrProp(arrayList, key);
    }

    private static StringProperty addStr(String key, String init) {
        return addStrProp(arrayList, key, init);
    }

    private static IntegerProperty addInt(String key, int init) {
        return addIntProp(arrayList, key, init);
    }

    private static BooleanProperty addBool(String key, boolean init) {
        return addBoolProp(arrayList, key, init);
    }
    private static BooleanProperty addBool(String key) {
        return addBoolProp(arrayList, key, true);
    }

}
