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
import de.p2tools.mosaic.controller.data.thumb.Thumb;
import de.p2tools.p2Lib.image.ImgFile;
import de.p2tools.p2Lib.image.ImgTools;
import de.p2tools.p2Lib.tools.PException;

import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class CreateMosaicFromColoredThumb {
    private EventListenerList listeners;
    private int maxLines = 0;

    private boolean stopAll = false;
    private int progressLines = 0;

    public void create(EventListenerList listeners, ArrayList<CreateMosaicData> createMosaicDataArrayList) {

        this.listeners = listeners;
        this.maxLines = createMosaicDataArrayList.size();

        if (ProgData.saveMem) {
            createMosaicDataArrayList.stream().forEach(createMosaicData -> genPixel(createMosaicData));
        } else {
            createMosaicDataArrayList.parallelStream().forEach(createMosaicData -> genPixel(createMosaicData));
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

    private void genPixel(CreateMosaicData createMosaicData) {
        BufferedImage buffImg;

        if (stopAll) {
            return;
        }

        ++progressLines;
        notifyEvent(maxLines, progressLines, "Zeile " + progressLines + " von " + maxLines +
                (maxLines == 0 ? "" : " [" + 100 * progressLines / maxLines + " Prozent]"));

        for (int xx = 0; xx < createMosaicData.numThumbsWidth && !stopAll; ++xx) {
            Color c = ImgTools.getColor(createMosaicData.srcImg.getSubimage(xx * createMosaicData.numPixelProThumb,
                    createMosaicData.yy * createMosaicData.numPixelProThumb, createMosaicData.numPixelProThumb, createMosaicData.numPixelProThumb));


            int thumbNr = createMosaicData.listForColoredThumbs.get(createMosaicData.yy * createMosaicData.numThumbsWidth + xx);
            Thumb thumb = createMosaicData.thumbCollection.getThumbList().get(thumbNr);
            if (thumb == null) {
                throw new PException("Es sind nicht mehr alle Miniaturbilder vorhanden. " +
                        "Bitte die Liste der Miniaturbilder " +
                        "neu einlesen.");
            }

            thumb.addAnz();
            File file = new File(thumb.getFileName());
            buffImg = ImgFile.getBufferedImage(file);

            if (buffImg == null) {
                throw new PException("Es sind nicht mehr alle Miniaturbilder vorhanden. " +
                        "Bitte die Liste der Miniaturbilder " +
                        "neu einlesen.");
            }


            buffImg = getBufferedImg(buffImg, c);
            buffImg = ImgTools.scaleBufferedImage(buffImg, createMosaicData.sizeThumb, createMosaicData.sizeThumb);

            if (createMosaicData.addBorder) {
                // border
                createMosaicData.imgOut.getRaster().setRect(xx * createMosaicData.sizeThumb + (1 + xx) * createMosaicData.borderSize,
                        createMosaicData.yy * createMosaicData.sizeThumb + (1 + createMosaicData.yy) * createMosaicData.borderSize,
                        buffImg.getData());

            } else {
                // no border
                createMosaicData.imgOut.getRaster().setRect(xx * createMosaicData.sizeThumb,
                        createMosaicData.yy * createMosaicData.sizeThumb,
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
                int p = srcImg.getRGB(x, y);

                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                //calculate average
                float avg = (float) (r + g + b) / 255;
                // float avg = 1.0f * (r + g + b) / (3 * 255);

                int rr = (int) (avg * c.getRed());
                int gg = (int) (avg * c.getGreen());
                int bb = (int) (avg * c.getBlue());
                if (rr > 255) {
                    rr = 255;
                }
                if (gg > 255) {
                    gg = 255;
                }
                if (bb > 255) {
                    bb = 255;
                }

                p = (a << 24) | (rr << 16) | (gg << 8) | bb;
                img.setRGB(x, y, p);
            }
        }
        return img;
    }
}
