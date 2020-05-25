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
import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.image.ImgFile;
import de.p2tools.p2Lib.image.ImgTools;
import de.p2tools.p2Lib.tools.PException;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.application.Platform;

import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class CreateMosaicFromThumbs {
    private EventListenerList listeners;

    private boolean stopAll = false;
    private int anz = 5;
    private int progressLines = 0;
    private int maxLines = 0;
    private String errMsg = "";
    private boolean loadOk = true;

    public boolean create(EventListenerList listeners, ArrayList<CreateMosaicData> createMosaicDataArrayList) {
        this.listeners = listeners;
        this.maxLines = createMosaicDataArrayList.size();

        if (ProgData.saveMem) {
            createMosaicDataArrayList.stream().forEach(createMosaicData -> {
                try {
                    generatePixel(createMosaicData);
                } catch (PException e) {
                    loadOk = false;
                    errMsg = e.getMsg();
                }
            });


        } else {
            createMosaicDataArrayList.parallelStream().forEach(createMosaicData -> {
                try {
                    generatePixel(createMosaicData);
                } catch (PException e) {
                    loadOk = false;
                    errMsg = e.getMsg();
                }
            });
        }

        if (!loadOk) {
            Platform.runLater(() ->
                    PAlert.showErrorAlert("Mosaik erstellen",
                            "Das Mosaik kann nicht richtig erstellt werden!" + P2LibConst.LINE_SEPARATORx2 + errMsg));
        }

        return loadOk;
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

    private void generatePixel(CreateMosaicData createMosaicData) throws PException {
        try {
            if (stopAll) {
                return;
            }

            ++progressLines;
            notifyEvent(maxLines, progressLines, "Zeile " + progressLines + " von " + maxLines +
                    (maxLines == 0 ? "" : " [" + 100 * progressLines / maxLines + " Prozent]"));

            for (int xx = 0; xx < createMosaicData.numThumbsWidth && !stopAll; ++xx) {

                Color c = ImgTools.getColor(createMosaicData.srcImg.getSubimage(xx * createMosaicData.numPixelProThumb,
                        createMosaicData.yy * createMosaicData.numPixelProThumb,
                        createMosaicData.numPixelProThumb, createMosaicData.numPixelProThumb));
                Thumb thumb = createMosaicData.colorCollection.getThumb(c, anz);

                if (thumb != null) {
                    thumb.addAnz();
                    File file = new File(thumb.getFileName());

                    BufferedImage buffImg = ImgFile.getBufferedImage(file);
                    if (buffImg == null) {
                        throw new PException("Es sind nicht mehr alle Miniaturbilder vorhanden. " +
                                "Bitte die Liste der Miniaturbilder " +
                                "neu einlesen.");
                    }
                    buffImg = ImgTools.scaleBufferedImage(buffImg, createMosaicData.sizeThumb, createMosaicData.sizeThumb);

                    if (createMosaicData.addBorder) {
                        // border
                        createMosaicData.imgOut.getRaster().setRect(xx * createMosaicData.sizeThumb + (1 + xx) * createMosaicData.borderSize,
                                createMosaicData.yy * createMosaicData.sizeThumb + (1 + createMosaicData.yy) * createMosaicData.borderSize,
                                buffImg.getData());

                    } else {
                        // no border
                        createMosaicData.imgOut.getRaster().setRect(xx * createMosaicData.sizeThumb,
                                createMosaicData.yy * createMosaicData.sizeThumb, buffImg.getData());
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
