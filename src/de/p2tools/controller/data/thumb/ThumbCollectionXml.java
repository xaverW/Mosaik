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

public class ThumbCollectionXml extends Data<ThumbCollection> {

    public static final int NR = 0;
    public static final int NAME = 1;
    public static final int RESOLUTION = 2;
    public static final int SQUARE = 3;
    public static final int RECURSIV = 4;

    public static final int FOTO_FORMAT = 5;
    public static final int DIR_THUMB = 6;
    public static final int DIR_FOTO_SRC = 7;

    public static final String[] XML_NAMES = {"Nr",
            "name",
            "square",
            "recursiv",
            "resolution",
            "foto-format",
            "dir-thumb",
            "dir-foto-src"};
    public static final String TAG = "ThumbCollection";
    public static int MAX_ELEM = XML_NAMES.length;

    public ThumbCollectionXml() {
        arr = makeArr(MAX_ELEM);
    }

}
