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


package de.p2tools.mLib.configFile.config;

import de.p2tools.mLib.configFile.ConfigsData;
import de.p2tools.mLib.configFile.ConfigsList;
import javafx.beans.property.IntegerProperty;

public class ConfigList extends Config {

    private int initValue;
    private ConfigsList<? extends ConfigsData> actValue;

    public ConfigList(String key, ConfigsList<? extends ConfigsData> actValue) {
        super(key);
        this.initValue = initValue;
        this.actValue = actValue;
    }

    public Integer getInitValue() {
        return initValue;
    }

    public ConfigsList<? extends ConfigsData> getActValue() {
        return actValue;
    }

    public String getActValueToString() {
        return String.valueOf(getActValue());
    }

    public IntegerProperty getActValueProperty() {
        return null;
    }
}