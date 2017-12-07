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


package de.p2tools.mLib.configFile;

import javafx.beans.property.ObjectProperty;

public abstract class Configs {

    private final String key;
    private final Object iniValue;
    private Object actValue;
    private ObjectProperty actValueProperty;

    public Configs() {
        this.key = "";
        iniValue = "";
        actValue = "";
    }

    public Configs(String key) {
        this.key = key;
        iniValue = "";
        actValue = "";
    }

    public Configs(String key, Object initValue, Object actValue) {
        this.key = key;
        this.iniValue = initValue;
        this.actValue = actValue;
    }

    public String getKey() {
        return key;
    }

    public Object getActValue() {
        return actValue;
    }

    public String getActValueToString() {
        return actValue.toString();
    }

    public Object getActValueProperty() {
        return actValueProperty;
    }

    public Object getInitValue() {
        return iniValue;
    }

    public void setActValue(String act) {
        actValue = act;
    }
}


