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

package de.p2tools.mosaik.controller.worker.genMosaik;

public class ColorData implements Comparable<ColorData> {

    public String[] arr;
    public int anz = 0;
    public int red = 0;
    public int green = 0;
    public int blue = 0;

    public static final String FARBEN = "Farben";
    public static final int FARBEN_MAX_ELEM = 6;
    public static final String FARBEN_PFAD = "Pfad";
    public static final int FARBEN_PFAD_NR = 0;
    public static final String FARBEN_R = "Rot";
    public static final int FARBEN_R_NR = 1;
    public static final String FARBEN_G = "Grün";
    public static final int FARBEN_G_NR = 2;
    public static final String FARBEN_B = "Blau";
    public static final int FARBEN_B_NR = 3;
    public static final String FARBEN_FARBE = "Farbe";
    public static final int FARBEN_FARBE_NR = 4;
    public static final String FARBEN_BENUTZEN = "Benutzen";
    public static final int FARBEN_BENUTZEN_NR = 5;
    public static final String[] FARBEN_COLUMN_NAMES = {FARBEN_PFAD, FARBEN_R, FARBEN_G, FARBEN_B, FARBEN_FARBE,
            FARBEN_BENUTZEN};

    public ColorData() {
        makeArr();
    }

    public ColorData(String pfad, String r, String g, String b) {
        makeArr();
        arr[FARBEN_PFAD_NR] = pfad;
        arr[FARBEN_R_NR] = r;
        arr[FARBEN_G_NR] = g;
        arr[FARBEN_B_NR] = b;
        red = Integer.parseInt(r);
        green = Integer.parseInt(g);
        blue = Integer.parseInt(b);
    }

    private void makeArr() {
        arr = new String[FARBEN_MAX_ELEM];
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = new String("");
        }
        arr[FARBEN_BENUTZEN_NR] = Boolean.toString(true);
    }

    @Override
    public int compareTo(ColorData arg0) {
        boolean ret = false;
        if (Integer.parseInt(arr[FARBEN_R_NR]) +
                Integer.parseInt(arr[FARBEN_G_NR]) +
                Integer.parseInt(arr[FARBEN_B_NR]) <
                Integer.parseInt(arg0.arr[FARBEN_R_NR]) +
                        Integer.parseInt(arg0.arr[FARBEN_G_NR]) +
                        Integer.parseInt(arg0.arr[FARBEN_B_NR])) {
            ret = true;
        }
        return (ret == true) ? 1 : -1;
    }

}
