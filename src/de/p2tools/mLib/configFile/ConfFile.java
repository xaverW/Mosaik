/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * http://zdfmediathk.sourceforge.net/
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


package de.p2tools.mLib.configFile;

import de.p2tools.mLib.tools.SysMsg;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.util.ArrayList;

public class ConfFile {
    public static final int MAX_COPY_BACKUPFILE = 5; // Maximum number of backup files to be stored.

    private final Path configFile;
    private final ArrayList<ConfigsData> configsList;
    private final ArrayList<ObservableList<? extends ConfigsData>> configsListList;

    private int maxCopyBackupfile = MAX_COPY_BACKUPFILE;


    public ConfFile(Path configFile) {
        this.configFile = configFile;
        this.configsList = new ArrayList<>();
        configsListList = new ArrayList<>();
    }

//    public void addConfigs(String tag, ArrayList<Config> configs) {
//        ConfigsData configsData = new ConfigsData() {
//            @Override
//            public String getTagName() {
//                return tag;
//            }
//
//            @Override
//            public ArrayList<Config> getConfigsArr() {
//                return configs;
//            }
//        };
//        configsList.add(configsData);
//    }

    public int getMaxCopyBackupfile() {
        return maxCopyBackupfile;
    }

    public void setMaxCopyBackupfile(int maxCopyBackupfile) {
        this.maxCopyBackupfile = maxCopyBackupfile;
    }

    public void addConfigs(ConfigsData configsData) {
        configsList.add(configsData);
    }

    public void addConfigs(ObservableList<? extends ConfigsData> observableList) {
        configsListList.add(observableList);
    }

    public boolean writeConfigFile() {
        boolean ret = false;
        new BackupConfigFile(maxCopyBackupfile, configFile).konfigCopy(configFile);

        SaveConfigFile saveConfigFile = new SaveConfigFile(configFile, configsListList, configsList);
        saveConfigFile.write();
        return ret;
    }

    public boolean readConfigFile(ArrayList<ConfigsList> configsListList,
                                  ArrayList<ConfigsData> configsDataArr) {
        if (new LoadConfigFile(configFile, configsListList, configsDataArr).readConfiguration()) {
            SysMsg.sysMsg("Config geladen");
            return true;

        } else if (new BackupConfigFile(maxCopyBackupfile, configFile).loadBackup(configFile, configsListList, configsDataArr)) {
            SysMsg.sysMsg("Config-Backup geladen");
            return true;
        }

        return false;
    }
}
