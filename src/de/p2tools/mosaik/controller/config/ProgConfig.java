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


    public static final String TAG = "Thumb";
    private static final ArrayList<Config> arrayList = new ArrayList<>();

    public static final Config SYSTEM_PROG_OPEN_DIR = addStrProp("system-prog-open-dir", "");
    public static final Config SYSTEM_PROG_OPEN_URL = addStrProp("system-prog-open-uri", "");
    public static final Config SYSTEM_PROG_PLAY_FILE = addStrProp("system-prog-open-media", "");

    // Fenstereinstellungen
    public static final Config SYSTEM_GROESSE_GUI = addStrProp("system-size-gui", "1000:900");

    // Einstellungen zum Erstellen der Fotolisten
    public static final Config FOTO_FORMAT = addStrProp("foto-format", ImgFile.IMAGE_FORMAT_JPG);

    // GuiStart
    public static final Config START_GUI_PROJECT_DATA = addIntProp("start-gui-project-data", 0);

    // GuiThumb
    public static final ConfigDoubleProp THUMB_GUI_DIVIDER = addDoubleProp("thumb-gui-divider", ProgConst.GUI_THUMB_DIVIDER_LOCATION);

    // GuiChangeThumb
    public static Config CHANGE_THUMB_GUI_TABLE_WIDTH = addStrProp("change-thumb-gui-table-width");
    public static Config CHANGE_THUMB_GUI_TABLE_SORT = addStrProp("change-thumb-gui-table-sort");
    public static Config CHANGE_THUMB_GUI_TABLE_UPDOWN = addStrProp("change-thumb-gui-table-upDown");
    public static Config CHANGE_THUMB_GUI_TABLE_VIS = addStrProp("change-thumb-gui-table-vis");
    public static Config CHANGE_THUMB_GUI_TABLE_ORDER = addStrProp("change-thumb-gui-table-order");

    // ConfigDialog
    public static Config DIALOG_ADD_MOSAIK = addStrProp("dialog-add-mosaik");

    // Programmpfade
    public static Config CONFIG_DIR_SRC_PHOTO_PATH = addStrProp("config-dir-src-photo-path", "");


    public ProgConfig() {
    }

    public String getTag() {
        return TAG;
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
