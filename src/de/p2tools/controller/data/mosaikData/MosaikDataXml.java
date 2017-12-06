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


package de.p2tools.controller.data.mosaikData;

import de.p2tools.controller.data.Data;

public class MosaikDataXml extends Data<MosaikData> {

    public static final int FORMAT = 0;
    public static final int FOTO_SRC = 1;
    public static final int FOTO_DEST = 2;
    public static final int THUMB_SIZE = 3;
    public static final int NUMBER_THUMBS_W = 4;
    public static final int THUMB_COUNT = 5;
    public static final int THUMB_COLLECTION_ID = 6;

    public static final String[] XML_NAMES = {"Format",
            "foto-src",
            "foto-dest",
            "thumb-size",
            "dest-size-w",
            "thumb-count",
            "thumb-collection-id"};
    public static final String TAG = "MosaikData";
    public static int MAX_ELEM = XML_NAMES.length;

    public MosaikDataXml() {
        arr = makeArr(MAX_ELEM);
    }

}
