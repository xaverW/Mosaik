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


import de.p2tools.controller.data.Data;

public class ThumbXml extends Data<Thumb> {


    public static final int NR = 0;
    public static final int COLOR_RED = 1;
    public static final int COLOR_GREEN = 2;
    public static final int COLOR_BLUE = 3;
    public static final int COLOR_SUM = 4;
    public static final int FILENAME = 5;

    public static final String[] XML_NAMES = {"Nr",
            "red",
            "green",
            "blue",
            "sum",
            "filename"};
    public static final String TAG = "Thumb";
    public static int MAX_ELEM = XML_NAMES.length;

    public ThumbXml() {
        arr = makeArr(MAX_ELEM);
    }

}
