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
import de.p2tools.mosaic.controller.data.mosaikData.MosaicDataBase;
import de.p2tools.mosaic.controller.data.thumb.ThumbCollection;
import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.image.ImgFile;
import de.p2tools.p2Lib.image.ImgTools;
import de.p2tools.p2Lib.tools.PRandom;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.application.Platform;

import javax.swing.event.EventListenerList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CreateMosaicThread implements Runnable {
    private final ThumbCollection thumbCollection;
    private final MosaicData mosaicData;
    private final CreateMosaicFromThumbs createMosaicFromThumbs = new CreateMosaicFromThumbs();
    private final CreateMosaicFromGrayThumbs createMosaicFromGrayThumbs = new CreateMosaicFromGrayThumbs();
    private final CreateMosaicFromColoredThumb createMosaicFromColoredThumb = new CreateMosaicFromColoredThumb();
    private final CreateMosaicFromThumbsAllPixelColored createMosaicFromThumbsAllPixelColored = new CreateMosaicFromThumbsAllPixelColored();
    private final CreateMosaicFromSrcImage createMosaicFromSrcImage = new CreateMosaicFromSrcImage();
    private final EventListenerList listeners;
    private final MosaicDataBase.MOSAIC_TYPE mosaicType;

    private boolean stopAll = false;
    private int progressLines = 0;

    private final ArrayList<CreateMosaicData> createMosaicDataArrayList = new ArrayList<>();
    private final String srcImgStr, destImgStr;
    private BufferedImage srcImg, imgOut;
    private int srcHeight, srcWidth, thumbSize, borderSize;
    private int quantityThumbsWidth, quantityPixelProThumb, quantityThumbsHeight;

    public CreateMosaicThread(MosaicDataBase.MOSAIC_TYPE mosaicType,
                              String srcImgStr, String destImgStr, ThumbCollection thumbCollection,
                              MosaicData mosaicData, EventListenerList listeners) {
        this.mosaicType = mosaicType;
        this.srcImgStr = srcImgStr;
        this.destImgStr = destImgStr;
        this.thumbCollection = thumbCollection;
        this.mosaicData = mosaicData;
        this.listeners = listeners;
    }

    public void setStop() {
        stopAll = true;
        createMosaicFromThumbs.setStop();
        createMosaicFromGrayThumbs.setStop();
        createMosaicFromColoredThumb.setStop();
        createMosaicFromThumbsAllPixelColored.setStop();
        createMosaicFromSrcImage.setStop();
    }

    @Override
    public synchronized void run() {

        PDuration.counterStart("Mosaik erstellen");
        try {
            thumbCollection.getThumbList().resetAnz();

            File srcImgFile = new File(this.srcImgStr);
            if (!srcImgFile.exists()) {
                showErrMsg("Das Bild für die Vorlage des Mosaiks:  " + P2LibConst.LINE_SEPARATOR +
                        srcImgStr + P2LibConst.LINE_SEPARATOR +
                        "existiert nicht, das Mosaik kann nicht erstellt werden.");
                return;
            }
            srcImg = ImgFile.getBufferedImage(srcImgFile);

            srcHeight = srcImg.getRaster().getHeight();
            srcWidth = srcImg.getRaster().getWidth();
            thumbSize = mosaicData.getThumbSize();

            quantityThumbsWidth = mosaicData.getQuantityThumbsWidth();
            quantityPixelProThumb = srcWidth / quantityThumbsWidth;
            quantityThumbsHeight = srcHeight / quantityPixelProThumb;
            borderSize = mosaicData.getBorderSize();

            int destWidth, destHeight;
            if (mosaicData.isAddBorder()) {
                destWidth = quantityThumbsWidth * thumbSize + (1 + quantityThumbsWidth) * borderSize;
                destHeight = quantityThumbsHeight * thumbSize + (1 + quantityThumbsHeight) * borderSize;
            } else {
                destWidth = quantityThumbsWidth * thumbSize;
                destHeight = quantityThumbsHeight * thumbSize;
            }

            if (destWidth >= ImgTools.JPEG_MAX_DIMENSION || destHeight >= ImgTools.JPEG_MAX_DIMENSION) {
                showErrMsg("Die Maximale Größe des Mosaiks ist überschritten. " + P2LibConst.LINE_SEPARATOR +
                        "(Es darf maximal eine Kantenlänge von " + ImgTools.JPEG_MAX_DIMENSION + " Pixeln haben.");
                return;
            }

            if (mosaicData.getBackground().equals(MosaicData.BACKGROUND.IMAGE.toString()) &&
                    mosaicData.getBgPic().isEmpty()) {
                showErrMsg("Es soll ein Hintergrundbild verwendet werden, es ist aber keines angegeben.");
                return;
            }


            // Zielbild vorbereiten (Rahmen, ..)
            if (!mosaicData.isAddBorder()) {
                imgOut = ImgFile.getBufferedImage(destWidth, destHeight);

            } else if (mosaicData.getBackground().equals(MosaicData.BACKGROUND.IMAGE.toString())) {
                BufferedImage bgImg = ImgFile.getBufferedImage(new File(mosaicData.getBgPic()));
                imgOut = ImgFile.getBufferedImage(destWidth, destHeight, bgImg);

            } else {
                imgOut = ImgFile.getBufferedImage(destWidth, destHeight, mosaicData.getBorderColor());
            }


            // ===================================================
            // los gehts
            notifyEvent(quantityThumbsHeight, 0, "Mosaik erstellen");


            if (mosaicType.equals(MosaicDataBase.MOSAIC_TYPE.THUMBS)) {
                // ===================================================
                // mosaik from thumbs
                if (!createMosaicFromThumbs()) {
                    stopAll = true;
                }


            } else if (mosaicType.equals(MosaicDataBase.MOSAIC_TYPE.THUMBS_GRAY)) {
                // ===================================================
                // gray mosaik from thumbs
                if (!createMosaicFromGrayThumbs()) {
                    stopAll = true;
                }


            } else if (mosaicType.equals(MosaicDataBase.MOSAIC_TYPE.THUMBS_COLORED)) {
                // ===================================================
                // mosaik from colored thumbs
                createMosaicFromColoredThumbs();


            } else if (mosaicType.equals(MosaicDataBase.MOSAIC_TYPE.THUMBS_ALL_PIXEL_COLORED)) {
                // ===================================================
                // mosaik from thumbs with color from the src
                createMosaicFromThumbsAllPixelColored();


            } else if (mosaicType.equals(MosaicDataBase.MOSAIC_TYPE.FROM_SRC_IMG)) {
                // ===================================================
                // mosaik from source image
                createMosaicFromSrcImg();
            }


            if (stopAll) {
                notifyEvent(0, 0, "Abbruch");
                PDuration.counterStop("Mosaik erstellen");
                return;
            }

            // ===================================================
            //fertig
            notifyEvent(quantityThumbsHeight, progressLines, "Speichern");
            ImgFile.writeImage(imgOut, destImgStr, mosaicData.getFormat(), ProgConst.IMG_JPG_COMPRESSION);

        } catch (Exception ex) {
            setStop(); // damit andere Threads auch stoppen
            PLog.errorLog(95120124, ex);
            showErrMsg("Das Mosaik kann nicht richtig erstellt werden!");
        } catch (OutOfMemoryError E) {
            setStop(); // damit andere Threads auch stoppen
            PLog.errorLog(951203547, E.getMessage());
            showErrMsg("Das Mosaik kann nicht erstellt werden, das Programm " +
                    "hat zu wenig Arbeitsspeicher!");

        } finally {
            notifyEvent(quantityThumbsHeight, progressLines, "");
            notifyEvent(0, 0, "");
            PDuration.counterStop("Mosaik erstellen");
        }

    }

    private boolean createMosaicFromThumbs() {
        final ColorCollection colorCollection = new ColorCollection(thumbCollection);
        createMosaicData(colorCollection, null, null);
        return createMosaicFromThumbs.create(listeners, createMosaicDataArrayList);
    }

    private boolean createMosaicFromGrayThumbs() {
        final GrayValueCollection colorCollection = new GrayValueCollection(thumbCollection);
        createMosaicData(colorCollection, null, null);
        return createMosaicFromGrayThumbs.create(listeners, createMosaicDataArrayList);
    }

    private void createMosaicFromColoredThumbs() {
        final int thumbListSize = thumbCollection.getThumbList().getSize();
        final int quantityAllThumbs = quantityThumbsHeight * quantityThumbsWidth;
        List<Integer> shuffleList = PRandom.getShuffleList(quantityAllThumbs, thumbListSize - 1);

        createMosaicData(null, null, shuffleList);
        createMosaicFromColoredThumb.create(listeners, createMosaicDataArrayList);
    }

    private void createMosaicFromThumbsAllPixelColored() {
        final int thumbListSize = thumbCollection.getThumbList().getSize();
        final int quantityAllThumbs = quantityThumbsHeight * quantityThumbsWidth;
        List<Integer> shuffleList = PRandom.getShuffleList(quantityAllThumbs, thumbListSize - 1);

        createMosaicData(null, null, shuffleList);
        createMosaicFromThumbsAllPixelColored.create(listeners, createMosaicDataArrayList);
    }

    private void createMosaicFromSrcImg() {
        BufferedImage imgSrcSmall = ImgTools.scaleBufferedImage(srcImg, thumbSize, thumbSize);
        createMosaicData(null, imgSrcSmall, null);
        createMosaicFromSrcImage.create(listeners, createMosaicDataArrayList);
    }

    private void createMosaicData(ColorCollection colorCollection, BufferedImage imgSrcSmall, List<Integer> shuffleList) {
        for (int yy = 0; yy < quantityThumbsHeight && !stopAll; ++yy) {

            final CreateMosaicData createMosaicData = new CreateMosaicData(imgOut, srcImg,
                    imgSrcSmall, colorCollection, shuffleList, thumbCollection,
                    thumbSize, yy, quantityThumbsWidth, quantityThumbsHeight, quantityPixelProThumb,
                    mosaicData.getBorderSize(), mosaicData.isAddBorder());

            createMosaicDataArrayList.add(createMosaicData);
        }
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
