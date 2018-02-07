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


package de.p2tools.controller.worker.genMosaik;

import de.p2tools.controller.RunEvent;
import de.p2tools.controller.RunListener;
import de.p2tools.controller.data.mosaikData.MosaikData;
import de.p2tools.controller.data.thumb.Thumb;
import de.p2tools.controller.data.thumb.ThumbCollection;
import de.p2tools.controller.worker.genThumbList.ScaleImage;
import de.p2tools.p2Lib.image.ImgTools;
import de.p2tools.p2Lib.tools.Duration;
import de.p2tools.p2Lib.tools.Log;

import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class MosaikThumb implements Runnable {
    String src, dest;
    ThumbCollection thumbCollection;
    MosaikData mosaikData;
    private boolean stopAll = false;
    private int anz = 5;
    private int progress = 0;
    private EventListenerList listeners;

    public MosaikThumb(String src, String dest, ThumbCollection thumbCollection, MosaikData mosaikData, EventListenerList listeners) {
        this.src = src;
        this.dest = dest;
        this.thumbCollection = thumbCollection;
        this.mosaikData = mosaikData;
        this.listeners = listeners;
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


    public synchronized void run() {

        Duration.counterStart("Mosaik erstellen");
        try {

            thumbCollection.getThumbList().resetAnz();
            BufferedImage imgOut;
            BufferedImage srcImg = ImgTools.getBufferedImage(new File(src));

            int srcHeight = srcImg.getRaster().getHeight();
            int srcWidth = srcImg.getRaster().getWidth();
            int sizeThumb = mosaikData.getThumbSize();

            int numThumbsWidth = mosaikData.getNumberThumbsWidth();
            int numPixelProThumb = srcWidth / numThumbsWidth;
            int numThumbsHeight = srcHeight / numPixelProThumb;

            int destWidth = numThumbsWidth * sizeThumb;
            int destHeight = numThumbsHeight * sizeThumb;

            imgOut = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);

            //Bild zusammenbauen
            Thumb thumb;
            Color c;
            int maxRun = numThumbsHeight * numThumbsWidth;
            notifyEvent(maxRun, 0, "");
            Farbraum farbraum = new Farbraum(thumbCollection);
            File file;
            BufferedImage buffImg;
            for (int yy = 0; yy < numThumbsHeight && !stopAll; ++yy) {
                System.out.println("yy " + yy + " von " + numThumbsHeight);
                GenImgDate genImgDate = new GenImgDate(imgOut, srcImg, farbraum,
                        sizeThumb, yy, numThumbsWidth, numPixelProThumb);
                genImgDateArrayList.add(genImgDate);

//                for (int xx = 0; xx < numThumbsWidth && !stopAll; ++xx) {
//                    ++progress;
//                    notifyEvent(maxRun, progress, "Zeilen: " + yy);
//                    c = ImgTools.getColor(srcImg.getSubimage(xx * numPixelProThumb, yy * numPixelProThumb,
//                            numPixelProThumb, numPixelProThumb));
//
//                    thumb = farbraum.getThumb(c, anz);
//                    if (thumb != null) {
//                        thumb.addAnz();
//                        file = new File(thumb.getFileName());
//                        buffImg = ImgTools.getBufferedImage(file);
//                        buffImg = ScaleImage.scaleBufferedImage(buffImg, sizeThumb, sizeThumb);
//                        imgOut.getRaster().setRect(xx * sizeThumb, yy * sizeThumb, buffImg.getData());
//                    } else {
//                        Log.errorLog(981021036, "MosaikErstellen.tus-Farbe fehlt!!");
//                    }
//                }
            }

            new GenImg().run();
            
            if (stopAll) {
                notifyEvent(0, 0, "");
                Duration.counterStop("Mosaik erstellen");
                return;
            }

            // Schwarz/Weis
            if (mosaikData.isBlackWhite()) {
                ImgTools.changeToGrayscale(imgOut);
            }

            //fertig
            notifyEvent(maxRun, progress, "Speichern");
            ImgTools.writeImage(imgOut, dest, mosaikData.getFormat());
            notifyEvent(0, 0, "");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        Duration.counterStop("Mosaik erstellen");
    }

    private class GenImgDate {
        BufferedImage imgOut;
        BufferedImage srcImg;
        Farbraum farbraum;
        int sizeThumb;
        int yy;
        int numThumbsWidth;
        int numPixelProThumb;

        public GenImgDate(BufferedImage imgOut, BufferedImage srcImg, Farbraum farbraum,
                          int sizeThumb, int yy, int numThumbsWidth, int numPixelProThumb) {
            this.imgOut = imgOut;
            this.srcImg = srcImg;
            this.farbraum = farbraum;
            this.sizeThumb = sizeThumb;
            this.yy = yy;
            this.numThumbsWidth = numThumbsWidth;
            this.numPixelProThumb = numPixelProThumb;
        }
    }

    private ArrayList<GenImgDate> genImgDateArrayList = new ArrayList<>();

    private synchronized GenImgDate getImgDate() {
        if (genImgDateArrayList.size() > 0) {
            return genImgDateArrayList.get(0);
        }
        return null;
    }

    private class GenImg implements Runnable {
        GenImgDate genImgDate;

        public GenImg() {
        }

        public synchronized void run() {
            while (!stopAll && (genImgDate = getImgDate()) != null) {
                try {
                    for (int xx = 0; xx < genImgDate.numThumbsWidth && !stopAll; ++xx) {
                        ++progress;
                        Color c = ImgTools.getColor(genImgDate.srcImg.getSubimage(xx * genImgDate.numPixelProThumb,
                                genImgDate.yy * genImgDate.numPixelProThumb,
                                genImgDate.numPixelProThumb, genImgDate.numPixelProThumb));
                        Thumb thumb = genImgDate.farbraum.getThumb(c, anz);
                        if (thumb != null) {
                            thumb.addAnz();
                            File file = new File(thumb.getFileName());
                            BufferedImage buffImg;
                            buffImg = ImgTools.getBufferedImage(file);
                            buffImg = ScaleImage.scaleBufferedImage(buffImg, genImgDate.sizeThumb, genImgDate.sizeThumb);
                            setRaster(genImgDate.imgOut, buffImg, genImgDate.sizeThumb, xx, genImgDate.yy);
                        } else {
                            Log.errorLog(912365478, "MosaikErstellen.tus-Farbe fehlt!!");
                        }
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    private synchronized void setRaster(BufferedImage imgOut, BufferedImage buffImg, int sizeThumb, int xx, int yy) {
        imgOut.getRaster().setRect(xx * sizeThumb, yy * sizeThumb, buffImg.getData());
    }
}
