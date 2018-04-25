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

package de.p2tools.mosaic.controller.data.thumb;

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.ConfigInt;
import de.p2tools.p2Lib.configFile.config.ConfigString;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;


public class ThumbProps extends PDataSample<Thumb> {

    public static final String TAG = "Thumb";
    private int anz = 0;
    private int nr = 0;
    private int red = 0;
    private int green = 0;
    private int blue = 0;
    private String fileName = "";


    public String getTag() {
        return TAG;
    }

    public ArrayList<Config> getConfigsArr() {
        return new ArrayList<>(Arrays.asList(
                new ConfigInt("red", 0, red) {
                    public void setActValue(String act) {
                        try {
                            red = Integer.valueOf(act);
                        } catch (Exception ex) {
                            red = 0;
                        }
                    }
                },
                new ConfigInt("green", 0, green) {
                    public void setActValue(String act) {
                        try {
                            green = Integer.valueOf(act);
                        } catch (Exception ex) {
                            green = 0;
                        }
                    }
                },
                new ConfigInt("blue", 0, blue) {
                    public void setActValue(String act) {
                        try {
                            blue = Integer.valueOf(act);
                        } catch (Exception ex) {
                            blue = 0;
                        }
                    }
                },
                new ConfigString("filename", "", fileName) {
                    public void setActValue(String act) {
                        fileName = act;
                    }
                })
        );
    }

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
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        if (!checkC(red)) {
            this.red = 0;
        } else {
            this.red = red;
        }
    }


    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        if (!checkC(green)) {
            this.green = 0;
        } else {
            this.green = green;
        }
    }

    public int getBlue() {
        return blue;
    }


    public void setBlue(int blue) {
        if (!checkC(blue)) {
            this.blue = 0;
        } else {
            this.blue = blue;
        }
    }

    public String getFileName() {
        return fileName;
    }


    public void setFileName(String fileName) {
        this.fileName = fileName;
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
