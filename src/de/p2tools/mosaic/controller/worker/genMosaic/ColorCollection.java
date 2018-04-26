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

package de.p2tools.mosaic.controller.worker.genMosaic;

import de.p2tools.mosaic.controller.data.thumb.Thumb;
import de.p2tools.mosaic.controller.data.thumb.ThumbCollection;
import de.p2tools.p2Lib.tools.log.PLog;

import java.awt.*;

public class ColorCollection {

    private boolean change = false;

    public final int COLORS = 256;
    public final int COLOR_MAX = COLORS - 1;

    public boolean[][][] allColors = new boolean[COLORS][COLORS][COLORS];
    public int[][][] allColorsCount = new int[COLORS][COLORS][COLORS];

    private final ThumbCollection thumbCollection;

    public ColorCollection(ThumbCollection thumbCollection) {
        this.thumbCollection = thumbCollection;

        for (int i = 0; i < COLOR_MAX; ++i) {
            for (int k = 0; k < COLOR_MAX; ++k) {
                for (int l = 0; l < COLOR_MAX; ++l) {

                    allColors[i][k][l] = false;
                    allColorsCount[i][k][l] = 0;

                }
            }
        }

        thumbCollection.getThumbList().stream().forEach(thumb -> addColor(thumb));
    }

    /**
     * @param c
     * @param anz
     * @return
     */
    public Thumb getThumb(Color c, int anz) {
        Thumb thumb;
        int mod = 4;
        int sprung = 0;
        int max = 10;

        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        int rMin = r, rMax = r, gMin = g, gMax = g, bMin = b, bMax = b;

        while (rMin > 0 || gMin > 0 || bMin > 0 ||
                rMax < COLOR_MAX || gMax < COLOR_MAX || bMax < COLOR_MAX) {

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

            if (rMax > COLOR_MAX) {
                rMax = COLOR_MAX;
            }
            if (gMax > COLOR_MAX) {
                gMax = COLOR_MAX;
            }
            if (bMax > COLOR_MAX) {
                bMax = COLOR_MAX;
            }

            mainloop:
//            if (change) {
            for (int i = rMin; i <= rMax; ++i) {
                for (int k = gMin; k <= gMax; ++k) {
                    for (int l = bMin; l <= bMax; ++l) {
                        if (allColors[i][k][l] == true) {
                            ++allColorsCount[i][k][l];

                            if (allColorsCount[i][k][l] % mod == 0) {
                                // dann suchen wir einen ähnlichen
//                                continue;
                                break mainloop;
                            }

                            thumb = thumbCollection.getThumbList().getThumb(i, k, l, anz);
                            if (thumb != null) {
                                return thumb;
                            } else {
                                allColors[i][k][l] = false;
                            }
                        }
                    }
                }
            }
//            } else {
//            for (int i = rMax; i >= rMin; --i) {
//                for (int k = gMax; k >= gMin; --k) {
//                    for (int l = bMax; l >= bMin; --l) {
//                        if (allColors[i][k][l] == true) {
//                            ++allColorsCount[i][k][l];
//
//                            if (allColorsCount[i][k][l] % mod == 0) {
//                                // dann suchen wir einen ähnlichen
////                                continue;
//                                break mainloop;
//                            }
//
//                            thumb = thumbCollection.getThumbList().getThumb(i, k, l, anz);
//                            if (thumb != null) {
//                                return thumb;
//                            } else {
//                                allColors[i][k][l] = false;
//                            }
//                        }
//                    }
//                }
//            }
//            }

            change = !change;

//            --mod;
//            if (mod < 2) {
//                mod = 2;
//            }

            sprung += 2;
            if (sprung > max) {
                sprung = max;
            }

            --anz;
            if (anz < 0) {
                anz = 0;
            }

        }
        PLog.errorLog(987120365, "ColorCollection.getThumb - keine Farbe!!");
        return null;
    }

    private void addColor(Thumb thumb) {
        int r, g, b;
        r = thumb.getRed();
        g = thumb.getGreen();
        b = thumb.getBlue();
        allColors[r][g][b] = true;
    }

}
