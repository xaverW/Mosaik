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

/**
 * its a pseudo CONFIG, it contains a CONFIGDATA, also
 * a array of config
 */
public class ConfigConfigsData extends Config {

    private ConfigsData actValue;

    public ConfigConfigsData(String key, ConfigsData actValue) {
        super(key);
        this.actValue = actValue;
    }

    public ConfigsData getActValue() {
        return actValue;
    }

}
