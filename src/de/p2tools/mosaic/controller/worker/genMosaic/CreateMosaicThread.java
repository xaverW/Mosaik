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
import de.p2tools.mosaic.controller.config.ProgConst;
import de.p2tools.mosaic.controller.data.mosaikData.MosaicData;
import de.p2tools.mosaic.controller.data.thumb.ThumbCollection;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.image.ImgFile;
import de.p2tools.p2Lib.image.ImgTools;
import de.p2tools.p2Lib.tools.log.Duration;
import javafx.application.Platform;

import javax.swing.event.EventListenerList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class CreateMosaicThread implements Runnable {
    private final ThumbCollection thumbCollection;
    private final MosaicData mosaicData;
    private final CreateMosaicFromThumbs createMosaicFromThumbs = new CreateMosaicFromThumbs();
    private final CreateMosaicFromSrcImage createMosaicFromSrcImage = new CreateMosaicFromSrcImage();
    private final EventListenerList listeners;
    private final MosaicData.THUMB_SRC thumbSrc;

    private boolean stopAll = false;
    private int progressLines = 0;

    private final ArrayList<CreateMosaicData> createMosaicDataArrayList = new ArrayList<>();
    private final String srcImgStr, destImgStr;
    private int maxLines = 0;
    private BufferedImage srcImg, imgOut;
    private int srcHeight, srcWidth, thumbSize, borderSize;
    private int numThumbsWidth, numPixelProThumb, numThumbsHeight;

    public CreateMosaicThread(MosaicData.THUMB_SRC thumbSrc, String srcImgStr, String destImgStr,
                              ThumbCollection thumbCollection,
                              MosaicData mosaicData, EventListenerList listeners) {
        this.thumbSrc = thumbSrc;
        this.srcImgStr = srcImgStr;
        this.destImgStr = destImgStr;
        this.thumbCollection = thumbCollection;
        this.mosaicData = mosaicData;
        this.listeners = listeners;
    }

    public void setStop() {
        stopAll = true;
        createMosaicFromThumbs.setStop();
        createMosaicFromSrcImage.setStop();
    }

    @Override
    public synchronized void run() {

        Duration.counterStart("Mosaik erstellen");
        try {
            thumbCollection.getThumbList().resetAnz();
            srcImg = ImgFile.getBufferedImage(new File(this.srcImgStr));

            srcHeight = srcImg.getRaster().getHeight();
            srcWidth = srcImg.getRaster().getWidth();
            thumbSize = mosaicData.getThumbSize();

            numThumbsWidth = mosaicData.getNumberThumbsWidth();
            numPixelProThumb = srcWidth / numThumbsWidth;
            numThumbsHeight = srcHeight / numPixelProThumb;
            maxLines = numThumbsHeight;
            borderSize = mosaicData.getBorderSize();

            int destWidth, destHeight;
            if (mosaicData.isAddBorder()) {
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

            if (mosaicData.getBackGround().equals(MosaicData.BACKGROUND.IMAGE.toString()) &&
                    mosaicData.getBgPic().isEmpty()) {
                showErrMsg("Es soll ein Hintergrundbild verwendet werden, es ist aber keines angegeben.");
                return;
            }


            if (!mosaicData.isAddBorder()) {
                imgOut = ImgFile.getBufferedImage(destWidth, destHeight);

            } else if (mosaicData.getBackGround().equals(MosaicData.BACKGROUND.IMAGE.toString())) {
                BufferedImage bgImg = ImgFile.getBufferedImage(new File(mosaicData.getBgPic()));
                imgOut = ImgFile.getBufferedImage(destWidth, destHeight, bgImg);

            } else {
                imgOut = ImgFile.getBufferedImage(destWidth, destHeight, mosaicData.getBorderColor());
            }


            // ===================================================
            // los gehts
            notifyEvent(maxLines, 0, "Mosaik erstellen");

            if (thumbSrc.equals(MosaicData.THUMB_SRC.THUMBS)) {
                // ===================================================
                // mosaik from thumbs
                if (!createMosaicFromThumbs()) {
                    stopAll = true;
                }


            } else if (thumbSrc.equals(MosaicData.THUMB_SRC.SRC_FOTO)) {
                // ===================================================
                // mosaik from source image
                createMosaicFromSrcImg();
            }


            if (stopAll) {
                notifyEvent(0, 0, "Abbruch");
                Duration.counterStop("Mosaik erstellen");
                return;
            }

            // Schwarz/Weis
            if (mosaicData.isBlackWhite()) {
                ImgTools.changeToGrayscale(imgOut);
            }

            // ===================================================
            //fertig
            notifyEvent(maxLines, progressLines, "Speichern");
            ImgFile.writeImage(imgOut, destImgStr, mosaicData.getFormat(), ProgConst.IMG_JPG_COMPRESSION);

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

    private boolean createMosaicFromThumbs() {
        // mosaik from thumbs
        final ColorCollection colorCollection = new ColorCollection(thumbCollection);
        for (int yy = 0; yy < numThumbsHeight && !stopAll; ++yy) {
            CreateMosaicData createMosaicData = new CreateMosaicData(imgOut, srcImg, null, colorCollection,
                    thumbSize, yy, numThumbsWidth, numThumbsHeight, numPixelProThumb,
                    mosaicData.getResizeThumb(), mosaicData.getBorderSize(), mosaicData.isAddBorder());

            createMosaicDataArrayList.add(createMosaicData);

        }

        return createMosaicFromThumbs.create(listeners, createMosaicDataArrayList);
    }

    private void createMosaicFromSrcImg() {
        BufferedImage imgSrcSmall = ImgTools.scaleBufferedImage(srcImg, thumbSize, thumbSize);
        for (int yy = 0; yy < numThumbsHeight && !stopAll; ++yy) {
            CreateMosaicData createMosaicData = new CreateMosaicData(imgOut, srcImg, imgSrcSmall, null,
                    thumbSize, yy, numThumbsWidth, numThumbsHeight, numPixelProThumb,
                    mosaicData.getResizeThumb(), mosaicData.getBorderSize(), mosaicData.isAddBorder());

            createMosaicDataArrayList.add(createMosaicData);
        }
        createMosaicFromSrcImage.create(listeners, createMosaicDataArrayList);
    }

    private void showErrMsg(String msg) {
        Platform.runLater(() ->
                PAlert.showErrorAlert("Mosaik erstellen", msg));
    }

    private void notifyEvent(int max, int progress, String text) {
        RunEvent event;
        event = new RunEvent(this, progress, max, text);
        for (RunListener l : listeners.getListeners(RunListener.class)) {
            l.notify(event);
        }
    }
}
