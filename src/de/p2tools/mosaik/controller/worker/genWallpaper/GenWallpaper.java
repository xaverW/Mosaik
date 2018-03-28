/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.mosaik.controller.worker.genWallpaper;

import de.p2tools.mosaik.controller.RunEvent;
import de.p2tools.mosaik.controller.RunListener;
import de.p2tools.mosaik.controller.config.ProgConst;
import de.p2tools.mosaik.controller.data.mosaikData.MosaikData;
import de.p2tools.mosaik.controller.data.mosaikData.WallpaperData;
import de.p2tools.mosaik.controller.data.thumb.Thumb;
import de.p2tools.mosaik.controller.data.thumb.ThumbCollection;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.image.ImgFile;
import de.p2tools.p2Lib.image.ImgTools;
import de.p2tools.p2Lib.tools.Duration;
import de.p2tools.p2Lib.tools.Log;
import de.p2tools.p2Lib.tools.PRandom;
import javafx.application.Platform;

import javax.swing.event.EventListenerList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GenWallpaper {
    private EventListenerList listeners = new EventListenerList();
    private ThumbCollection thumbCollection;
    private String dest;
    private boolean stopAll = false;
    private int numThumbsWidth, numThumbsHeight, sumAllThumbs;
    private int thumbSize;
    private Path destPathName = null;
    //    private String thumbResize;
    private String background = "";
    private String backgroundImg = "";

    private boolean addBorder = false;
    private int borderSize = 0;
    private String borderColor;

    public GenWallpaper() {
    }

    /**
     * @param listener
     */
    public void addAdListener(RunListener listener) {
        listeners.add(RunListener.class, listener);
    }

    public void setStop() {
        stopAll = true;
    }

    public void create(ThumbCollection thumbCollection, WallpaperData wallpaperData) {
        this.thumbCollection = thumbCollection;
        this.dest = wallpaperData.getFotoDest();
        this.numThumbsWidth = wallpaperData.getNumberThumbsWidth();
        this.thumbSize = wallpaperData.getThumbSize();
//        this.thumbResize = wallpaperData.getResizeThumb();

        this.addBorder = wallpaperData.isAddBorder();
        this.borderSize = wallpaperData.getBorderSize();
        this.borderColor = wallpaperData.getBorderColor();
        this.background = wallpaperData.getBackGround();
        this.backgroundImg = wallpaperData.getBgPic();

        if (dest.isEmpty()) {
            Log.errorLog(945120364, "Keine Zieldatei angegeben!");
            return;
        }

        if (wallpaperData.getFormat().equals(ImgFile.IMAGE_FORMAT_PNG) &&
                !dest.endsWith("." + ImgFile.IMAGE_FORMAT_PNG)) {
            dest += "." + ImgFile.IMAGE_FORMAT_PNG;

        } else if (!dest.endsWith("." + ImgFile.IMAGE_FORMAT_JPG)) {
            dest += "." + ImgFile.IMAGE_FORMAT_JPG;
        }

        destPathName = Paths.get(dest);
        if (destPathName.toFile().exists() &&
                !new PAlert().showAlert_yes_no("Ziel existiert", destPathName.toString(),
                        "Soll die bereits vorhandene Datei überschrieben werden?").equals(PAlert.BUTTON.YES)) {
            return;
        }

        Path p = Paths.get(dest).getParent();
        if (!p.toFile().exists()) {
            p.toFile().mkdirs();
        }

        final int thumbListSize = thumbCollection.getThumbList().getSize();
        if (thumbListSize <= 0) {
            return;
        }

        stopAll = false;
        GenerateWallPaper generateWallPaper = new GenerateWallPaper();
        Thread startenThread = new Thread(generateWallPaper);
        startenThread.setDaemon(true);
        startenThread.start();
    }

    private class GenerateWallPaper implements Runnable {

        @Override
        public synchronized void run() {

            Duration.counterStart("Mosaik erstellen");
            try {
                final int thumbListSize = thumbCollection.getThumbList().getSize();

                if (thumbListSize < numThumbsWidth) {
                    numThumbsWidth = thumbListSize;
                }

                numThumbsHeight = thumbListSize / numThumbsWidth;
                if (thumbListSize % numThumbsWidth != 0) {
                    ++numThumbsHeight;
                }
                sumAllThumbs = numThumbsWidth * numThumbsHeight;


                int destHeight = numThumbsHeight * thumbSize;
                int destWidth = numThumbsWidth * thumbSize;
                if (addBorder) {
                    destHeight += (1 + numThumbsHeight) * borderSize;
                    destWidth += (1 + numThumbsWidth) * borderSize;
                }

                if (destWidth >= ImgTools.JPEG_MAX_DIMENSION || destHeight >= ImgTools.JPEG_MAX_DIMENSION) {
                    showErrMsg("Die Maximale Größe des Mosaiks ist überschritten.\n" +
                            "(Es darf maximal eine Kantenlänge von " + ImgTools.JPEG_MAX_DIMENSION + " Pixeln haben.");
                    return;
                }

                if (background.equals(MosaikData.BACKGROUND.IMAGE.toString()) &&
                        backgroundImg.isEmpty()) {
                    showErrMsg("Es soll ein Hintergrundbild verwendet werden, es ist aber keines angegeben.");
                    return;
                }

                // =======================================
                // los gehts
                notifyEvent(thumbListSize, 0, "Fototapete erstellen");

                int hh = 0, ww = 0;
//                final BufferedImage imgOut =  ImgFile.getBufferedImage(destWidth, destHeight, borderColor);
                final BufferedImage imgOut;

                if (!addBorder) {
                    imgOut = ImgFile.getBufferedImage(destWidth, destHeight);

                } else if (background.equals(MosaikData.BACKGROUND.IMAGE.toString())) {
                    BufferedImage bgImg = ImgFile.getBufferedImage(new File(backgroundImg));
                    imgOut = ImgFile.getBufferedImage(destWidth, destHeight, bgImg);

                } else {
                    imgOut = ImgFile.getBufferedImage(destWidth, destHeight, borderColor);
                }


                List<Integer> getList = PRandom.getShuffleList(sumAllThumbs, thumbListSize - 1);

                for (int i = 0; i < sumAllThumbs && !stopAll; ++i) {
                    notifyEvent(sumAllThumbs, i, thumbListSize == 0 ? "" : 100 * i / sumAllThumbs + " Prozent");


                    final Thumb thumb = thumbCollection.getThumbList().get(getList.get(i));
                    BufferedImage img = ImgFile.getBufferedImage(new File(thumb.getFileName()));
                    if (img == null) {
                        showErrMsg("Es sind nicht mehr alle Miniaturbilder vorhanden. " +
                                "Bitte die Liste der Miniaturbilder " +
                                "neu einlesen.");
                        stopAll = true;
                        break;
                    }


                    if (img.getWidth() != thumbSize) {
                        img = ImgTools.scaleBufferedImage(img, thumbSize, thumbSize);
                    }


                    if (addBorder) {
                        imgOut.getRaster().setRect(ww * thumbSize + (1 + ww) * borderSize,
                                hh * thumbSize + (1 + hh) * borderSize, img.getData());
                    } else {
                        imgOut.getRaster().setRect(ww * thumbSize, hh * thumbSize, img.getData());
                    }


                    ++ww;
                    if (ww >= numThumbsWidth) {
                        ww = 0;
                        ++hh;
                    }
                }


                if (!stopAll) {
                    notifyEvent(thumbListSize, thumbListSize, "Datei schreiben");
                    ImgFile.writeImage(imgOut, destPathName, ImgFile.ImgFormat.JPG, ProgConst.IMG_JPG_COMPRESSION);
                }
                notifyEvent(0, 0, "");

            } catch (Exception ex) {
                showErrMsg("Das Mosaik kann nicht richtig erstellt werden!");
            } catch (OutOfMemoryError E) {
                showErrMsg("Das Mosaik kann nicht erstellt werden, das Programm " +
                        "hat zu wenig Arbeitsspeicher!");
            }

        }

    }

    private void showErrMsg(String msg) {
        Platform.runLater(() ->
                PAlert.showErrorAlert("Fototapete erstellen", msg));
    }


    private void notifyEvent(int max, int progress, String text) {
        RunEvent event;
        event = new RunEvent(this, progress, max, text);
        for (RunListener l : listeners.getListeners(RunListener.class)) {
            l.notify(event);
        }
    }
}
