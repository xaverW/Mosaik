/*
 *    Copyright (C) 2008
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.p2tools.mosaic.controller.worker.genMosaic;

import de.p2tools.mosaic.controller.RunListener;
import de.p2tools.mosaic.controller.config.ProgData;
import de.p2tools.mosaic.controller.data.mosaikData.MosaicData;
import de.p2tools.mosaic.controller.data.thumb.ThumbCollection;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.image.ImgFile;
import de.p2tools.p2Lib.tools.log.PLog;

import javax.swing.event.EventListenerList;
import java.io.File;

public class CreateMosaic {

    String dest;
    String src;
    private int anz = 5;
    private EventListenerList listeners = new EventListenerList();

    private ProgData progData;
    private CreateMosaicThread createMosaicThread = null;

    public CreateMosaic(ProgData progData) {
        this.progData = progData;
    }


    /**
     * @param listener
     */
    public void addAdListener(RunListener listener) {
        listeners.add(RunListener.class, listener);
    }

    public void setStop() {
        if (createMosaicThread != null) {
            createMosaicThread.setStop();
        }
    }

    /**
     * @param mosaicData
     */
    public void create(MosaicData mosaicData, ThumbCollection thumbCollection) {
        dest = mosaicData.getFotoDest();
        src = mosaicData.getFotoSrc();
        anz = mosaicData.getThumbCount();

        if (dest.isEmpty()) {
            PLog.errorLog(945120365, "Keine Zieldatei angegeben!");
            return;
        }

        if (mosaicData.getFormat().equals(ImgFile.IMAGE_FORMAT_PNG) &&
                !dest.endsWith("." + ImgFile.IMAGE_FORMAT_PNG)) {
            dest += "." + ImgFile.IMAGE_FORMAT_PNG;

        } else if (!dest.endsWith("." + ImgFile.IMAGE_FORMAT_JPG)) {
            dest += "." + ImgFile.IMAGE_FORMAT_JPG;
        }

        if (new File(dest).exists() &&
                !new PAlert().showAlert_yes_no("Ziel existiert", dest,
                        "Soll die bereits vorhandene Datei überschrieben werden?").equals(PAlert.BUTTON.YES)) {
            return;
        }

        int len = thumbCollection.getThumbList().getSize();
        if (len <= 0) {
            PLog.errorLog(912030578, "Es gibt keine Miniaturbilder!");
            return;
        }


        MosaicData.THUMB_SRC thumbSrc;
        String threadName;
        if (mosaicData.getThumbSrc().equals(MosaicData.THUMB_SRC.THUMBS.toString())) {
            // Mosaik aus Thumbs
            thumbSrc = MosaicData.THUMB_SRC.THUMBS;
            threadName = "MosaicThumb";

        } else if (mosaicData.getThumbSrc().equals(MosaicData.THUMB_SRC.THUMBS_COLOR.toString())) {
            // Mosaik aus ColoredThumbs
            thumbSrc = MosaicData.THUMB_SRC.THUMBS_COLOR;
            threadName = "MosaicColoredThumb";

        } else if (mosaicData.getThumbSrc().equals(MosaicData.THUMB_SRC.THUMBS_ALL_PIXEL_COLOR.toString())) {
            // Mosaik aus Thumbs und alles colored
            thumbSrc = MosaicData.THUMB_SRC.THUMBS_ALL_PIXEL_COLOR;
            threadName = "MosaicAllPixelColored";

        } else {
            // Mosaik aus dem SRC-Image
            thumbSrc = MosaicData.THUMB_SRC.SRC_FOTO;
            threadName = "MosaicFromSrcImage";

        }

        createMosaicThread = new CreateMosaicThread(thumbSrc, src, dest, thumbCollection, mosaicData, listeners);
        Thread thread = new Thread(createMosaicThread);
        thread.setName(threadName);
        thread.setDaemon(true);
        thread.start();
    }

}
