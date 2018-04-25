/*
 * MTPlayer Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.mosaic.controller.worker.genMosaic;

import de.p2tools.mosaic.controller.RunEvent;
import de.p2tools.mosaic.controller.RunListener;
import de.p2tools.mosaic.controller.config.ProgData;
import de.p2tools.p2Lib.image.ImgFile;
import de.p2tools.p2Lib.image.ImgTools;

import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CreateMosaicFromSrcImage {
    private EventListenerList listeners;
    private int maxLines = 0;

    private boolean stopAll = false;
    private int progressLines = 0;

    public void create(EventListenerList listeners, ArrayList<GenImgData> genImgDataArrayList) {

        this.listeners = listeners;
        this.maxLines = genImgDataArrayList.size();

        if (ProgData.saveMem) {
            genImgDataArrayList.stream().forEach(genImgData -> genPixel(genImgData));
        } else {
            genImgDataArrayList.parallelStream().forEach(genImgData -> genPixel(genImgData));
        }
    }

    private void notifyEvent(int max, int progress, String text) {
        RunEvent event;
        event = new RunEvent(this, progress, max, text);
        for (RunListener l : listeners.getListeners(RunListener.class)) {
            l.notify(event);
        }
    }


    public void setStop() {
        stopAll = true;
    }

    private void genPixel(GenImgData genImgData) {
        BufferedImage buffImg;

        if (stopAll) {
            return;
        }

        ++progressLines;
        notifyEvent(maxLines, progressLines, "Zeile " + progressLines + " von " + maxLines +
                (maxLines == 0 ? "" : " [" + 100 * progressLines / maxLines + " Prozent]"));

        for (int xx = 0; xx < genImgData.numThumbsWidth && !stopAll; ++xx) {
            Color c = ImgTools.getColor(genImgData.srcImg.getSubimage(xx * genImgData.numPixelProThumb,
                    genImgData.yy * genImgData.numPixelProThumb, genImgData.numPixelProThumb, genImgData.numPixelProThumb));

            buffImg = getBufferedImg(genImgData.srcImgSmall, c);
            buffImg = ImgTools.scaleBufferedImage(buffImg, genImgData.sizeThumb, genImgData.sizeThumb);

            if (genImgData.addBorder) {
                // border
                genImgData.imgOut.getRaster().setRect(xx * genImgData.sizeThumb + (1 + xx) * genImgData.borderSize,
                        genImgData.yy * genImgData.sizeThumb + (1 + genImgData.yy) * genImgData.borderSize,
                        buffImg.getData());

            } else {
                // no border
                genImgData.imgOut.getRaster().setRect(xx * genImgData.sizeThumb,
                        genImgData.yy * genImgData.sizeThumb,
                        buffImg.getData());
            }
        }
    }

    private BufferedImage getBufferedImg(BufferedImage srcImg, Color c) {
        BufferedImage img = ImgFile.cloneImage(srcImg);

        int width = img.getWidth();
        int height = img.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x, y);

                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                //calculate average
                float avg = 1.0f * (r + g + b) / (3 * 255);
                p = (a << 24) |
                        ((int) (avg * c.getRed()) << 16) |
                        ((int) (avg * c.getGreen()) << 8) |
                        (int) (avg * c.getBlue());

                img.setRGB(x, y, p);
            }
        }
        return img;
    }
}
