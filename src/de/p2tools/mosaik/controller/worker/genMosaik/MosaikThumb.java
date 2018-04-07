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
import de.p2tools.mosaik.controller.data.thumb.Thumb;
import de.p2tools.mosaik.controller.data.thumb.ThumbCollection;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.image.ImgFile;
import de.p2tools.p2Lib.image.ImgTools;
import de.p2tools.p2Lib.tools.Duration;
import de.p2tools.p2Lib.tools.PException;
import de.p2tools.p2Lib.tools.log.PLog;
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
    private int progressLines = 0;
    private int maxLines = 0;
    private EventListenerList listeners;
    private boolean loadErr = false;
    private String errMsg = "";

    public MosaikThumb(String src, String dest, ThumbCollection thumbCollection,
                       MosaikData mosaikData, EventListenerList listeners) {
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
            BufferedImage srcImg = ImgFile.getBufferedImage(new File(src));

            final int srcHeight = srcImg.getRaster().getHeight();
            final int srcWidth = srcImg.getRaster().getWidth();
            final int thumbSize = mosaikData.getThumbSize();

            final int numThumbsWidth = mosaikData.getNumberThumbsWidth();
            final int numPixelProThumb = srcWidth / numThumbsWidth;
            final int numThumbsHeight = srcHeight / numPixelProThumb;
            final int borderSize = mosaikData.getBorderSize();

            int destWidth, destHeight;
            if (mosaikData.isAddBorder()) {
                destWidth = numThumbsWidth * thumbSize + (1 + numThumbsWidth) * borderSize;
                destHeight = numThumbsHeight * thumbSize + (1 + numThumbsHeight) * borderSize;
            } else {
                destWidth = numThumbsWidth * thumbSize;
                destHeight = numThumbsHeight * thumbSize;
            }

            if (destWidth >= ImgTools.JPEG_MAX_DIMENSION || destHeight >= ImgTools.JPEG_MAX_DIMENSION) {
                showErrMsg("Die Maximale Größe des Mosaiks ist überschritten.\n" +
                        "(Es darf maximal eine Kantenlänge von " + ImgTools.JPEG_MAX_DIMENSION + " Pixeln haben.");
                return;
            }


            final BufferedImage imgOut = ImgFile.getBufferedImage(destWidth, destHeight, mosaikData.getBorderColor());
            final ArrayList<GenImgData> genImgDataArrayList = new ArrayList<>();
            final ColorCollection colorCollection = new ColorCollection(thumbCollection);

            //Bild zusammenbauen
            maxLines = numThumbsHeight;
            notifyEvent(maxLines, 0, "Mosaik erstellen");
            for (int yy = 0; yy < numThumbsHeight && !stopAll; ++yy) {
                GenImgData genImgData = new GenImgData(imgOut, srcImg, null, colorCollection,
                        thumbSize, yy, numThumbsWidth, numThumbsHeight, numPixelProThumb,
                        mosaikData.getResizeThumb(), mosaikData.getBorderSize(), mosaikData.isAddBorder());

                genImgDataArrayList.add(genImgData);
            }

            if (ProgData.saveMem) {
                genImgDataArrayList.stream().forEach(genImgData -> {
                    try {
                        generatePixel(genImgData);
                    } catch (PException e) {
                        loadErr = true;
                        errMsg = e.getMsg();
                    }
                });


            } else {
                genImgDataArrayList.parallelStream().forEach(genImgData -> {
                    try {
                        generatePixel(genImgData);
                    } catch (PException e) {
                        loadErr = true;
                        errMsg = e.getMsg();
                    }
                });
            }


            if (loadErr) {
                showErrMsg("Das Mosaik kann nicht richtig erstellt werden!" + "\n\n" + errMsg);
            }
            if (loadErr || stopAll) {
                notifyEvent(0, 0, "Abbruch");
                Duration.counterStop("Mosaik erstellen");
                return;
            }

            // Schwarz/Weis
            if (mosaikData.isBlackWhite()) {
                ImgTools.changeToGrayscale(imgOut);
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
            notifyEvent(maxLines, progressLines, "");
            notifyEvent(0, 0, "");
            Duration.counterStop("Mosaik erstellen");
        }

    }

    private void showErrMsg(String msg) {
        Platform.runLater(() ->
                PAlert.showErrorAlert("Mosaik erstellen", msg));
    }


    private void generatePixel(GenImgData genImgData) throws PException {
        try {
            if (stopAll) {
                return;
            }

            ++progressLines;
            notifyEvent(maxLines, progressLines, "Zeile " + progressLines + " von " + maxLines +
                    (maxLines == 0 ? "" : " [" + 100 * progressLines / maxLines + " Prozent]"));

            for (int xx = 0; xx < genImgData.numThumbsWidth && !stopAll; ++xx) {

                Color c = ImgTools.getColor(genImgData.srcImg.getSubimage(xx * genImgData.numPixelProThumb,
                        genImgData.yy * genImgData.numPixelProThumb,
                        genImgData.numPixelProThumb, genImgData.numPixelProThumb));
                Thumb thumb = genImgData.colorCollection.getThumb(c, anz);

                if (thumb != null) {
                    thumb.addAnz();
                    File file = new File(thumb.getFileName());

                    BufferedImage buffImg = ImgFile.getBufferedImage(file);
                    if (buffImg == null) {
                        throw new PException("Es sind nicht mehr alle Miniaturbilder vorhanden. " +
                                "Bitte die Liste der Miniaturbilder " +
                                "neu einlesen.");
                    }
                    buffImg = ImgTools.scaleBufferedImage(buffImg, genImgData.sizeThumb, genImgData.sizeThumb);

                    if (genImgData.addBorder) {
                        // border
                        genImgData.imgOut.getRaster().setRect(xx * genImgData.sizeThumb + (1 + xx) * genImgData.borderSize,
                                genImgData.yy * genImgData.sizeThumb + (1 + genImgData.yy) * genImgData.borderSize,
                                buffImg.getData());

                    } else {
                        // no border
                        genImgData.imgOut.getRaster().setRect(xx * genImgData.sizeThumb,
                                genImgData.yy * genImgData.sizeThumb, buffImg.getData());
                    }

                } else {
                    PLog.errorLog(912365478, "thumb konnte nicht gefunden werden.");
                }
            }
        } catch (PException ex) {
            throw ex;
        } catch (Exception ex) {
            PLog.errorLog(642101787, ex);
        }
    }
}
