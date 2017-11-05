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

import java.awt.image.Raster;
import java.io.File;
import mosaik.Funktionen;
import mosaik.daten.Daten;
import mosaik.daten.DatenFarbe;

public class GetColor_ {

    /**
     * 
     * @param daten
     * @param img
     */
    public static void getColor(Daten daten, File img) {
        Raster rast = Funktionen.getRenderedImage( img).getData();
        long r = 0, g = 0, b = 0;
        long count = 0;
        if (rast != null) {
            for (int x = rast.getMinX(); x < (rast.getMinX() + rast.getWidth()); x++) {
                for (int y = rast.getMinY(); y < (rast.getMinY() + rast.getHeight()); y++) {
                    r += rast.getSample(x, y, 0);
                    g += rast.getSample(x, y, 1);
                    b += rast.getSample(x, y, 2);
                    ++count;
                }
            }
            DatenFarbe farbe = new DatenFarbe(img.getAbsolutePath(),
                                              Long.toString(r / count),
                                              Long.toString(g / count),
                                              Long.toString(b / count));
            daten.listeFarben.add(farbe);
        }
    }

}
