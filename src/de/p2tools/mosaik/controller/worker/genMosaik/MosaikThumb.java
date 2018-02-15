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


package de.p2tools.mosaik.controller.worker.genMosaik;

import de.p2tools.mosaik.controller.RunEvent;
import de.p2tools.mosaik.controller.RunListener;
import de.p2tools.mosaik.controller.config.ProgData;
import de.p2tools.mosaik.controller.data.mosaikData.MosaikData;
import de.p2tools.mosaik.controller.data.thumb.Thumb;
import de.p2tools.mosaik.controller.data.thumb.ThumbCollection;
import de.p2tools.mosaik.controller.worker.genThumbList.ScaleImage;
import de.p2tools.mosaik.gui.dialog.MTAlert;
import de.p2tools.p2Lib.image.ImgFile;
import de.p2tools.p2Lib.image.ImgTools;
import de.p2tools.p2Lib.tools.Duration;
import de.p2tools.p2Lib.tools.Log;
import javafx.application.Platform;

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
            BufferedImage srcImg = ImgFile.getBufferedImage(new File(src));

            final int srcHeight = srcImg.getRaster().getHeight();
            final int srcWidth = srcImg.getRaster().getWidth();
            final int sizeThumb = mosaikData.getThumbSize();

            final int numThumbsWidth = mosaikData.getNumberThumbsWidth();
            final int numPixelProThumb = srcWidth / numThumbsWidth;
            final int numThumbsHeight = srcHeight / numPixelProThumb;

            final int destWidth = numThumbsWidth * sizeThumb;
            final int destHeight = numThumbsHeight * sizeThumb;

            if (destWidth >= ImgTools.JPEG_MAX_DIMENSION || destHeight >= ImgTools.JPEG_MAX_DIMENSION) {
                Platform.runLater(() ->
                        MTAlert.showErrorAlert("Mosaik erstellen", "Die Maximale Größe des Mosaiks ist überschritten.\n" +
                                "(Es darf maximal eine Kantenlänge von " + ImgTools.JPEG_MAX_DIMENSION + " Pixeln haben.")
                );
                return;
            }

            
            //Bild zusammenbauen
            final BufferedImage imgOut = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
            final int maxRun = numThumbsHeight * numThumbsWidth;
            final Farbraum farbraum = new Farbraum(thumbCollection);
            final ArrayList<GenImgData> genImgDataArrayList = new ArrayList<>();

            notifyEvent(maxRun, 0, "");
            for (int yy = 0; yy < numThumbsHeight && !stopAll; ++yy) {
                GenImgData genImgData = new GenImgData(imgOut, srcImg, farbraum,
                        sizeThumb, yy, maxRun, numThumbsWidth, numThumbsHeight, numPixelProThumb);
                genImgDataArrayList.add(genImgData);
            }

            if (ProgData.saveMem) {
                genImgDataArrayList.stream().forEach(genImgData -> run(genImgData));
            } else {
                genImgDataArrayList.parallelStream().forEach(genImgData -> run(genImgData));
            }

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
            ImgFile.writeImage(imgOut, dest, mosaikData.getFormat());
            notifyEvent(0, 0, "");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } catch (OutOfMemoryError E) {
            Platform.runLater(() ->
                    MTAlert.showErrorAlert("Mosaik erstellen", "Das Mosaik kann nicht erstellt werden, das Programm " +
                            "hat zu wenig Arbeitsspeicher!")
            );
        }

        Duration.counterStop("Mosaik erstellen");
    }

    private class GenImgData {
        BufferedImage imgOut;
        BufferedImage srcImg;
        Farbraum farbraum;
        int sizeThumb;
        int yy;
        int maxRun;
        int numThumbsWidth;
        int numThumbsHeight;
        int numPixelProThumb;

        public GenImgData(BufferedImage imgOut, BufferedImage srcImg, Farbraum farbraum,
                          int sizeThumb, int yy, int maxRun, int numThumbsWidth, int numThumbsHeight, int numPixelProThumb) {
            this.imgOut = imgOut;
            this.srcImg = srcImg;
            this.farbraum = farbraum;
            this.sizeThumb = sizeThumb;
            this.yy = yy;
            this.maxRun = maxRun;
            this.numThumbsWidth = numThumbsWidth;
            this.numThumbsHeight = numThumbsHeight;
            this.numPixelProThumb = numPixelProThumb;
        }
    }


    private void run(GenImgData genImgData) {
        try {
            if (stopAll) {
                return;
            }

            notifyEvent(genImgData.maxRun, progress, "Pixel: " + progress);
            for (int xx = 0; xx < genImgData.numThumbsWidth && !stopAll; ++xx) {
                ++progress;
                Color c = ImgTools.getColor(genImgData.srcImg.getSubimage(xx * genImgData.numPixelProThumb,
                        genImgData.yy * genImgData.numPixelProThumb,
                        genImgData.numPixelProThumb, genImgData.numPixelProThumb));
                Thumb thumb = genImgData.farbraum.getThumb(c, anz);
                if (thumb != null) {
                    thumb.addAnz();
                    File file = new File(thumb.getFileName());
                    BufferedImage buffImg;
                    buffImg = ImgFile.getBufferedImage(file);
                    buffImg = ScaleImage.scaleBufferedImage(buffImg, genImgData.sizeThumb, genImgData.sizeThumb);
                    genImgData.imgOut.getRaster().setRect(xx * genImgData.sizeThumb,
                            genImgData.yy * genImgData.sizeThumb, buffImg.getData());
                } else {
                    Log.errorLog(912365478, "MosaikErstellen.tus-Farbe fehlt!!");
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
