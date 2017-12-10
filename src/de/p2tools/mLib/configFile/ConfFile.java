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

import de.p2tools.mLib.configFile.config.Config;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class ConfFile {
    private final String configFileUrl;
    private final ArrayList<ConfigsData> configsList;
    private final ArrayList<ObservableList<? extends ConfigsData>> configsListList;

    public ConfFile(String configFileUrl) {
        this.configFileUrl = configFileUrl;
        this.configsList = new ArrayList<>();
        configsListList = new ArrayList<>();
    }

    public void addConfigs(String tag, ArrayList<Config> configs) {
        ConfigsData configsData = new ConfigsData() {
            @Override
            public String getTagName() {
                return tag;
            }

            @Override
            public ArrayList<Config> getConfigsArr() {
                return configs;
            }
        };
        configsList.add(configsData);
    }

    public void addConfigs(ConfigsData configsData) {
        configsList.add(configsData);
    }

    public void addConfigs(ObservableList<? extends ConfigsData> observableList) {
        configsListList.add(observableList);
    }

    public boolean writeConfigFile() {
        boolean ret = false;
        SaveConfigFile saveConfigFile = new SaveConfigFile(configFileUrl, configsListList, configsList);
        saveConfigFile.write();
        return ret;
    }

    public boolean readConfigFile(ArrayList<ConfigsList> configsListList,
                                  ArrayList<ConfigsData> configsDataArr) {
        return new LoadConfigFile(configFileUrl, configsListList, configsDataArr).readConfiguration();
    }
}
