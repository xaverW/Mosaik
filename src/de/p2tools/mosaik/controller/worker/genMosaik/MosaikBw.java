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
import de.p2tools.mosaik.controller.data.thumb.ThumbCollection;
import de.p2tools.mosaik.controller.worker.genThumbList.ScaleImage;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.image.ImgFile;
import de.p2tools.p2Lib.image.ImgTools;
import de.p2tools.p2Lib.tools.Duration;
import javafx.application.Platform;

import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class MosaikBw implements Runnable {
    String src, dest;
    ThumbCollection thumbCollection;
    MosaikData mosaikData;
    private boolean stopAll = false;
    private int anz = 5;
    private int progress = 0;
    private EventListenerList listeners;

    public MosaikBw(String src, String dest, ThumbCollection thumbCollection, MosaikData mosaikData, EventListenerList listeners) {
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
            BufferedImage imgOut, imgSrcSmall;
            BufferedImage srcImg = ImgFile.getBufferedImage(new File(src));

            int srcHeight = srcImg.getRaster().getHeight();
            int srcWidth = srcImg.getRaster().getWidth();
            int sizeThumb = mosaikData.getThumbSize();

            int numThumbsWidth = mosaikData.getNumberThumbsWidth();
            int numPixelProThumb = srcWidth / numThumbsWidth;
            int numThumbsHeight = srcHeight / numPixelProThumb;

            int destWidth = numThumbsWidth * sizeThumb;
            int destHeight = numThumbsHeight * sizeThumb;
            boolean blackWhite = mosaikData.isBlackWhite();

            if (destWidth >= ImgTools.JPEG_MAX_DIMENSION || destHeight >= ImgTools.JPEG_MAX_DIMENSION) {
                Platform.runLater(() ->
                        PAlert.showErrorAlert("Mosaik erstellen", "Die Maximale Größe des Mosaiks ist überschritten.\n" +
                                "(Es darf maximal eine Kantenlänge von " + ImgTools.JPEG_MAX_DIMENSION + " Pixeln haben.")
                );
                return;
            }

            imgOut = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
            imgSrcSmall = ScaleImage.scaleBufferedImage(srcImg, sizeThumb, sizeThumb);
            final ArrayList<GenImgData> genImgDataArrayList = new ArrayList<>();

            //Bild zusammenbauen
            int maxRun = numThumbsHeight * numThumbsWidth;
            notifyEvent(maxRun, 0, "");
            BufferedImage buffImg;
            for (int yy = 0; yy < numThumbsHeight && !stopAll; ++yy) {
                GenImgData genImgData = new GenImgData(imgOut, srcImg, imgSrcSmall, blackWhite,
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

            //fertig
            notifyEvent(maxRun, progress, "Speichern");
            ImgFile.writeImage(imgOut, dest, mosaikData.getFormat());
            notifyEvent(0, 0, "");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } catch (OutOfMemoryError E) {
            Platform.runLater(() ->
                    PAlert.showErrorAlert("Mosaik erstellen", "Das Mosaik kann nicht erstellt werden, das Programm " +
                            "hat zu wenig Arbeitsspeicher!")
            );
        }
        Duration.counterStop("Mosaik erstellen");

    }

    private class GenImgData {
        BufferedImage imgOut;
        BufferedImage srcImg;
        BufferedImage imgSrcSmall;
        boolean blackWhite;
        int sizeThumb;
        int yy;
        int maxRun;
        int numThumbsWidth;
        int numThumbsHeight;
        int numPixelProThumb;

        public GenImgData(BufferedImage imgOut, BufferedImage srcImg, BufferedImage imgSrcSmall, boolean blackWhite,
                          int sizeThumb, int yy, int maxRun, int numThumbsWidth, int numThumbsHeight, int numPixelProThumb) {
            this.imgOut = imgOut;
            this.srcImg = srcImg;
            this.imgSrcSmall = imgSrcSmall;
            this.blackWhite = blackWhite;
            this.sizeThumb = sizeThumb;
            this.yy = yy;
            this.maxRun = maxRun;
            this.numThumbsWidth = numThumbsWidth;
            this.numThumbsHeight = numThumbsHeight;
            this.numPixelProThumb = numPixelProThumb;
        }
    }

    private void run(GenImgData genImgData) {
        BufferedImage buffImg;

        if (stopAll) {
            return;
        }

        notifyEvent(genImgData.maxRun, progress, "Zeilen: " + genImgData.yy);
        for (int xx = 0; xx < genImgData.numThumbsWidth && !stopAll; ++xx) {
            ++progress;
            Color c = ImgTools.getColor(genImgData.srcImg.getSubimage(xx * genImgData.numPixelProThumb,
                    genImgData.yy * genImgData.numPixelProThumb, genImgData.numPixelProThumb, genImgData.numPixelProThumb));

            buffImg = getImgBw(genImgData.imgSrcSmall, c, genImgData.blackWhite);
            buffImg = ScaleImage.scaleBufferedImage(buffImg, genImgData.sizeThumb, genImgData.sizeThumb);
            genImgData.imgOut.getRaster().setRect(xx * genImgData.sizeThumb, genImgData.yy * genImgData.sizeThumb, buffImg.getData());
        }
    }

    private BufferedImage getImgBw(BufferedImage srcImg, Color c, boolean bw) {
        BufferedImage img = ImgFile.cloneImage(srcImg);

        int width = img.getWidth();
        int height = img.getHeight();

        float avgSrc = 1.0f * (c.getRed() + c.getGreen() + c.getBlue()) * 255 / (3 * 255);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x, y);

                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                if (bw) {
                    //calculate average
                    float avg = 1.0f * (r + g + b) / (3 * 255);
                    int avgI = (int) (avg * avgSrc);

                    p = (a << 24) | (avgI << 16) | (avgI << 8) | avgI;

                } else {
                    //calculate average
                    float avg = 1.0f * (r + g + b) / (3 * 255);

                    p = (a << 24) |
                            ((int) (avg * c.getRed()) << 16) |
                            ((int) (avg * c.getGreen()) << 8) |
                            (int) (avg * c.getBlue());
                }

                img.setRGB(x, y, p);
            }
        }
        return img;
    }
}
