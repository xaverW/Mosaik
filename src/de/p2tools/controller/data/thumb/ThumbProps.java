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

package de.p2tools.controller.data.thumb;

import de.p2tools.mLib.configFile.Configs;
import de.p2tools.mLib.configFile.ConfigsIntProp;
import de.p2tools.mLib.configFile.ConfigsStringProp;
import javafx.beans.property.*;
import javafx.scene.paint.Color;


public class ThumbProps extends ThumbXml {

    private int anz = 0;
    private final IntegerProperty nr = new SimpleIntegerProperty(0);
    private final IntegerProperty red = new SimpleIntegerProperty(0);
    private final IntegerProperty green = new SimpleIntegerProperty(0);
    private final IntegerProperty blue = new SimpleIntegerProperty(0);
    private final StringProperty fileName = new SimpleStringProperty("");

    private int sum = 0;
    private Color color = Color.rgb(0, 0, 0);


    public String getTagName() {
        return TAG;
    }

    public Configs[] getConfigsArr() {
        Configs[] arr = new Configs[]{
                new ConfigsIntProp("nr", 0, nr),
                new ConfigsIntProp("red", 0, red),
                new ConfigsIntProp("green", 0, green),
                new ConfigsIntProp("blue", 0, blue),
                new ConfigsStringProp("filename", "", fileName),
        };
        return arr;
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
        this.red.set(red);
        setColor();
    }

    public int getGreen() {
        return green.get();
    }

    public IntegerProperty greenProperty() {
        return green;
    }

    public void setGreen(int green) {
        this.green.set(green);
        setColor();
    }

    public int getBlue() {
        return blue.get();
    }

    public IntegerProperty blueProperty() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue.set(blue);
        setColor();
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


    public int getSum() {
        return sum;
    }

    public void setSum() {
        sum = getGreen() + getBlue() + getRed();
    }

    public Color getColor() {
        return color;
    }

    public void setColor() {
        this.color = Color.rgb(getRed(), getGreen(), getBlue());
    }

    public void setPropsFromXml() {
        setRed(Integer.parseInt(arr[COLOR_RED]));
        setGreen(Integer.parseInt(arr[COLOR_GREEN]));
        setBlue(Integer.parseInt(arr[COLOR_BLUE]));
        setSum();
        setColor();
        setFileName(arr[FILENAME]);
    }


    public void setXmlFromProps() {
        arr[COLOR_RED] = String.valueOf(getRed());
        arr[COLOR_GREEN] = String.valueOf(getGreen());
        arr[COLOR_BLUE] = String.valueOf(getBlue());
        arr[FILENAME] = getFileName();
    }


    @Override
    public int compareTo(Thumb arg0) {
        if (getColor().getBrightness() > arg0.getColor().getBrightness()) {
            return -1;
        } else {
            return 1;
        }
//        if (arg0.getSum() == getSum()) {
//            return 0;
//        } else if (arg0.getSum() < getSum()) {
//            return -1;
//        } else {
//            return 1;
//        }
    }

}
