/*
 *    Copyright (C) 2008
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.p2tools.controller.genMosaik;

import mosaik.daten.Konstanten;

public class DataColor implements Comparable<DataColor> {

    public String[] arr;
    public int anz = 0;
    public int red = 0;
    public int green = 0;
    public int blue = 0;

    public DataColor() {
        makeArr();
    }

    public DataColor(String pfad, String r, String g, String b) {
        makeArr();
        arr[Konstanten.FARBEN_PFAD_NR] = pfad;
        arr[Konstanten.FARBEN_R_NR] = r;
        arr[Konstanten.FARBEN_G_NR] = g;
        arr[Konstanten.FARBEN_B_NR] = b;
        red = Integer.parseInt(r);
        green = Integer.parseInt(g);
        blue = Integer.parseInt(b);
    }

    private void makeArr() {
        arr = new String[Konstanten.FARBEN_MAX_ELEM];
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = new String("");
        }
        arr[Konstanten.FARBEN_BENUTZEN_NR] = Boolean.toString(true);
    }

    @Override
    public int compareTo(DataColor arg0) {
        boolean ret = false;
        if (Integer.parseInt(arr[Konstanten.FARBEN_R_NR]) +
                Integer.parseInt(arr[Konstanten.FARBEN_G_NR]) +
                Integer.parseInt(arr[Konstanten.FARBEN_B_NR]) <
                Integer.parseInt(arg0.arr[Konstanten.FARBEN_R_NR]) +
                        Integer.parseInt(arg0.arr[Konstanten.FARBEN_G_NR]) +
                        Integer.parseInt(arg0.arr[Konstanten.FARBEN_B_NR])) {
            ret = true;
        }
        return (ret == true) ? 1 : -1;
    }

}
