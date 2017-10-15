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

package mosaik;

import java.io.File;

public class BildErstellen {

    public static final int GITTER = 100;
    private File fileSrc,  fileDest;

    public BildErstellen(File ffileSrc, File ffileDest) {
        fileSrc = ffileSrc;
        fileDest = ffileDest;
    }

    private void erstellen() {
//        Raster rast = Funktionen.getRenderedImage(fileSrc).getData();
//        long r = 0, g = 0, b = 0;
//        long count = 0;
//        if (rast == null) {
//            new Fehler().fehlermeldung("BildErstellen - erstellen");
//            try {
//                int gx, gy;
//                int x = rast.getMinX(), y = rast.getMinY();
//                for (gx = 0; gx < rast.getWidth() / GITTER; ++gx) {
//                    for (gy = 0; gy < rast.getHeight() / GITTER; ++gy) {
//                        //Feld untersuchen
//                        for (x = rast.getMinX() + gx * GITTER;
//                            x < rast.getMinX() + (gx + 1) * GITTER && x < (rast.getMinX() + rast.getWidth());
//                            x++) {
//                            for (y = rast.getMinY() + gy * GITTER;
//                                y < rast.getMinY() + (gy + 1) * GITTER && y < (rast.getMinY() + rast.getHeight());
//                                y++) {
//                                r += rast.getSample(x, y, 0);
//                                g += rast.getSample(x, y, 1);
//                                b += rast.getSample(x, y, 2);
//                                ++count;
//                            }
//                        }
//                        //Feld verarbeiten
//                        r /= count;
//                        g /= count;
//                        b /= count;
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

//    private TiledImage erstellen(RenderedImage img) {
//        Raster rast = img.getData();
//        TiledImage returnImage = new TiledImage(img, rast.getWidth(), rast.getHeight());
//        WritableRaster outRast = rast.createCompatibleWritableRaster();
//        try {
//            for (int x = rast.getMinX(); x < (rast.getMinX() + rast.getWidth()); x++) {
//                for (int y = rast.getMinY(); y < (rast.getMinY() + rast.getHeight()); y++) {
//                    outRast.setSample(x, y, 0, rast.getSample(x, y, 0));
//                    outRast.setSample(x, y, 1, rast.getSample(x, y, 0));
//                    outRast.setSample(x, y, 2, rast.getSample(x, y, 0));
//                }
//            }
//        } catch (Exception e) {
//            new Fehler().fehlermeldung(e, "BildErstellen - erstellen");
//        }
//        returnImage.setData(outRast);
//        return returnImage;
//    }

}
