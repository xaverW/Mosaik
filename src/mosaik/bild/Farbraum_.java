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

package mosaik.bild;

import mosaik.daten.Daten;
import mosaik.daten.DatenFarbe_;
import mosaik.daten.Konstanten;

import java.awt.*;
import java.util.Iterator;

public class Farbraum_ {

    public final int FARBEN = 256;
    public boolean[][][] suchraum = new boolean[FARBEN][FARBEN][FARBEN];
    //private
    private Daten daten;
//    int doppelte = 0;

    public Farbraum_(Daten ddaten) {
        daten = ddaten;
        for (int i = 0; i < FARBEN - 1; ++i) {
            for (int k = 0; k < FARBEN - 1; ++k) {
                for (int l = 0; l < FARBEN - 1; ++l) {
                    suchraum[i][k][l] = false;
                }
            }
        }
        Iterator<DatenFarbe_> it = daten.listeFarben.iterator();
        while (it.hasNext()) {
            addFarbe(it.next());
        }
//        JOptionPane.showMessageDialog(null, "Anzahl doppelte Farben: " + doppelte);
    }
    ///////////////////////////////////
    // public
    ///////////////////////////////////

    /**
     * @param c
     * @param anz
     * @return
     */
    public DatenFarbe_ getFarbe(Color c, int anz) {
        DatenFarbe_ farbe;
        int sprung = 0;
        int max = 10;
//        int abs = 0;
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();
        int rMin = r, rMax = r, gMin = g, gMax = g, bMin = b, bMax = b;
        while (rMin > 0 || gMin > 0 || bMin > 0 || rMax < FARBEN - 1 || gMax < FARBEN - 1 || bMax < FARBEN - 1) {
            rMin -= sprung;
            gMin -= sprung;
            bMin -= sprung;
            rMax += sprung;
            gMax += sprung;
            bMax += sprung;
            if (rMin < 0) {
                rMin = 0;
            }
            if (gMin < 0) {
                gMin = 0;
            }
            if (bMin < 0) {
                bMin = 0;
            }
            if (rMax >= FARBEN) {
                rMax = FARBEN - 1;
            }
            if (gMax >= FARBEN) {
                gMax = FARBEN - 1;
            }
            if (bMax >= FARBEN) {
                bMax = FARBEN - 1;
            }
            for (int i = rMin; i <= rMax; ++i) {
                for (int k = gMin; k <= gMax; ++k) {
                    for (int l = bMin; l <= bMax; ++l) {
                        if (suchraum[i][k][l] == true) {
                            farbe = daten.listeFarben.getFarbe(i, k, l, anz);
                            if (farbe != null) {
                                return farbe;
                            } else {
                                suchraum[i][k][l] = false;
                            }
                        }
                    }
                }
            }
//            abs += 1;
            sprung += 2;
            if (sprung > max) {
                sprung = max;
            }
        }
        daten.fehler.fehlermeldung("Farbraum_.getFarbe - keine Farbe!!");
//        new Fehler().fehlermeldung("zu wenig Farben im Archiv!!", false);
        return null;
    }

    /////////////////////////////////
    // private
    /////////////////////////////////
    private void addFarbe(DatenFarbe_ farbe) {
        if (Boolean.parseBoolean(farbe.arr[Konstanten.FARBEN_BENUTZEN_NR])) {


            int r, g, b;
            r = Integer.parseInt(farbe.arr[Konstanten.FARBEN_R_NR]);
            g = Integer.parseInt(farbe.arr[Konstanten.FARBEN_G_NR]);
            b = Integer.parseInt(farbe.arr[Konstanten.FARBEN_B_NR]);
//            if (suchraum[r][g][b]) {
//                doppelte++;
//            } else {
            suchraum[r][g][b] = true;
//            }
        }
    }

}
