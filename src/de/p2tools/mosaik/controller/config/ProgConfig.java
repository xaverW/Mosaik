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

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.ConfigDoubleProp;
import de.p2tools.p2Lib.configFile.config.ConfigIntProp;
import de.p2tools.p2Lib.configFile.config.ConfigStringProp;
import de.p2tools.p2Lib.configFile.pData.PDataVault;
import de.p2tools.p2Lib.image.ImgFile;
import javafx.beans.property.*;

import java.util.ArrayList;

public class ProgConfig extends PDataVault<ProgConfig> {

    public static final String TAG = "ProgConf";

    public static ConfigStringProp SYSTEM_PROG_OPEN_DIR;
    public static ConfigStringProp SYSTEM_PROG_OPEN_URL;
    public static ConfigStringProp SYSTEM_PROG_PLAY_FILE;

    // Fenstereinstellungen
    public static ConfigStringProp SYSTEM_GROESSE_GUI;

    // Einstellungen zum Erstellen der Fotolisten
    public static ConfigStringProp FOTO_FORMAT;

    // GuiStart
    public static ConfigIntProp START_GUI_PROJECT_DATA;

    // GuiThumb
    public static ConfigDoubleProp THUMB_GUI_DIVIDER;

    // GuiChangeThumb
    public static ConfigStringProp CHANGE_THUMB_GUI_TABLE_WIDTH;
    public static ConfigStringProp CHANGE_THUMB_GUI_TABLE_SORT;
    public static ConfigStringProp CHANGE_THUMB_GUI_TABLE_UPDOWN;
    public static ConfigStringProp CHANGE_THUMB_GUI_TABLE_VIS;
    public static ConfigStringProp CHANGE_THUMB_GUI_TABLE_ORDER;

    // ConfigDialog
    public static ConfigStringProp DIALOG_ADD_MOSAIK;

    // Programmpfade
    public static ConfigStringProp CONFIG_DIR_SRC_PHOTO_PATH;

    private static final ArrayList<Config> arrayList = new ArrayList<>();

    public ProgConfig() {
        SYSTEM_PROG_OPEN_DIR = addStrProp("system-prog-open-dir", "");
        SYSTEM_PROG_OPEN_URL = addStrProp("system-prog-open-uri", "");
        SYSTEM_PROG_PLAY_FILE = addStrProp("system-prog-open-media", "");

        SYSTEM_GROESSE_GUI = addStrProp("system-size-gui", "1000:900");

        FOTO_FORMAT = addStrProp("foto-format", ImgFile.IMAGE_FORMAT_JPG);

        START_GUI_PROJECT_DATA = addIntProp("start-gui-project-data", 0);

        THUMB_GUI_DIVIDER = addDoubleProp("thumb-gui-divider", ProgConst.GUI_THUMB_DIVIDER_LOCATION);

        CHANGE_THUMB_GUI_TABLE_WIDTH = addStrProp("change-thumb-gui-table-width");
        CHANGE_THUMB_GUI_TABLE_SORT = addStrProp("change-thumb-gui-table-sort");
        CHANGE_THUMB_GUI_TABLE_UPDOWN = addStrProp("change-thumb-gui-table-upDown");
        CHANGE_THUMB_GUI_TABLE_VIS = addStrProp("change-thumb-gui-table-vis");
        CHANGE_THUMB_GUI_TABLE_ORDER = addStrProp("change-thumb-gui-table-order");

        DIALOG_ADD_MOSAIK = addStrProp("dialog-add-mosaik");

        CONFIG_DIR_SRC_PHOTO_PATH = addStrProp("config-dir-src-photo-path", "");
    }

    public String getTag() {
        return TAG;
    }

    public String getComment() {
        return "Programmeinstellungen";
    }

    public ArrayList<Config> getConfigsArr() {
        return arrayList;
    }

    public static synchronized ConfigStringProp addStrProp(String key) {
        StringProperty property = new SimpleStringProperty("");
        ConfigStringProp c = new ConfigStringProp(key, "", property);
        arrayList.add(c);
        return c;
    }

    public static synchronized ConfigStringProp addStrProp(String key, String init) {
        StringProperty property = new SimpleStringProperty(init);
        ConfigStringProp c = new ConfigStringProp(key, init, property);
        arrayList.add(c);
        return c;
    }

    public static synchronized ConfigIntProp addIntProp(String key, int init) {
        IntegerProperty property = new SimpleIntegerProperty(init);
        ConfigIntProp c = new ConfigIntProp(key, init, property);
        arrayList.add(c);
        return c;
    }

    public static synchronized ConfigDoubleProp addDoubleProp(String key, double init) {
        DoubleProperty property = new SimpleDoubleProperty(init);
        ConfigDoubleProp c = new ConfigDoubleProp(key, init, property);
        arrayList.add(c);
        return c;
    }

}
