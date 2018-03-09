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

package de.p2tools.mosaik.controller.data.thumb;

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.ConfigIntProp;
import de.p2tools.p2Lib.configFile.config.ConfigStringProp;
import de.p2tools.p2Lib.configFile.pData.PDataVault;
import javafx.beans.property.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;


public class ThumbProps extends PDataVault<Thumb> {

    public static final String TAG = "Thumb";
    private int anz = 0;
    private final IntegerProperty nr = new SimpleIntegerProperty(0);
    private final IntegerProperty red = new SimpleIntegerProperty(0);
    private final IntegerProperty green = new SimpleIntegerProperty(0);
    private final IntegerProperty blue = new SimpleIntegerProperty(0);
    private final StringProperty fileName = new SimpleStringProperty("");


    public String getTag() {
        return TAG;
    }

    public ArrayList<Config> getConfigsArr() {
        return new ArrayList<>(Arrays.asList(
                new ConfigIntProp("nr", 0, nr),
                new ConfigIntProp("red", 0, red),
                new ConfigIntProp("green", 0, green),
                new ConfigIntProp("blue", 0, blue),
                new ConfigStringProp("filename", "", fileName)));
    }

    public final Property[] properties = {nr, red, green, blue, fileName};

    public int getAnz() {
        return anz;
    }

    public void setAnz(int anz) {
        this.anz = anz;
    }

    public void addAnz() {
        this.anz = ++anz;
    }

    public int getNr() {
        return nr.get();
    }

    public void setNr(int nr) {
        this.nr.set(nr);
    }

    public IntegerProperty nrProperty() {
        return nr;
    }

    public int getRed() {
        return red.get();
    }

    public IntegerProperty redProperty() {
        return red;
    }

    public void setRed(int red) {
        if (!checkC(red)) {
            this.red.set(0);
        } else {
            this.red.set(red);
        }
    }


    public int getGreen() {
        return green.get();
    }

    public IntegerProperty greenProperty() {
        return green;
    }

    public void setGreen(int green) {
        if (!checkC(green)) {
            this.green.set(0);
        } else {
            this.green.set(green);
        }
    }

    public int getBlue() {
        return blue.get();
    }

    public IntegerProperty blueProperty() {
        return blue;
    }

    public void setBlue(int blue) {
        if (!checkC(blue)) {
            this.blue.set(0);
        } else {
            this.blue.set(blue);
        }
    }

    public String getFileName() {
        return fileName.get();
    }

    public StringProperty fileNameProperty() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }


    public Color getColor() {
        return Color.rgb(getRed(), getGreen(), getBlue());
    }

    public static boolean checkC(int c) {
        if (c < 0) {
            return false;
        }
        if (c > 255) {
            return false;
        }
        return true;
    }

    public static boolean checkC(long c) {
        if (c < 0) {
            return false;
        }
        if (c > 255) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Thumb arg0) {
        if (getColor().getBrightness() > arg0.getColor().getBrightness()) {
            return -1;
        } else {
            return 1;
        }
    }

}
