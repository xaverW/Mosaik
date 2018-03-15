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
import de.p2tools.mosaik.controller.config.ProgConst;
import de.p2tools.mosaik.controller.config.ProgData;
import de.p2tools.mosaik.controller.data.mosaikData.MosaikData;
import de.p2tools.mosaik.controller.data.thumb.ThumbCollection;
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

public class MosaikSrcImage implements Runnable {
    private String src, dest;
    private ThumbCollection thumbCollection;
    private MosaikData mosaikData;
    private boolean stopAll = false;
    private int anz = 5;
    private int progressLines = 0;
    private int maxLines = 0;
    private EventListenerList listeners;

    public MosaikSrcImage(String src, String dest, ThumbCollection thumbCollection, MosaikData mosaikData, EventListenerList listeners) {
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


    @Override
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
                showErrMsg("Die Maximale Größe des Mosaiks ist überschritten.\n" +
                        "(Es darf maximal eine Kantenlänge von " + ImgTools.JPEG_MAX_DIMENSION + " Pixeln haben.");
                return;
            }

            imgOut = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
            imgSrcSmall = ImgTools.scaleBufferedImage(srcImg, sizeThumb, sizeThumb);
            final ArrayList<GenImgData> genImgDataArrayList = new ArrayList<>();

            //Bild zusammenbauen
            maxLines = numThumbsHeight;
            notifyEvent(maxLines, 0, "Mosaik erstellen");

            for (int yy = 0; yy < numThumbsHeight && !stopAll; ++yy) {
                GenImgData genImgData = new GenImgData(imgOut, srcImg, imgSrcSmall, blackWhite,
                        sizeThumb, yy, numThumbsWidth, numThumbsHeight, numPixelProThumb);
                genImgDataArrayList.add(genImgData);
            }

            if (ProgData.saveMem) {
                genImgDataArrayList.stream().forEach(genImgData -> genMosaik(genImgData));
            } else {
                genImgDataArrayList.parallelStream().forEach(genImgData -> genMosaik(genImgData));
            }

            if (stopAll) {
                notifyEvent(0, 0, "Abbruch");
                Duration.counterStop("Mosaik erstellen");
                return;
            }

            //fertig
            notifyEvent(maxLines, progressLines, "Speichern");
            ImgFile.writeImage(imgOut, dest, mosaikData.getFormat(), ProgConst.IMG_JPG_COMPRESSION);
        } catch (Exception ex) {
            showErrMsg("Das Mosaik kann nicht richtig erstellt werden!");
        } catch (OutOfMemoryError E) {
            showErrMsg("Das Mosaik kann nicht erstellt werden, das Programm " +
                    "hat zu wenig Arbeitsspeicher!");
        } finally {
            notifyEvent(0, 0, "");
            Duration.counterStop("Mosaik erstellen");
        }
    }

    private void showErrMsg(String msg) {
        Platform.runLater(() ->
                PAlert.showErrorAlert("Mosaik erstellen", msg));

    }

    private class GenImgData {
        BufferedImage imgOut;
        BufferedImage srcImg;
        BufferedImage imgSrcSmall;
        boolean blackWhite;
        int sizeThumb;
        int yy;
        int numThumbsWidth;
        int numThumbsHeight;
        int numPixelProThumb;

        public GenImgData(BufferedImage imgOut, BufferedImage srcImg, BufferedImage imgSrcSmall, boolean blackWhite,
                          int sizeThumb, int yy, int numThumbsWidth, int numThumbsHeight, int numPixelProThumb) {
            this.imgOut = imgOut;
            this.srcImg = srcImg;
            this.imgSrcSmall = imgSrcSmall;
            this.blackWhite = blackWhite;
            this.sizeThumb = sizeThumb;
            this.yy = yy;
            this.numThumbsWidth = numThumbsWidth;
            this.numThumbsHeight = numThumbsHeight;
            this.numPixelProThumb = numPixelProThumb;
        }
    }

    private void genMosaik(GenImgData genImgData) {
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

            buffImg = getImgBw(genImgData.imgSrcSmall, c, genImgData.blackWhite);
            buffImg = ImgTools.scaleBufferedImage(buffImg, genImgData.sizeThumb, genImgData.sizeThumb);
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
