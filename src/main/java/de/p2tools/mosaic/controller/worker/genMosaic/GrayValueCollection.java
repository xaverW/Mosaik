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

public class GrayValueCollection extends ColorCollection {

    private boolean change = false;

    public final int COLORS = 256;
    public final int COLOR_MAX = COLORS - 1;

    public boolean[] allColors = new boolean[COLORS];
    public int[] allColorsCount = new int[COLORS];

    private final ThumbCollection thumbCollection;

    public GrayValueCollection(ThumbCollection thumbCollection) {
        this.thumbCollection = thumbCollection;

        for (int i = 0; i < COLOR_MAX; ++i) {

            allColors[i] = false;
            allColorsCount[i] = 0;

        }

        thumbCollection.getThumbList().stream().forEach(thumb -> addColor(thumb));
    }

    /**
     * @param c
     * @param anz
     * @return
     */
    @Override
    public Thumb getThumb(Color c, int anz) {
        Thumb thumb;
        int mod = 4;
        int sprung = 0;
        int max = 10;

        long avg = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
        int avgMin = (int) avg, avgMax = (int) avg;

        while (avgMin > 0 || avgMax < COLOR_MAX) {

            avgMin -= sprung;
            avgMax += sprung;
            if (avgMin < 0) {
                avgMin = 0;
            }
            if (avgMax > COLOR_MAX) {
                avgMax = COLOR_MAX;
            }


            mainloop:

//            if (change) {
            for (int i = avgMin; i <= avgMax; ++i) {
                if (allColors[i] == true) {
                    ++allColorsCount[i];

                    if (allColorsCount[i] % mod == 0) {
                        // dann suchen wir einen ähnlichen
//                                continue;
                        break mainloop;
                    }

                    thumb = thumbCollection.getThumbList().getThumbWithGrayValue(i, anz);
                    if (thumb != null) {
                        return thumb;
                    } else {
                        allColors[i] = false;
                    }
                }
            }
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
        PLog.errorLog(987120365, "ColorCollection.getThumbWithGrayValue - keine Farbe!!");
        return null;
    }

    private void addColor(Thumb thumb) {
        allColors[thumb.getAvg()] = true;
    }

}
